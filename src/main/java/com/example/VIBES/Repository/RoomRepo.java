package com.example.VIBES.Repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VIBES.Model.Rooms;


@Repository
public interface RoomRepo extends JpaRepository <Rooms ,Long>{
     List<Rooms> findByHotel_Hotelname(String hotelname);
     List<Rooms> findByHotel_Id(Long hotelId);



    
}
