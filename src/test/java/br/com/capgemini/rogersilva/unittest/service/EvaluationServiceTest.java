package br.com.capgemini.rogersilva.unittest.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.capgemini.rogersilva.unittest.dto.EvaluationDto;
import br.com.capgemini.rogersilva.unittest.exception.BadRequestException;
import br.com.capgemini.rogersilva.unittest.exception.NotFoundException;
import br.com.capgemini.rogersilva.unittest.model.Evaluation;
import br.com.capgemini.rogersilva.unittest.model.EvaluationId;
import br.com.capgemini.rogersilva.unittest.model.Process;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.repository.EvaluationRepository;
import br.com.capgemini.rogersilva.unittest.repository.ProcessRepository;

@SpringBootTest
public class EvaluationServiceTest {

    private static final String FEEDBACK = "Comentários";

    @InjectMocks
    private EvaluationService evaluationService;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private ProcessRepository processRepository;

    private Process mockProcess;

    private User mockUser;

    private EvaluationId mockEvaluationId;

    private Evaluation mockEvaluation;

    private EvaluationDto mockEvaluationDto;

    @BeforeEach
    public void setup() {
        mockProcess = Process.builder().id(1L).build();

        mockUser = User.builder().id(1L).build();

        mockEvaluationId = EvaluationId.builder().process(mockProcess).evaluator(mockUser).build();

        mockEvaluation = Evaluation.builder().id(mockEvaluationId).build();

        mockEvaluationDto = EvaluationDto.builder().feedback(FEEDBACK).build();
    }

    @Test
    public void createEvaluation() throws BadRequestException, NotFoundException {
        Mockito.when(processRepository.findById(mockProcess.getId())).thenReturn(Optional.of(mockProcess));

        Mockito.when(evaluationRepository.findById(mockEvaluationId)).thenReturn(Optional.of(mockEvaluation));

        Mockito.when(evaluationRepository.save(ArgumentMatchers.any(Evaluation.class))).thenReturn(mockEvaluation);

        EvaluationDto evaluation = evaluationService.createEvaluation(
                EvaluationDto.builder().processId(mockProcess.getId()).feedback(FEEDBACK).build(), mockUser);

        Assertions.assertThat(evaluation).isEqualTo(mockEvaluationDto);
    }

    @Test
    public void createEvaluationProcessNotFound() {
        Mockito.when(processRepository.findById(mockProcess.getId())).thenReturn(Optional.empty());

        try {
            evaluationService.createEvaluation(
                    EvaluationDto.builder().processId(mockProcess.getId()).feedback(FEEDBACK).build(), mockUser);
            Assertions.fail("BadRequestException should have been generated");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(BadRequestException.class);
            Assertions.assertThat(e.getMessage())
                    .isEqualTo(String.format("Process with id %s not found", mockProcess.getId()));
        }
    }

    @Test
    public void createEvaluationEvaluationNotFound() {
        Mockito.when(processRepository.findById(mockProcess.getId())).thenReturn(Optional.of(mockProcess));

        Mockito.when(evaluationRepository.findById(mockEvaluationId)).thenReturn(Optional.empty());

        try {
            evaluationService.createEvaluation(
                    EvaluationDto.builder().processId(mockProcess.getId()).feedback(FEEDBACK).build(), mockUser);
            Assertions.fail("NotFoundException should have been generated");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(NotFoundException.class);
            Assertions.assertThat(e.getMessage())
                    .isEqualTo(String.format("Evaluation for user with id %s and process with id %s not found",
                            mockUser.getId(), mockProcess.getId()));
        }
    }
}