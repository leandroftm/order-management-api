package com.leandroftm.ordermanagement.order_management_api.service;

import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create.CreateUserRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update.UpdateUserRequest;
import com.leandroftm.ordermanagement.order_management_api.domain.dto.response.UserResponse;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.user.*;
import com.leandroftm.ordermanagement.order_management_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private final long id = 1L;

    //CREATE
    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserRequest request = new CreateUserRequest(
                "test@test.com",
                "123456",
                Role.USER
        );

        User savedUser = createUser();
        ReflectionTestUtils.setField(savedUser, "id", id);

        when(encoder.encode(request.password())).thenReturn("encoded-password");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        long createdId = userService.create(request);

        assertEquals(createdId, savedUser.getId());

        verify(userRepository).save(argThat(user ->
                user.getEmail().equals(request.email()) &&
                        user.getPassword().equals("encoded-password") &&
                        user.getRole().equals(Role.USER)));
        verifyNoMoreInteractions(userRepository);
    }

    //READ
    @Test
    void shouldReturnUsersByRoleSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User(
                "test@test.com",
                "123456",
                Role.USER
        ));

        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findByRoleIgnoreCase(any(Role.class), any(Pageable.class))).thenReturn(page);

        Page<UserResponse> response = userService.getUsers(Role.USER, pageable);

        assertNotNull(response);
        assertEquals("test@test.com", response.getContent().get(0).email());
        assertEquals("123456", response.getContent().get(0).password());
        assertEquals(Role.USER, response.getContent().get(0).role());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnAllUsersSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User(
                "test@test.com",
                "123456",
                Role.USER
        ));

        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserResponse> response = userService.getUsers(null, pageable);

        assertNotNull(response);
        assertEquals("test@test.com", response.getContent().get(0).email());
        assertEquals("123456", response.getContent().get(0).password());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnUserSuccessfully() {
        User savedUser = createUser();
        ReflectionTestUtils.setField(savedUser, "id", id);

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));

        userService.getUser(id);

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    //UPDATE
    @Test
    void shouldUpdateUserSuccessfully() {
        User savedUser = createUser();
        ReflectionTestUtils.setField(savedUser, "id", id);

        UpdateUserRequest request = new UpdateUserRequest(
                "new.test.email@email.com",
                "11223344"
        );

        when(encoder.encode(request.password())).thenReturn("encoded-password");

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.update(id, request);

        verify(userRepository).findById(id);
        verify(userRepository).existsByEmail(request.email());
        verify(userRepository).save(argThat(user ->
                user.getEmail().equals(request.email()) &&
                        user.getPassword().equals("encoded-password")));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldDisableUserSuccessfully() {
        User savedUser = createUser();
        ReflectionTestUtils.setField(savedUser, "id", id);

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));
        assertTrue(savedUser.isEnabled());

        userService.disable(id);
        assertFalse(savedUser.isEnabled());

        verify(userRepository).findById(id);
        verify(userRepository).save(argThat(user ->
                !user.isEnabled()));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldEnableUserSuccessfully() {
        User savedUser = createUser();
        ReflectionTestUtils.setField(savedUser, "id", id);
        savedUser.disable();

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));
        assertFalse(savedUser.isEnabled());

        userService.enable(id);
        assertTrue(savedUser.isEnabled());

        verify(userRepository).findById(id);
        verify(userRepository).save(argThat(User::isEnabled));
        verifyNoMoreInteractions(userRepository);
    }

    //# EXCEPTIONS

    @Test
    void shouldReturnBadRequestWhenUserAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest(
                "test@test.com",
                "123456",
                Role.USER
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnBadRequestWhenUserDoesNotExist() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(id));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnBadRequestWhenUserOldAndNewEmailsAreIdentical() {
        UpdateUserRequest request = new UpdateUserRequest(
                "test@test.com",
                "123456"
        );

        User savedUser = createUser();

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));

        assertThrows(InvalidEmailException.class, () -> userService.update(id, request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturnBadRequestWhenInactivatingUserAlreadyDisabled() {
        User savedUser = createUser();
        savedUser.disable();
        assertFalse(savedUser.isEnabled());

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));

        assertThrows(UserAlreadyInactiveException.class, () -> userService.disable(id));
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnBadRequestWhenInactivatingAdminUser() {
        User savedUser = new User(
                "test@test.com",
                "123456",
                Role.ADMIN
        );

        assertTrue(savedUser.isEnabled());

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));

        assertEquals(Role.ADMIN, savedUser.getRole());
        assertThrows(CannotDisableAdminException.class, () -> userService.disable(id));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnBadRequestWhenActivatingUserAlreadyEnabled() {
        User savedUser = createUser();
        assertTrue(savedUser.isEnabled());

        when(userRepository.findById(id)).thenReturn(Optional.of(savedUser));

        assertThrows(UserAlreadyActiveException.class, () -> userService.enable(id));
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    //helper
    private User createUser() {
        return new User(
                "test@test.com",
                "123456",
                Role.USER
        );
    }
}
