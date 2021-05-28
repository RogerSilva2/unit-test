package br.com.capgemini.rogersilva.unittest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.capgemini.rogersilva.unittest.model.Evaluation;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EvaluationRepositoryTest {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Test
    public void findByIdEvaluatorId() {
        Long evaluatorId = 3L;

        Optional<List<Evaluation>> evaluations = evaluationRepository.findByIdEvaluatorId(evaluatorId);

        assertThat(evaluations).isPresent();
        assertThat(evaluations.get()).isNotEmpty();
        assertThat(evaluations.get().get(0).getId().getEvaluator().getId()).isEqualTo(evaluatorId);
    }

    @Test
    public void findByIdEvaluatorIdNotFound() {
        Optional<List<Evaluation>> evaluations = evaluationRepository.findByIdEvaluatorId(0L);

        assertThat(evaluations).isPresent();
        assertThat(evaluations.get()).isEmpty();
    }
}