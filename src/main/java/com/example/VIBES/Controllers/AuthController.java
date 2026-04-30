package com.example.VIBES.Controllers;



import com.example.VIBES.DTO.Urequest;
import com.example.VIBES.Model.User;
import com.example.VIBES.Services.Authservice;
import com.example.VIBES.Services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/vibes")
public class AuthController {

   @Autowired
   private Authservice authService;

   @Autowired
   private UserService userService;

   @ModelAttribute("urequest")
    public Urequest urequest() {
        return new Urequest();
    }

   @GetMapping("/signupform")
    public String Signup(){
        return "Register";
    }

   @PostMapping("/signup")
    public String signUp(@ModelAttribute("urequest") Urequest urequest, Model model) {
    String result = authService.SignUp(urequest);

        if (!result.equals("User registered successfully")) {
            model.addAttribute("error", result);
            return "Register";
        }

        return "redirect:/vibes/signinform";
    }

   @GetMapping("/signinform")   
    public String loginPage() {
        return "Login";
    }

    @PostMapping("/signin")
public String signIn(@RequestParam String username,
                     @RequestParam String password,
                     HttpServletResponse response,
                     HttpSession session,
                     Model model) {

    try {
        String jwt = authService.SignIn(username, password);

        User user = userService.getUserbyUsername(username);

        // optional (for UI)
        session.setAttribute("user", user);

        // ✅ STORE JWT IN COOKIE
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return "redirect:/vibes/home";

    } catch (Exception e) {
        model.addAttribute("error", "Invalid username or password");
        return "Login";
    }
}
}