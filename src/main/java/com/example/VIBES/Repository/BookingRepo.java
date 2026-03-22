package com.example.VIBES.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
    

import com.example.VIBES.Model.Booking;
import com.example.VIBES.Model.Bookingstatus;

public interface BookingRepo extends JpaRepository<Booking, Long> {

   List<Booking> findByUser_UsernameOrderByIdDesc(String username);

    @Query("""
        SELECT b FROM Booking b
        WHERE b.rooms.id = :roomId
          AND b.status = :status
          AND b.checkIn < :checkOut
          AND b.checkOut > :checkIn
    """)
    List<Booking> findConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("status") Bookingstatus status
    );
}