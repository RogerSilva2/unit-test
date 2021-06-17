package br.com.capgemini.rogersilva.unittest.service;

import static br.com.capgemini.rogersilva.unittest.model.Role.ADMINISTRATOR;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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
        mockUser = User.builder().id(1L).username("roger").role(ADMINISTRATOR).build();

        mockUserDto = UserDto.builder().id(mockUser.getId()).username(mockUser.getUsername()).role(mockUser.getRole())
                .build();
    }

    @Test
    public void loadUserByUsername() {
        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));

        UserDetails user = userService.loadUserByUsername(mockUser.getUsername());

        Assertions.assertThat(user).isInstanceOf(User.class);
        Assertions.assertThat((User) user).isEqualTo(mockUser);
    }

    @Test
    public void loadUserByUsernameNotFound() {
        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.empty());

        try {
            userService.loadUserByUsername(mockUser.getUsername());
            Assertions.fail("UsernameNotFoundException should have been generated");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(UsernameNotFoundException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo(String.format("User %s not found", mockUser.getUsername()));
        }
    }

    @Test
    public void findUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<UserDto> users = userService.findUsers();

        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.get(0)).isEqualTo(mockUserDto);
    }

    @Test
    public void findUsersEmpty() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of());

        List<UserDto> users = userService.findUsers();

        Assertions.assertThat(users).isEmpty();
    }

    @Test
    public void createUser() {
        String password = "123456";

        Mockito.when(encoder.encode(password)).thenReturn(password);

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(mockUser);

        UserDto user = userService.createUser(
                UserDto.builder().username(mockUser.getUsername()).password(password).role(mockUser.getRole()).build());

        Assertions.assertThat(user).isEqualTo(mockUserDto);
    }

    @Test
    public void updateUser() throws NotFoundException {
        String password = "123456";

        Mockito.when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        Mockito.when(encoder.encode(password)).thenReturn(password);

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(mockUser);

        UserDto user = userService.updateUser(mockUser.getId(),
                UserDto.builder().username(mockUser.getUsername()).password(password).role(mockUser.getRole()).build());

        Assertions.assertThat(user).isEqualTo(mockUserDto);
    }

    @Test
    public void deleteUser() throws NotFoundException {
        Mockito.when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        userService.deleteUser(mockUser.getId());

        Mockito.verify(userRepository, Mockito.times(1)).delete(mockUser);
    }

    @Test
    public void findById() throws NotFoundException {
        Mockito.when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));

        User user = userService.findById(mockUser.getId());

        Assertions.assertThat(user).isEqualTo(mockUser);
    }

    @Test
    public void findByIdNotFound() {
        Mockito.when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());

        try {
            userService.findById(mockUser.getId());
            Assertions.fail("NotFoundException should have been generated");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(NotFoundException.class);
            Assertions.assertThat(e.getMessage())
                    .isEqualTo(String.format("User with id %s not found", mockUser.getId()));
        }
    }
}