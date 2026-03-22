package com.example.VIBES.DTO;

import com.example.VIBES.Model.Hotel;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Rrequest {
    private String Type;
    private double price; 
    private boolean availability;

    @ManyToOne
    private Hotel hotel; 
    
}
