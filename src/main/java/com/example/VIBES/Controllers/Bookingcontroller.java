package com.example.VIBES.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.VIBES.DTO.Brequest;
import com.example.VIBES.Model.Booking;
import com.example.VIBES.Model.Rooms;
import com.example.VIBES.Repository.RoomRepo;
import com.example.VIBES.Services.Bookingservice;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/hotels")
public class Bookingcontroller {

    @Autowired
    private Bookingservice bookingservice;

    @Autowired
    private RoomRepo roomRepo;

    @GetMapping("/bookinglist")
     @PreAuthorize("hasRole('ADMIN')")
    public String getallbookings(Model model) {
        List<Booking> bookings = bookingservice.getallbookings();
        model.addAttribute("bookings", bookings);
        return "bookinglist";
    }

    @GetMapping("/{hotelname}/book/{roomId}")
    public String showBookingForm(@PathVariable("hotelname") String hotelname, @PathVariable("roomId") Long roomId,
            @RequestParam(required = false) String username, Model model) {

        Rooms room = roomRepo.findById(roomId).orElse(null);
        if (room == null) {
            model.addAttribute("error", "Room not found");
            return "booking-form";
        }

        Brequest brequest = new Brequest();
        brequest.setUsername(username);
        model.addAttribute("room", room);
        model.addAttribute("roomId", roomId);
        model.addAttribute("brequest", brequest);
        model.addAttribute("today", LocalDate.now());

        return "Bookingform";
    }

    @PostMapping("/{hotelname}/book/{roomId}")
    public String createBooking(@PathVariable("hotelname") String hotelname, @PathVariable("roomId") Long roomId,
            @ModelAttribute("brequest") Brequest brequest, RedirectAttributes ra, Model model) {

        try {
            LocalDate checkIn = LocalDate.parse(brequest.getCheckIn());
            LocalDate checkOut = LocalDate.parse(brequest.getCheckOut());

            Booking booking = bookingservice.createBooking(
                    brequest.getUsername(),
                    roomId,
                    checkIn,
                    checkOut);

            model.addAttribute("booking", booking);
            model.addAttribute("hotelname", hotelname);
            return "Bookingcompleted";

        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/hotels/" + hotelname + "/book/" + roomId + "?username=" + brequest.getUsername();
        }
    }

    @GetMapping("/bookings")
     @PreAuthorize("hasRole('ADMIN')")
    public String bookingHistory(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null || username.isBlank()) {
            model.addAttribute("error", "Username required");
            return "Bookinghistory";
        }
        List<Booking> bookings = bookingservice.getBookingsByUser(username);
        model.addAttribute("bookings", bookings);
        model.addAttribute("username", username);
        return "Bookinghistory";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, @RequestParam String username, RedirectAttributes ra) {

        try {
            bookingservice.cancelBooking(id);
            ra.addFlashAttribute("msg", "Booking cancelled successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/hotels/bookings?username=" + username;
    }

}
