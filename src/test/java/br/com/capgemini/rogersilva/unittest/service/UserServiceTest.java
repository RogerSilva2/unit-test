package br.com.capgemini.rogersilva.unittest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.capgemini.rogersilva.unittest.dto.UserDto;
import br.com.capgemini.rogersilva.unittest.exception.NotFoundException;
import br.com.capgemini.rogersilva.unittest.model.Role;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User mockUser;

    private UserDto mockUserDto;

    @BeforeEach
    public void setup() {
        mockUser = User.builder().id(1L).username("roger").role(Role.ADMINISTRATOR).build();

        mockUserDto = UserDto.builder().id(mockUser.getId()).username(mockUser.getUsername()).role(mockUser.getRole())
                .build();
    }

    @Test
    public void loadUserByUsername() {
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));

        UserDetails user = userService.loadUserByUsername(mockUser.getUsername());

        assertThat(user).isInstanceOf(User.class);
        assertThat((User) user).isEqualTo(mockUser);
    }

    @Test
    public void loadUserByUsernameNotFound() {
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.empty());

        try {
            userService.loadUserByUsername(mockUser.getUsername());
            fail("UsernameNotFoundException should have been generated");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UsernameNotFoundException.class);
            assertThat(e.getMessage()).isEqualTo(String.format("User %s not found", mockUser.getUsername()));
        }
    }

    @Test
    public void findUsers() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<UserDto> users = userService.findUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.get(0)).isEqualTo(mockUserDto);
    }

    @Test
    public void findUsersEmpty() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDto> users = userService.findUsers();

        assertThat(users).isEmpty();
    }

    @Test
    public void createUser() {
        String password = "123456";

        when(encoder.encode(password)).thenReturn(password);

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(mockUser);

        UserDto user = userService.createUser(
                UserDto.builder().username(mockUser.getUsername()).password(password).role(mockUser.getRole()).build());

        assertThat(user).isEqualTo(mockUserDto);
    }

    @Test
    public void updateUser() throws NotFoundException {
        String password = "123456";

        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        when(encoder.encode(password)).thenReturn(password);

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(mockUser);

        UserDto user = userService.updateUser(mockUser.getId(),
                UserDto.builder().username(mockUser.getUsername()).password(password).role(mockUser.getRole()).build());

        assertThat(user).isEqualTo(mockUserDto);
    }

    @Test
    public void deleteUser() throws NotFoundException {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        userService.deleteUser(mockUser.getId());

        verify(userRepository, Mockito.times(1)).delete(mockUser);
    }

    @Test
    public void findById() throws NotFoundException {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        User user = userService.findById(mockUser.getId());

        assertThat(user).isEqualTo(mockUser);
    }

    @Test
    public void findByIdNotFound() {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());

        try {
            userService.findById(mockUser.getId());
            fail("NotFoundException should have been generated");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
            assertThat(e.getMessage()).isEqualTo(String.format("User with id %s not found", mockUser.getId()));
        }
    }
}