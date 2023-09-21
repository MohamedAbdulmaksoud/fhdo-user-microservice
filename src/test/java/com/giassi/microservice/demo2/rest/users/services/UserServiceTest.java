package com.giassi.microservice.demo2.rest.users.services;

import com.giassi.microservice.demo2.rest.users.dtos.UserDTO;
import com.giassi.microservice.demo2.rest.users.dtos.requests.CreateOrUpdateUserDTO;
import com.giassi.microservice.demo2.rest.users.dtos.requests.RegisterUserAccountDTO;
import com.giassi.microservice.demo2.rest.users.entities.Gender;
import com.giassi.microservice.demo2.rest.users.entities.Role;
import com.giassi.microservice.demo2.rest.users.entities.User;
import com.giassi.microservice.demo2.rest.users.exceptions.*;
import com.giassi.microservice.demo2.rest.users.repositories.RoleRepository;
import com.giassi.microservice.demo2.rest.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static com.giassi.microservice.demo2.rest.users.services.UserTestHelper.getUserTestData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService = new UserService();

    @BeforeEach
    public void setUp() {
        // using the default salt for test
        ReflectionTestUtils.setField(userService, "salt", EncryptionService.DEFAULT_SALT);
    }

    @Test
    public void given_existing_users_when_getUserPresentationList_return_validList() {
        User user1 = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");
        User user2 = getUserTestData(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"), "marco", "Marco",
                "Rossi", "marco.test@gmail.com", "+3531122334466");
        User user3 = getUserTestData(UUID.fromString("44fe4a75-edc8-4c9f-95ce-aaa3b34dce46"), "francesco", "Francesco",
                "Verdi", "francesco.test@gmail.com", "+3531122334477");

        List<User> list = Arrays.asList(user1, user2, user3);

        given(userService.getUserList()).willReturn(list);

        List<UserDTO> userDTOList = userService.getUserPresentationList();

        assertNotNull(userDTOList);
        assertEquals(3, userDTOList.size());

        // take the second element to test the DTO content
        UserDTO userDTO = userDTOList.get(1);

        assertEquals(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"), userDTO.getId());
        assertEquals("marco", userDTO.getUsername());
        assertEquals("Marco", userDTO.getName());
        assertEquals("Rossi", userDTO.getSurname());

        assertNotNull(userDTO.getContactDTO());
        assertEquals("marco.test@gmail.com", userDTO.getContactDTO().getEmail());
        assertEquals("+3531122334466", userDTO.getContactDTO().getPhone());
    }

    @Test
    public void given_existing_user_when_getUserById_returnUser() {
        UUID userId = UUID.randomUUID();

        User user = getUserTestData(userId, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        User userRet = userService.getUserById(userId);

        assertNotNull(userRet);
        assertEquals(userId, userRet.getId());
        assertEquals("andrea", userRet.getUsername());
        assertEquals("Andrea", userRet.getName());
        assertEquals("Giassi", userRet.getSurname());
        assertEquals("andrea.test@gmail.com", userRet.getContact().getEmail());
        assertEquals("+3531122334455", userRet.getContact().getPhone());
    }

    @Test
    public void givenNotExistingUserWhenGetUserByIdThrowException() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    public void givenNullUserIdWhenGetUserByIdThrowException() {
        assertThrows(InvalidUserIdentifierException.class, () -> userService.getUserById(null));
    }

    @Test
    public void givenNullUsernameWhenGetUserByUsernameReturnUser() {
        assertThrows(InvalidUsernameException.class, () -> userService.getUserByUsername(null));
    }

    @Test
    public void given_existing_username_when_getUserByUsername_return_user() {
        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByUsername("andrea")).willReturn(userDataForTest);

        User user = userService.getUserByUsername("andrea");

        assertNotNull(user);
        assertEquals(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), user.getId());
        assertEquals("andrea", user.getUsername());
        assertEquals("Andrea", user.getName());
        assertEquals("Giassi", user.getSurname());
        assertEquals("andrea.test@gmail.com", user.getContact().getEmail());
        assertEquals("+3531122334455", user.getContact().getPhone());
    }

    @Test
    public void given_existing_email_when_getUserByEmail_return_user() {
        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByEmail("andrea.test@gmail.com")).willReturn(userDataForTest);

        User user = userService.getUserByEmail("andrea.test@gmail.com");

        assertNotNull(user);
        assertEquals(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), user.getId());
        assertEquals("andrea", user.getUsername());
        assertEquals("Andrea", user.getName());
        assertEquals("Giassi", user.getSurname());
        assertEquals("andrea.test@gmail.com", user.getContact().getEmail());
        assertEquals("+3531122334455", user.getContact().getPhone());
    }

    @Test
    public void givenInvalidEmailGetUserByEmailThrowInvalidUserEmailException() {
        assertThrows(InvalidEmailException.class, () -> userService.getUserByEmail(null));
    }

    @Test
    public void givenNullCreateUserAccountDTOWhenCreateNewUserAccountThrowInvalidUserDataException() {
        assertThrows(InvalidUserDataException.class, () -> userService.registerUserAccount(null));
    }

    @Test
    public void givenAlreadyExistingUsernameWhenCreateNewUserAccountThrowInvalidUserDataException() {
        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByUsername("andrea")).willReturn(userDataForTest);

        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .name("Andrea")
                .surname("Giassi")
                .email("andrea.test@gmail.com")
                .gender("MALE")
                .username("andrea")
                .password(UserTestHelper.TEST_PASSWORD_DECRYPTED)
                .build();

        assertThrows(InvalidUserDataException.class, () -> userService.registerUserAccount(registerUserAccountDTO));
    }

    @Test
    public void givenExistingEmailWhenCreateNewUserAccountThrowInvalidUserDataException() {
        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByEmail("andrea.test@gmail.com")).willReturn(userDataForTest);

        // Existing email
        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .name("Marco")
                .password("Marco!123")
                .surname("Rossi")
                .email("andrea.test@gmail.com")
                .gender("MALE")
                .username("marco")
                .password(UserTestHelper.TEST_PASSWORD_DECRYPTED)
                .build();

        assertThrows(InvalidUserDataException.class, () -> userService.registerUserAccount(registerUserAccountDTO));
    }

    @Test
    public void givenInvalidRoleWhenSetUserRoleThrowRoleNotFoundException() {
        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        // Role doesn't exist
        assertThrows(RoleNotFoundException.class, () -> userService.addUserRole(userDataForTest, UUID.randomUUID()));
    }

    @Test
    public void given_valid_role_id_when_setUserRole_returnUser() {
        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(roleRepository.findById(Role.USER)).willReturn(Optional.of(new Role(Role.USER, "USER")));

        userService.addUserRole(userDataForTest, Role.USER);

        assertNotNull(userDataForTest);

        Role roleUser = new Role(Role.USER, "USER");
        assertTrue(userDataForTest.getRoles().contains(roleUser));

        assertEquals("andrea", userDataForTest.getUsername());
        assertEquals("Andrea", userDataForTest.getName());
        assertEquals("Giassi", userDataForTest.getSurname());
        assertTrue(userDataForTest.getEnabled());

        assertNotNull(userDataForTest.getContact());
        assertEquals("andrea.test@gmail.com", userDataForTest.getContact().getEmail());
        assertEquals("+3531122334455", userDataForTest.getContact().getPhone());
    }

    @Test
    public void givenInvalidCreateOrUpdateUserDTOWhenCreateUserThrowInvalidUserDataException() {
        assertThrows(InvalidUserDataException.class, () -> userService.createUser(null));
    }

    @Test
    public void givenAlreadyRegisteredUsernameWhenCreateUserThrowInvalidUserDataException() {
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder().username("andrea").build();

        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByUsername("andrea")).willReturn(userDataForTest);

        assertThrows(InvalidUserDataException.class, () -> userService.createUser(createOrUpdateUserDTO));
    }

    @Test
    public void givenAlreadyRegisteredEmailWhenCreateUserThrowInvalidUserDataException() {
        // existing email
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Marco")
                .surname("Rossi")
                .email("andrea.test@gmail.com")
                .gender("MALE")
                .username("marco")
                .phone("+3531122334466")
                .enabled(true).build();

        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByEmail("andrea.test@gmail.com")).willReturn(userDataForTest);

        assertThrows(InvalidUserDataException.class, () -> userService.createUser(createOrUpdateUserDTO));
    }

    @Test
    public void givenInvalidGenderStringWhenGetValidGenderThrowInvalidUserGenderException() {
        assertThrows(InvalidGenderException.class, () -> Gender.getValidGender("WRONG_GENDER"));
    }

    @Test
    public void given_valid_gender_strings_when_getValidGender_return_Gender() {
        // male
        Gender maleGender = Gender.getValidGender("MALE");

        assertNotNull(maleGender);
        assertEquals(1L, maleGender.getGender());

        // female
        Gender femaleGender = Gender.getValidGender("FEMALE");

        assertNotNull(femaleGender);
        assertEquals(2L, femaleGender.getGender());
    }

    @Test
    public void givenInvalidUserIdWhenUpdateUserThrowInvalidUserIdentifierException() {
        assertThrows(InvalidUserIdentifierException.class, () -> userService.updateUser(null, new CreateOrUpdateUserDTO()));
    }

    @Test
    public void givenInvalidCreateOrUpdateUserDTOWhenUpdateUserThrowInvalidUserDataException() {
        assertThrows(InvalidUserDataException.class, () -> userService.updateUser(UUID.randomUUID(), null));
    }

    @Test
    public void givenNotExistingUserIdWhenUpdateUserThrowUserNotFoundException() {
        UUID uuid = UUID.randomUUID();
        given(userRepository.findById(uuid)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(uuid, new CreateOrUpdateUserDTO()));
    }

    @Test
    public void givenExistingUsernameWhenUpdateUserThrowInvalidUserDataException() {
        // setting an existing username
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Marco")
                .surname("Rossi")
                .email("andrea.test@gmail.com")
                .gender("MALE")
                .username("andrea")
                .phone("+3531122334466")
                .enabled(true)
                .build();

        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");
        User userDataForTest2 = getUserTestData(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"), "andrea", "Marco",
                "Rossi", "marco.test@gmail.com", "+3531122334466");

        given(userRepository.findById(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"))).willReturn(Optional.of(userDataForTest2));
        given(userRepository.findByUsername("andrea")).willReturn(userDataForTest);

        assertThrows(InvalidUserDataException.class, () -> userService.updateUser(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"), createOrUpdateUserDTO));
    }

    @Test
    public void givenExistingEmailWhenUpdateUserThrowInvalidUserDataException() {
        // setting an existing email
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Marco")
                .surname("Rossi")
                .email("andrea.test@gmail.com")
                .gender("MALE")
                .username("marco")
                .password("Test!123")
                .phone("+3531122334466")
                .enabled(true)
                .build();

        User userDataForTest = getUserTestData(UUID.fromString("1af36f5b-19ae-40ff-a9ae-ed64c91d2204"), "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");
        User userDataForTest2 = getUserTestData(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"), "andrea", "Marco",
                "Rossi", "marco.test@gmail.com", "+3531122334466");

        given(userRepository.findById(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"))).willReturn(Optional.of(userDataForTest2));
        given(userRepository.findByEmail("andrea.test@gmail.com")).willReturn(userDataForTest);

        assertThrows(InvalidUserDataException.class, () -> userService.updateUser(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"), createOrUpdateUserDTO));
    }

    @Test
    public void given_existing_user_when_updatedUser_return_userUpdated() {
        UUID uuid = UUID.randomUUID();
        // correct user data, update the phone number
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Andrea")
                .surname("Giassi")
                .email("andrea.test@gmail.com")
                .gender("MALE")
                .username("andrea")
                .password("Test!123")
                .phone("+3539988776655")
                .enabled(true)
                .address("via roma 3").city("Rome").country("Italy").zipCode("00100")
                .build();

        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findById(uuid)).willReturn(Optional.of(userDataForTest));

        userService.updateUser(uuid, createOrUpdateUserDTO);
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void givenNullUserIdWhenDeleteUserByIdThrowInvalidUserIdentifierException() {
        assertThrows(InvalidUserIdentifierException.class, () -> userService.deleteUserById(null));
    }

    @Test
    public void givenNotExistingUserIdWhenDeleteUserByIdThrowUserNotFoundException() {
        UUID uuid = UUID.randomUUID();
        given(userRepository.findById(uuid)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(uuid));
    }

    @Test
    public void givenNullUsernameAndPasswordWhenLoginThrowInvalidLoginException() {
        assertThrows(InvalidLoginException.class, () -> userService.login(null, null));
    }

    @Test
    public void givenNullUsernameWhenLoginThrowInvalidLoginException() {
        assertThrows(InvalidLoginException.class, () -> userService.login(null, "WRONG_PWD"));
    }

    @Test
    public void givenNullPasswordWhenLoginThrowInvalidLoginException() {
        assertThrows(InvalidLoginException.class, () -> userService.login("WRONG", null));
    }

    @Test
    public void givenInvalidLoginWhenLoginThrowInvalidLoginException() {
        assertThrows(InvalidLoginException.class, () -> userService.login("WRONG", "WRONG_PWD"));
    }

    @Test
    public void given_valid_login_when_login_return_User() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByUsername("andrea")).willReturn(userDataForTest);

        User user = userService.login("andrea", UserTestHelper.TEST_PASSWORD_DECRYPTED);

        assertNotNull(user);
        assertEquals("andrea", user.getUsername());
    }


    @Test
    public void givenInvalidLogin2WhenLoginThrowInvalidLoginException() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findByUsername("andrea")).willReturn(userDataForTest);

        assertThrows(InvalidLoginException.class, () -> userService.login("andrea", "WRONG_PWD"));
    }

    @Test
    public void givenNotEnabledLoginWhenLoginThrowInvalidLoginException() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        userDataForTest.setEnabled(false);

        given(userRepository.findByUsername("andrea")).willReturn(userDataForTest);

        assertThrows(InvalidLoginException.class, () -> userService.login("andrea", UserTestHelper.TEST_PASSWORD_DECRYPTED));
    }

    // tests add role on User
    @Test
    public void givenNotExistingUserIdWhenAddRoleThrowUserNotFoundException() {
        UUID uuid = UUID.randomUUID();
        given(userRepository.findById(uuid)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.addRole(uuid, UUID.fromString("2dd89f24-4d96-47fc-8a62-c15e2228d8aa")));
    }

    @Test
    public void givenExistingUserIdNotExistingRoleIdWhenAddRoleThrowRoleNotFoundException() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findById(uuid)).willReturn(Optional.of(userDataForTest));

        assertThrows(RoleNotFoundException.class, () -> userService.addRole(uuid, uuid));
    }

    @Test
    public void given_validUserAndRoleIds_when_addRole_returnUser() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findById(uuid)).willReturn(Optional.of(userDataForTest));

        Role roleAdmin = new Role(Role.ADMINISTRATOR, "Administrator");

        given(roleRepository.findById(UUID.fromString("2dd89f24-4d96-47fc-8a62-c15e2228d8aa"))).willReturn(Optional.of(roleAdmin));

        User user = userService.addRole(uuid, UUID.fromString("2dd89f24-4d96-47fc-8a62-c15e2228d8aa"));

        assertNotNull(user);

        // check the new added role
        Set<Role> roleSet = user.getRoles();

        assertNotNull(roleSet);
        assertEquals(2, roleSet.size());
        assertTrue(roleSet.contains(roleAdmin));
    }

    // test remove role from User
    @Test
    public void givenNotExistingUserIdWhenRemoveRoleThrowUserNotFoundException() {
        UUID uuid = UUID.randomUUID();
        given(userRepository.findById(uuid)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.removeRole(uuid, UUID.fromString("2dd89f24-4d96-47fc-8a62-c15e2228d8aa")));
    }

    @Test
    public void givenExistingUserIdNotExistingRoleIdWhenRemoveRoleThrowRoleNotFoundException() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        given(userRepository.findById(uuid)).willReturn(Optional.of(userDataForTest));

        assertThrows(RoleNotFoundException.class, () -> userService.removeRole(uuid, uuid));
    }

    @Test
    public void givenValidSecuredUserWhenDeleteUserThrowUserIsSecuredException() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");
        // set a secure user
        userDataForTest.setSecured(true);

        given(userRepository.findById(uuid)).willReturn(Optional.of(userDataForTest));

        assertThrows(UserIsSecuredException.class, () -> userService.deleteUserById(uuid));
    }

    @Test
    public void given_validUserAndRoleIds_when_removeRole_returnUser() {
        UUID uuid = UUID.randomUUID();
        User userDataForTest = getUserTestData(uuid, "andrea", "Andrea",
                "Giassi", "andrea.test@gmail.com", "+3531122334455");

        Role roleAdmin = new Role(Role.ADMINISTRATOR, "Administrator");
        userDataForTest.getRoles().add(roleAdmin);

        given(userRepository.findById(uuid)).willReturn(Optional.of(userDataForTest));
        given(roleRepository.findById(UUID.fromString("2dd89f24-4d96-47fc-8a62-c15e2228d8aa"))).willReturn(Optional.of(roleAdmin));

        User user = userService.removeRole(uuid, UUID.fromString("2dd89f24-4d96-47fc-8a62-c15e2228d8aa"));

        assertNotNull(user);

        // check the remove role
        Set<Role> roleSet = user.getRoles();

        assertNotNull(roleSet);
        assertEquals(1, roleSet.size());
        assertFalse(roleSet.contains(roleAdmin));
    }

}
