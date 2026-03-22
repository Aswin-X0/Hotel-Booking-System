package com.example.VIBES.Controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.VIBES.Model.Booking;
import com.example.VIBES.Services.Bookingservice;
import com.example.VIBES.Services.Paymentservice;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/Bookings")
public class Paymentcontroller {

    @Autowired
    private Paymentservice paymentservice;
    
    @Autowired
    private Bookingservice bookingservice;

    @Value("${razorpay.keyId}")
    private String keyId;

    @GetMapping("/paymentform/{bookingId}")
    public String payPage(@PathVariable Long bookingId, Model model) throws Exception {

        Booking booking = bookingservice.getById(bookingId);

 
        double amountRupees = booking.getRooms().getPrice(); 
        int amountPaise = (int) Math.round(amountRupees * 100);

        var payment = paymentservice.createOrder(bookingId, amountPaise);

        model.addAttribute("booking", booking);
        model.addAttribute("razorpayKeyId", keyId);
        model.addAttribute("amountPaise", amountPaise);
        model.addAttribute("orderId", payment.getRazorpayOrderId());

        return "Payment";
    }

    @PostMapping("/payment/{bookingId}")
    public String paymentResult(
            @PathVariable Long bookingId,
            @RequestParam(required = false) String razorpay_order_id,
            @RequestParam(required = false) String razorpay_payment_id,
            @RequestParam(required = false) String razorpay_signature,
            Model model 
    ) throws Exception {

        boolean ok = false;
        if (razorpay_order_id != null && razorpay_payment_id != null && razorpay_signature != null) {
            ok = paymentservice.verifySignature(razorpay_order_id, razorpay_payment_id, razorpay_signature);
        }

        Booking booking = bookingservice.getById(bookingId);

        if (ok) {
            paymentservice.markSuccess(bookingId, razorpay_order_id, razorpay_payment_id);
            bookingservice.markConfirmed(bookingId);

            model.addAttribute("status", "SUCCESS");
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("paymentId", razorpay_payment_id);
        } else {
            paymentservice.markFailed(bookingId);
            model.addAttribute("status", "FAILED");
            model.addAttribute("bookingId", bookingId);
        }
        return "Paymentsuccess";
}   
}
