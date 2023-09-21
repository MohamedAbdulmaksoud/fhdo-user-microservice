package com.giassi.microservice.demo2.rest.users.dtos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserListDTOTest {

    @Test
    public void userListDTOTest() {
        UserListDTO userListDTO = new UserListDTO();

        Assertions.assertNotNull(userListDTO.getUserList().size());
        Assertions.assertEquals(0, userListDTO.getUserList().size());
    }

}
