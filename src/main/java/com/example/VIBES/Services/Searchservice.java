package com.example.VIBES.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VIBES.Model.Booking;
import com.example.VIBES.Model.Bookingstatus;
import com.example.VIBES.Model.Hotel;
import com.example.VIBES.Model.Rooms;
import com.example.VIBES.Model.Search;
import com.example.VIBES.Repository.BookingRepo;
import com.example.VIBES.Repository.HotelRepo;
import com.example.VIBES.Repository.RoomRepo;

@Service
public class Searchservice {

    @Autowired
    private HotelRepo hotelRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private BookingRepo bookingRepo;

     public List<Search> searchAvailableHotels(String location,LocalDate checkIn, LocalDate checkOut){
        
        if (location == null || location.trim().isEmpty()) return List.of();
        if (checkIn == null || checkOut == null) return List.of();
        if (!checkIn.isBefore(checkOut)) return List.of();

        List<Hotel> hotels = hotelRepo.findByLocation(location.trim());
        List<Search> results = new ArrayList<>();

        for (Hotel hotel : hotels) {
            List<Rooms> roomsList = roomRepo.findByHotel_Hotelname(hotel.getHotelname());
            List<Rooms> availableRooms = new ArrayList<>();
        
        for (Rooms room : roomsList) {

                if (!room.isAvailability()) continue;

                List<Booking> conflicts = bookingRepo.findConflictingBookings(
                        room.getId(),
                        checkIn,
                        checkOut,
                        Bookingstatus.CONFIRMED
                );
                if (conflicts == null || conflicts.isEmpty()) {
                    availableRooms.add(room);
                }
            }
            if (!availableRooms.isEmpty()) {
                results.add(new Search(hotel, availableRooms));
            }
        }
        return results;  
     }
    
}
