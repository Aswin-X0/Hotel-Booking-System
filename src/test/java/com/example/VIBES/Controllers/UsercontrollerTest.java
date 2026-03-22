package com.example.VIBES.Controllers;

import com.example.VIBES.Configs.JwtUtil;
import com.example.VIBES.DTO.Urequest;
import com.example.VIBES.Model.User;
import com.example.VIBES.Services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = Usercontroller.class,
    excludeAutoConfiguration = {
        org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration.class,
        org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration.class,
        org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration.class
    }
)
@AutoConfigureMockMvc(addFilters = false)
public class UsercontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;


    @Test
    void testHomePage() throws Exception {
        User user = new User();
        user.setUsername("john");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/vibes/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    void testAdminPage() throws Exception {
        mockMvc.perform(get("/vibes/Adminpanel"))
                .andExpect(status().isOk())
                .andExpect(view().name("Adminpanel"));
    }

    @Test
    void testUserList() throws Exception {
        User user1 = new User();
        user1.setUsername("john");

        User user2 = new User();
        user2.setUsername("alice");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/vibes/userlist"))
                .andExpect(status().isOk())
                .andExpect(view().name("Userlist"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void testUserEditWhenUserExists() throws Exception {
        User user = new User();
        user.setUsername("john");

        when(userService.getUserbyUsername("john")).thenReturn(user);

        mockMvc.perform(get("/vibes/useredit/john"))
                .andExpect(status().isOk())
                .andExpect(view().name("usereditform"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    void testUserEditWhenUserNotFound() throws Exception {
        when(userService.getUserbyUsername("unknown")).thenReturn(null);

        mockMvc.perform(get("/vibes/useredit/unknown"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vibes/home"));
    }

    @Test
    void testUserUpdate() throws Exception {
        mockMvc.perform(post("/vibes/useredit/john")
                        .param("username", "john")
                        .param("password", "12345")
                        .param("role", "USER")
                        .param("email", "john@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vibes/userlist"));

        verify(userService).Updateuser(eq("john"), any(Urequest.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(get("/vibes/deleteuser/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vibes/home"));

        verify(userService).Deleteuser(1L);
    }
}