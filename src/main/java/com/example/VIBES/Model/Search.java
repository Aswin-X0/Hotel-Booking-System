package com.example.VIBES.Model;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Search {
    private Hotel hotel;
    private List<Rooms> availableRooms;

}