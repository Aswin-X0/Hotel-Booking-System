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

import com.example.VIBES.DTO.Rrequest;
import com.example.VIBES.Model.Rooms;
import com.example.VIBES.Services.Roomservice;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/hotels/{hotelname}/rooms")

public class Roomcontroller {

    @Autowired
    private Roomservice roomservice;

    @GetMapping("/roomlist")
     @PreAuthorize("hasRole('ADMIN')")
    public String listRoomsByHotel(@PathVariable String hotelname, Model model) {
        List<Rooms> rooms = roomservice.getRoomsByHotel(hotelname);
        model.addAttribute("hotelname", hotelname);
        model.addAttribute("rooms", rooms);
        return "rooomlist";   
    }

    @GetMapping("/Newroom")
     @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(@PathVariable String hotelname, Model model) {
        model.addAttribute("hotelname", hotelname);
        model.addAttribute("Rrequest",new Rrequest());
        return "roomcreate";
    }       

    @PostMapping("/Newroom")
     @PreAuthorize("hasRole('ADMIN')")
    public String createform(@PathVariable String hotelname,@ModelAttribute Rrequest Rrequest) {
        System.out.println("Creating room for hotel: " + hotelname);
        roomservice.createRoom(hotelname, Rrequest);
        return "redirect:/hotels/" + hotelname + "/rooms/roomlist";
    }

    @GetMapping("/Editroom")
     @PreAuthorize("hasRole('ADMIN')")
    public String showEditform(@PathVariable String hotelname,Model model) {
        roomservice.updateRoomByHotel(hotelname, new Rrequest());
        model.addAttribute("hotelname", hotelname);
         model.addAttribute("Rrequest", new Rrequest());
        return "roomupdate";        
    }
    @PostMapping("/Editroom")
     @PreAuthorize("hasRole('ADMIN')")
    public String updateRooms(@PathVariable String hotelname, @ModelAttribute Rrequest request) {
        roomservice.updateRoomsByHotel(hotelname, request);
        return "redirect:/hotels/" + hotelname + "/rooms/roomlist";
    }

    @GetMapping("/delete/{roomId}")
     @PreAuthorize("hasRole('ADMIN')")
    public String deleteRoom(@PathVariable String hotelname,@PathVariable Long roomId) {
        roomservice.deleteRoom(roomId);
        return "redirect:/hotels/" + hotelname + "/rooms/roomlist";
}



        
}
