package br.com.capgemini.rogersilva.unittest.repository;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.capgemini.rogersilva.unittest.model.Evaluation;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class EvaluationRepositoryTest {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Test
    public void findByIdEvaluatorId() {
        Long evaluatorId = 3L;

        Optional<List<Evaluation>> evaluations = evaluationRepository.findByIdEvaluatorId(evaluatorId);

        Assertions.assertThat(evaluations).isPresent();
        Assertions.assertThat(evaluations.get()).isNotEmpty();
        Assertions.assertThat(evaluations.get().get(0).getId().getEvaluator().getId()).isEqualTo(evaluatorId);
    }

    @Test
    public void findByIdEvaluatorIdNotFound() {
        Optional<List<Evaluation>> evaluations = evaluationRepository.findByIdEvaluatorId(0L);

        Assertions.assertThat(evaluations).isPresent();
        Assertions.assertThat(evaluations.get()).isEmpty();
    }
}