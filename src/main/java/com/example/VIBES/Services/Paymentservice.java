package com.example.VIBES.Services;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.VIBES.Model.Payment;
import com.example.VIBES.Model.Paymentstatus;
import com.example.VIBES.Repository.PaymentRepo;
import com.razorpay.RazorpayClient;
import com.razorpay.Order;


@Service
public class Paymentservice {

    @Autowired
    private RazorpayClient razorpayClient;
    @Autowired
    private PaymentRepo paymentRepo;
    // @Autowired
    // private BookingRepo bookingRepo;

    @Value("${razorpay.keySecret}")
    private String keysecret;

    @Value("${razorpay.currency:INR}")
    private String currency;

    public Payment createOrder(Long bookingId, int amount) throws Exception {
        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", currency);
        options.put("receipt", "booking_" + bookingId);

        Order order = razorpayClient.orders.create(options);

        Payment payment = paymentRepo.findByBookingId(bookingId).orElse(new Payment());
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentstatus(Paymentstatus.CREATED);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setRazorpayPaymentId(null);
        return paymentRepo.save(payment);
    }

     public boolean verifySignature(String orderId, String paymentId, String signature) throws Exception {

        String payload = orderId + "|" + paymentId;

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(keysecret.getBytes(StandardCharsets.UTF_8),"HmacSHA256" ));

        byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String expectedSignature = HexFormat.of().formatHex(digest);

        return expectedSignature.equals(signature);
    }

    public Payment markSuccess(Long bookingId, String orderId, String paymentId) {

        Payment payment = paymentRepo.findByBookingId(bookingId).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentstatus(Paymentstatus.SUCCESS);
        payment.setRazorpayOrderId(orderId);
        payment.setRazorpayPaymentId(paymentId);
        return paymentRepo.save(payment);
    }

    public Payment markFailed(Long bookingId) {

        Payment payment = paymentRepo.findByBookingId(bookingId).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentstatus(Paymentstatus.FAILED);
        return paymentRepo.save(payment);
    }
    
}
