package com.example.VIBES.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.VIBES.Model.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Long>{
    Optional<Payment> findByBookingId(Long bookingId);
    
} 