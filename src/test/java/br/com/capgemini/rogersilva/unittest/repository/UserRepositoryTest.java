package br.com.capgemini.rogersilva.unittest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.capgemini.rogersilva.unittest.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername() {
        String username = "administrator";

        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user).isPresent();
        assertThat(user.get()).isNotNull();
        assertThat(user.get().getUsername()).isEqualTo(username);
    }

    @Test
    public void findByUsernameNotFound() {
        Optional<User> user = userRepository.findByUsername("john-doe");

        assertThat(user).isNotPresent();
    }
}