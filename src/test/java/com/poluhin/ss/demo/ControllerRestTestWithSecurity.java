package com.poluhin.ss.demo;

import com.poluhin.ss.demo.domain.security.Role;
import com.poluhin.ss.demo.domain.security.User;
import com.poluhin.ss.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ControllerRestTestWithSecurity {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserRepository userRepository;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        User adminUser = new User(null, "admin", "admin", Role.ADMIN);
        User userUser = new User(null, "user", "user", Role.USER);
        when(userRepository.findByUsername("admin")).thenReturn(adminUser);
        when(userRepository.findByUsername("user")).thenReturn(userUser);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    public void givenRequestOnPrivateService_shouldUnauthorized() throws Exception {
        mvc.perform(get("/resource/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSuccessfulAuthenticationWithUserRole() throws Exception {
        mvc.perform(get("/resource/all")
                        .with(userHttpBasic(new User(1L, "user", "user", Role.USER))))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuccessfulAuthenticationWithAdminRole() throws Exception {
        mvc.perform(get("/resource/all")
                        .with(userHttpBasic(new User(2L, "admin","admin", Role.ADMIN))))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnsuccessfulAuthenticationWithWrongCredentials() throws Exception {
        mvc.perform(get("/resource/all")
                        .with(userHttpBasic(new User(1L, "admin", "wrongpassword", Role.ADMIN))))
                .andExpect(status().isUnauthorized());
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors
                .httpBasic(user.getUsername(), user.getPassword());
    }


}
