package br.com.capgemini.rogersilva.unittest.repository;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.capgemini.rogersilva.unittest.model.Process;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ProcessRepositoryTest {

    @Autowired
    private ProcessRepository processRepository;

    @Test
    public void findById() {
        Long processId = 1L;

        Optional<Process> process = processRepository.findById(processId);

        Assertions.assertThat(process).isPresent();
        Assertions.assertThat(process.get()).isNotNull();
        Assertions.assertThat(process.get().getId()).isEqualTo(processId);
    }

    @Test
    public void findByIdNotFound() {
        Optional<Process> process = processRepository.findById(0L);

        Assertions.assertThat(process).isNotPresent();
    }
}