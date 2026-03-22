package com.example.VIBES.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VIBES.Model.Booking;
import com.example.VIBES.Model.Bookingstatus;
import com.example.VIBES.Model.Rooms;
import com.example.VIBES.Model.User;
import com.example.VIBES.Repository.BookingRepo;
import com.example.VIBES.Repository.RoomRepo;
import com.example.VIBES.Repository.UserRepo;

@Service
public class Bookingservice {
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoomRepo roomRepo;

    public Booking createBooking(String username, Long roomId, LocalDate checkIn, LocalDate checkOut) {

    if (!checkOut.isAfter(checkIn)) {
        throw new RuntimeException("Check-out must be after check-in");
    }

    if (checkIn.isBefore(LocalDate.now())) {
        throw new RuntimeException("Check-in cannot be in the past");
    }
    
    User user = userRepo.findByUsername(username);

    if (user == null) {
        throw new RuntimeException("User not found");
    }

    Rooms room = roomRepo.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Room not found"));

    if (!room.isAvailability()) {
        throw new RuntimeException("Room is disabled");
    }

    List<Booking> conflicts = bookingRepo.findConflictingBookings(
            roomId,
            checkIn,
            checkOut,
            Bookingstatus.CONFIRMED
    );

    if (!conflicts.isEmpty()) {
        throw new RuntimeException("Room already booked for selected dates");
    }

    Booking booking = new Booking();
    booking.setUser(user);        
    booking.setRooms(room);
    booking.setCheckIn(checkIn);
    booking.setCheckOut(checkOut);
    booking.setStatus(Bookingstatus.PENDING);

        return bookingRepo.save(booking);
}

    public Booking markConfirmed(Long bookingId) {
         Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
         booking.setStatus(Bookingstatus.CONFIRMED);
         return bookingRepo.save(booking);
}

    public Booking getById(Long bookingId) {
        return bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
}


    public List<Booking> getBookingsByUser(String username) {
        return bookingRepo.findByUser_UsernameOrderByIdDesc(username);
    }


    public Booking cancelBooking(Long bookingId) {
        
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Bookingstatus.CANCELLED);
        return bookingRepo.save(booking);
    }

    public List<Booking> getallbookings(){
        return bookingRepo.findAll();
    }
}

