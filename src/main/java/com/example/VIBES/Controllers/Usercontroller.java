package com.example.VIBES.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;

import com.example.VIBES.DTO.Urequest;
import com.example.VIBES.Model.User;
import com.example.VIBES.Services.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;






@Controller
@RequestMapping("/vibes")
public class Usercontroller {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String HOMEPAGE(HttpSession session , Model model) {
    User user = (User) session.getAttribute("user");
    model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/Adminpanel")
    @PreAuthorize("hasRole('ADMIN')")
    public String Adminpage() {
        return "Adminpanel";
    }
    
    @GetMapping("/userlist")
    @PreAuthorize("hasRole('ADMIN')")
     public String Userlist(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "Userlist";
}
    
    @GetMapping("/useredit/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String useredit(@PathVariable String username, Model model) {
        User user = userService.getUserbyUsername(username);
    if (user == null) {
        return "redirect:/vibes/home";
    }
    model.addAttribute("user", user);
    return "usereditform";
}

    @PostMapping("/useredit/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userupdate(@PathVariable String username,@ModelAttribute("user") Urequest urequest) {
    System.out.println("Updating user: " + username);
    userService.Updateuser(username,urequest );
    return "redirect:/vibes/userlist";
}

   @GetMapping("/deleteuser/{id}")
   @PreAuthorize("hasRole('ADMIN')")
    public String userdeletion(@PathVariable Long id) {
       userService.Deleteuser(id);
       return "redirect:/vibes/home";
   }    
}
