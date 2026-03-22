package com.example.VIBES.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.VIBES.Model.Search;
import com.example.VIBES.Services.Searchservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("search")

public class Searchcontroller {

    @Autowired
    private Searchservice searchservice;

@GetMapping("/hotels")
public String Hotelsearch(Model model) {
    model.addAttribute("today" , LocalDate.now());
    return "search";
}
@GetMapping("/results")
public String searchResults(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            Model model
    ) {

        model.addAttribute("today", LocalDate.now());
        model.addAttribute("location", location);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);

        if (location == null || location.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a location.");
            return "home";
        }
        if (checkIn == null || checkOut == null) {
            model.addAttribute("error", "Please select both check-in and check-out dates.");
            return "home";
        }
        if (!checkIn.isBefore(checkOut)) {
            model.addAttribute("error", "Check-out date must be after check-in date.");
            return "home";
        }

        List<Search> results = searchservice.searchAvailableHotels(location, checkIn, checkOut);

        if (results == null || results.isEmpty()) {
            model.addAttribute("message", "No hotels available in " + location + " for the selected dates.");
        }

        model.addAttribute("featuredHotels", results);
        return "home";
    }

}

