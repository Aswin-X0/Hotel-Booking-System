package com.example.VIBES.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VIBES.DTO.Hrequest;
import com.example.VIBES.Model.Hotel;
import com.example.VIBES.Repository.HotelRepo;

@Service
public class HotelService {

    @Autowired
    private HotelRepo hotelRepo;

    public List<Hotel>getallHotels(){
        return hotelRepo.findAll();
    }

    public Hotel getHotelbyHotelname(String hotelname){
        return hotelRepo.findByHotelname(hotelname).stream().findFirst().orElse(null);
    }

    public Hotel getHotelByLocation(String location){
        return hotelRepo.findByLocation(location).stream().findFirst().orElse(null);
    }

    public Hotel Createhotel(Hrequest hrequest){
        Hotel hotel = new Hotel();
        hotel.setHotelname(hrequest.getHotelname());
        hotel.setNumber(hrequest.getNumber());
        hotel.setEmail(hrequest.getEmail());
        hotel.setLocation(hrequest.getLocation());
        hotel.setPrice(hrequest.getPrice());
        hotel.setAmmenities(hrequest.getAmmenities());
        hotel.setRating(hrequest.getRating());
        hotel.setDescription(hrequest.getDescription());
        return hotelRepo.save(hotel);
    }

    public Hotel Updatehotel(String hotelname,Hrequest hrequest){
        Hotel existingHotel = hotelRepo.findByHotelname(hotelname).stream().findFirst().orElse(null);
            if(existingHotel != null){
                existingHotel.setNumber(hrequest.getNumber());
                existingHotel.setEmail(hrequest.getEmail());
                existingHotel.setLocation(hrequest.getLocation());
                existingHotel.setPrice(hrequest.getPrice());
                existingHotel.setAmmenities(hrequest.getAmmenities());
                existingHotel.setRating(hrequest.getRating());
                existingHotel.setDescription(hrequest.getDescription());
                return hotelRepo.save(existingHotel);
            }     
             return null;
    }
    
    public void Deletehotel(String hotelname){
          Hotel hotel = hotelRepo.findByHotelname(hotelname).stream().findFirst().orElse(null);

    if (hotel != null) {
        hotelRepo.delete(hotel);
    } else {
        System.out.println("Hotel not found");
    }
    
}
}
