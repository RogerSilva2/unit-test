package br.com.capgemini.rogersilva.unittest.service;

import static br.com.capgemini.rogersilva.unittest.model.Role.ADMINISTRATOR;
import static br.com.capgemini.rogersilva.unittest.model.Role.EVALUATOR;
import static br.com.capgemini.rogersilva.unittest.model.Role.GRADER;

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

import br.com.capgemini.rogersilva.unittest.dto.ProcessDto;
import br.com.capgemini.rogersilva.unittest.exception.BadRequestException;
import br.com.capgemini.rogersilva.unittest.model.Evaluation;
import br.com.capgemini.rogersilva.unittest.model.EvaluationId;
import br.com.capgemini.rogersilva.unittest.model.Process;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.repository.EvaluationRepository;
import br.com.capgemini.rogersilva.unittest.repository.ProcessRepository;
import br.com.capgemini.rogersilva.unittest.repository.UserRepository;

@SpringBootTest
public class ProcessServiceTest {

    @InjectMocks
    private ProcessService processService;

    @Mock
    private ProcessRepository processRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EvaluationRepository evaluationRepository;

    private Process mockProcess;

    private ProcessDto mockProcessDto;

    @BeforeEach
    public void setup() {
        mockProcess = Process.builder().id(1L).name("Proceso").content("Conteúdo").build();

        mockProcessDto = ProcessDto.builder().id(mockProcess.getId()).name(mockProcess.getName())
                .content(mockProcess.getContent()).build();
    }

    @Test
    public void findProcessesWithRoleAdministrator() {
        List<ProcessDto> processes = processService.findProcesses(User.builder().role(ADMINISTRATOR).build());

        Assertions.assertThat(processes).isEmpty();
    }

    @Test
    public void findProcessesWithRoleGrader() {
        Mockito.when(processRepository.findAll()).thenReturn(List.of(mockProcess));

        List<ProcessDto> processes = processService.findProcesses(User.builder().role(GRADER).build());

        Assertions.assertThat(processes).isNotEmpty();
        Assertions.assertThat(processes.get(0)).isEqualTo(mockProcessDto);
    }

    @Test
    public void findProcessesEmptyWithRoleGrader() {
        Mockito.when(processRepository.findAll()).thenReturn(List.of());

        List<ProcessDto> processes = processService.findProcesses(User.builder().role(GRADER).build());

        Assertions.assertThat(processes).isEmpty();
    }

    @Test
    public void findProcessesWithRoleEvaluator() {
        Long evaluatorId = 1L;

        Mockito.when(evaluationRepository.findByIdEvaluatorId(evaluatorId)).thenReturn(Optional
                .of(List.of(Evaluation.builder().id(EvaluationId.builder().process(mockProcess).build()).build())));

        List<ProcessDto> processes = processService
                .findProcesses(User.builder().id(evaluatorId).role(EVALUATOR).build());

        Assertions.assertThat(processes).isNotEmpty();
        Assertions.assertThat(processes.get(0)).isEqualTo(mockProcessDto);
    }

    @Test
    public void findProcessesEmptyWithRoleEvaluator() {
        Long evaluatorId = 1L;

        Mockito.when(evaluationRepository.findByIdEvaluatorId(evaluatorId)).thenReturn(Optional.empty());

        List<ProcessDto> processes = processService
                .findProcesses(User.builder().id(evaluatorId).role(EVALUATOR).build());

        Assertions.assertThat(processes).isEmpty();
    }

    @Test
    public void createProcessWithoutEvaluatorIds() throws BadRequestException {
        Mockito.when(processRepository.save(ArgumentMatchers.any(Process.class))).thenReturn(mockProcess);

        ProcessDto process = processService.createProcess(ProcessDto.builder().name(mockProcess.getName())
                .content(mockProcess.getContent()).evaluatorIds(List.of()).build(), User.builder().build());

        Assertions.assertThat(process).isEqualTo(mockProcessDto);
    }

    @Test
    public void createProcessWithEvaluatorIdsAndUserNotFound() {
        Long evaluatorId = 1L;

        Mockito.when(processRepository.save(ArgumentMatchers.any(Process.class))).thenReturn(mockProcess);

        Mockito.when(userRepository.findById(evaluatorId)).thenReturn(Optional.empty());

        try {
            processService.createProcess(ProcessDto.builder().name(mockProcess.getName())
                    .content(mockProcess.getContent()).evaluatorIds(List.of(evaluatorId)).build(),
                    User.builder().build());
            Assertions.fail("BadRequestException should have been generated");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(BadRequestException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo(String.format("User with id %s not found", evaluatorId));
        }
    }

    @Test
    public void createProcessWithEvaluatorIdsAndUserNotEvaluator() {
        Long evaluatorId = 1L;

        Mockito.when(processRepository.save(ArgumentMatchers.any(Process.class))).thenReturn(mockProcess);

        Mockito.when(userRepository.findById(evaluatorId))
                .thenReturn(Optional.of(User.builder().role(ADMINISTRATOR).build()));

        try {
            processService.createProcess(ProcessDto.builder().name(mockProcess.getName())
                    .content(mockProcess.getContent()).evaluatorIds(List.of(evaluatorId)).build(),
                    User.builder().build());
            Assertions.fail("BadRequestException should have been generated");
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(BadRequestException.class);
            Assertions.assertThat(e.getMessage())
                    .isEqualTo(String.format("User with id %s not is evaluator", evaluatorId));
        }
    }

    @Test
    public void createProcessWithEvaluatorIds() throws BadRequestException {
        Long evaluatorId = 1L;

        Mockito.when(processRepository.save(ArgumentMatchers.any(Process.class))).thenReturn(mockProcess);

        Mockito.when(userRepository.findById(evaluatorId))
                .thenReturn(Optional.of(User.builder().role(EVALUATOR).build()));

        Mockito.when(evaluationRepository.save(ArgumentMatchers.any(Evaluation.class)))
                .thenReturn(Evaluation.builder().build());

        ProcessDto process = processService.createProcess(ProcessDto.builder().name(mockProcess.getName())
                .content(mockProcess.getContent()).evaluatorIds(List.of(evaluatorId)).build(), User.builder().build());

        Assertions.assertThat(process).isEqualTo(mockProcessDto);
    }
}