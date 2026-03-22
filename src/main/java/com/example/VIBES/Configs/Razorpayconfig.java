package com.example.VIBES.Configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.razorpay.RazorpayClient;

@Configuration
public class Razorpayconfig {

    @Bean
    public RazorpayClient razorpayClient(@Value("${razorpay.keyId}") String keyId,@Value("${razorpay.keySecret}") String keySecret
    ) 
    throws Exception {
        return new RazorpayClient(keyId, keySecret);
    }
}