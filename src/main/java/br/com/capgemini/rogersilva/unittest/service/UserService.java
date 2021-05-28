package br.com.capgemini.rogersilva.unittest.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.capgemini.rogersilva.unittest.dto.UserDto;
import br.com.capgemini.rogersilva.unittest.exception.NotFoundException;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    public List<UserDto> findUsers() {
        return userRepository.findAll().stream().map(this::convertToUserDto).collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        LocalDateTime now = LocalDateTime.now();

        User user = convertToUser(userDto);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return convertToUserDto(userRepository.save(user));
    }

    public UserDto updateUser(Long userId, UserDto userDto) throws NotFoundException {
        User user = findById(userId);

        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setUpdatedAt(LocalDateTime.now());

        return convertToUserDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) throws NotFoundException {
        User user = findById(userId);

        userRepository.delete(user);
    }

    public User findById(Long userId) throws NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
    }

    private User convertToUser(UserDto userDto) {
        return User.builder().username(userDto.getUsername())
                .password(new BCryptPasswordEncoder().encode(userDto.getPassword())).role(userDto.getRole()).build();
    }

    private UserDto convertToUserDto(User user) {
        return UserDto.builder().id(user.getId()).username(user.getUsername()).role(user.getRole()).build();
    }
}