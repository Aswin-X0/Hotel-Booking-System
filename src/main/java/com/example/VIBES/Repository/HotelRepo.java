package com.example.VIBES.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VIBES.Model.Hotel;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long>{
    List<Hotel> findByHotelname(String hotelname); 
    List<Hotel> findByLocation(String location);
    List<Hotel> findByHotelnameIgnoreCase(String hotelname);

} 