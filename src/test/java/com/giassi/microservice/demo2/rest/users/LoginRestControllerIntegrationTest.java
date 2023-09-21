package com.giassi.microservice.demo2.rest.users;

import com.giassi.microservice.demo2.rest.users.dtos.UserDTO;
import com.giassi.microservice.demo2.rest.users.dtos.requests.LoginRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginRestControllerIntegrationTest{

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testValidLogin() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("Andrea", "Test!123");

        ResponseEntity<UserDTO> response = restTemplate.postForEntity("/login", loginRequest, UserDTO.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> {
                    UserDTO userDTO = response.getBody();
                    assertNotNull(userDTO);
                    assertNotNull(userDTO.getId());
                    assertEquals("Andrea", userDTO.getName());
                    assertEquals("Giassi", userDTO.getSurname());
                    assertNotNull(userDTO.getContactDTO());
                    assertEquals("andrea.test@gmail.com", userDTO.getContactDTO().getEmail());
                    assertTrue(userDTO.isEnabled());
                }
        );
    }

    @Test
    public void testInvalidLogin() {
        // Use a formal valid password but not correct for the given account
        LoginRequestDTO loginRequest = new LoginRequestDTO("Andrea", "Test!123456");

        ResponseEntity<UserDTO> response = restTemplate.postForEntity("/login", loginRequest, UserDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
