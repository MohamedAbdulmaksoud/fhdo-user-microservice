package com.giassi.microservice.demo2.rest.users;

import com.giassi.microservice.demo2.rest.users.dtos.UserDTO;
import com.giassi.microservice.demo2.rest.users.dtos.requests.RegisterUserAccountDTO;
import com.giassi.microservice.demo2.rest.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterRestControllerTest{

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Test
    public void testCreateNewUserAccount() {
        // Create a new user using the quick account endpoint
        RegisterUserAccountDTO quickAccount = RegisterUserAccountDTO.builder()
                .username("violet")
                .password("Violet!123")
                .name("Marco")
                .surname("Violet")
                .gender("MALE")
                .email("marco.violet@gmail.com")
                .build();

        String userQuickAccountURL = "/users/register";

        HttpEntity<RegisterUserAccountDTO> request = new HttpEntity<>(quickAccount);
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(userQuickAccountURL, request, UserDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        UserDTO userDTO = response.getBody();

        assertNotNull(userDTO);
        assertEquals("violet", userDTO.getUsername());
        assertEquals("Marco", userDTO.getName());
        assertEquals("Violet", userDTO.getSurname());
        assertEquals("MALE", userDTO.getGender());

        assertNotNull(userDTO.getContactDTO());
        assertEquals("marco.violet@gmail.com", userDTO.getContactDTO().getEmail());

        // Delete the created user
        userService.deleteUserById(userDTO.getId());
    }

}
