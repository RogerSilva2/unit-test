package br.com.capgemini.rogersilva.unittest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.capgemini.rogersilva.unittest.model.Process;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProcessRepositoryTest {

    @Autowired
    private ProcessRepository processRepository;

    @Test
    public void findById() {
        Long processId = 1L;

        Optional<Process> process = processRepository.findById(processId);

        assertThat(process).isPresent();
        assertThat(process.get()).isNotNull();
        assertThat(process.get().getId()).isEqualTo(processId);
    }

    @Test
    public void findByIdNotFound() {
        Optional<Process> process = processRepository.findById(0L);

        assertThat(process).isNotPresent();
    }
}