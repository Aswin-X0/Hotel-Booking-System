package com.example.VIBES.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hrequest {
    private String hotelname;
    private String number;
    private String email;
    private String location;
    private String description;
    private String rating;
    private String price;
    private String ammenities;
    

    
}
