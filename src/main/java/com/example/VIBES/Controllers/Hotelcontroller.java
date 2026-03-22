package com.example.VIBES.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.VIBES.DTO.Hrequest;
import com.example.VIBES.Model.Hotel;
import com.example.VIBES.Services.HotelService;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/hotels")

public class Hotelcontroller {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/hotellist")
    public String getAllHotels(Model model) {
        List<Hotel> hotels = hotelService.getallHotels();
        model.addAttribute("hotels", hotels);
        return "hotels";   
    }                                               

    @GetMapping("/Registerhotelform")
     @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("hrequest", new Hrequest());
        return "hotelregister";   
    }
    @PostMapping("/Registerhotel")
     @PreAuthorize("hasRole('ADMIN')")
    public String registerhotel(@ModelAttribute("hrequest") Hrequest hrequest , Model model) {
        hotelService.Createhotel(hrequest);
        return "redirect:/hotels/hotellist";
    }

    @GetMapping("/Updatehotel/{hotelname}")
     @PreAuthorize("hasRole('ADMIN')")
    public String showeditform(@PathVariable String hotelname, Model model) {
        Hotel hotel = hotelService.getHotelbyHotelname(hotelname);
        if (hotel == null) {
        model.addAttribute("message", "Hotel does not exist!");
        return "hotelregister";
    }
        model.addAttribute("hotel", hotel);
        return "updatehotel";
    }
    
    @PostMapping("/Updatehotel/{hotelname}")
     @PreAuthorize("hasRole('ADMIN')")
    public String updatehotel(@PathVariable String hotelname, Hrequest hrequest) {
        System.out.println("Updating hotel :" + hotelname);
        hotelService.Updatehotel(hotelname, hrequest);   
        return "redirect:/hotels/hotellist";
    }

    @GetMapping("/Deletehotel/{hotelname}")
     @PreAuthorize("hasRole('ADMIN')")
    public String Deletehotel(@PathVariable String hotelname) {
        hotelService.Deletehotel(hotelname);
        return "redirect:/hotels/hotellist";
    }
    
}
