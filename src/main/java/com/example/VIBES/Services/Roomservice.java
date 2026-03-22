package com.example.VIBES.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VIBES.DTO.Rrequest;
import com.example.VIBES.Model.Hotel;
import com.example.VIBES.Model.Rooms;
import com.example.VIBES.Repository.HotelRepo;
import com.example.VIBES.Repository.RoomRepo;

@Service
public class Roomservice {

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private HotelRepo hotelRepo;


    public List<Rooms> getRoomsByHotel(String hotelname) {
    return roomRepo.findByHotel_Hotelname(hotelname);
}

    public List<Rooms> getAvailableRooms(String hotelname, LocalDate checkIn, LocalDate checkOut) {
        return getRoomsByHotel(hotelname);
    }

public Rooms createRoom(String hotelname, Rrequest rrequest) {

    List<Hotel> hotels = hotelRepo.findByHotelname(hotelname);
    if (hotels == null || hotels.isEmpty()) {
        throw new RuntimeException("Hotel not found: " + hotelname);
    }
    Hotel hotel = hotels.get(0);
    Rooms room = new Rooms();
    room.setHotel(hotel);
    room.setType(rrequest.getType());
    room.setPrice(rrequest.getPrice());
    room.setAvailability(rrequest.isAvailability());
    return roomRepo.save(room);
}

public List<Rooms> updateRoomsByHotel(String hotelname, Rrequest rrequest) {

    List<Rooms> rooms = roomRepo.findByHotel_Hotelname(hotelname);

    if (rooms == null || rooms.isEmpty()) {
        throw new RuntimeException("No rooms found for hotel: " + hotelname);
    }

    for (Rooms room : rooms) {
        room.setType(rrequest.getType());
        room.setPrice(rrequest.getPrice());
        room.setAvailability(rrequest.isAvailability());
    }

    return roomRepo.saveAll(rooms);
}

public Rooms updateRoomByHotel(String hotelname, Rrequest rrequest) {

    List<Rooms> rooms = roomRepo.findByHotel_Hotelname(hotelname);

    if (rooms == null || rooms.isEmpty()) {
        throw new RuntimeException("No rooms found for hotel: " + hotelname);
    }

    Rooms room = rooms.get(0);

    room.setType(rrequest.getType());
    room.setPrice(rrequest.getPrice());
    room.setAvailability(rrequest.isAvailability());

    return roomRepo.save(room);
}
public void deleteRoom(Long roomId) {
    Rooms room = roomRepo.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Room not found"));
    roomRepo.delete(room);
}
    
}
