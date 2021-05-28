package br.com.capgemini.rogersilva.unittest.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.capgemini.rogersilva.unittest.dto.EvaluationDto;
import br.com.capgemini.rogersilva.unittest.exception.BadRequestException;
import br.com.capgemini.rogersilva.unittest.exception.NotFoundException;
import br.com.capgemini.rogersilva.unittest.model.Evaluation;
import br.com.capgemini.rogersilva.unittest.model.EvaluationId;
import br.com.capgemini.rogersilva.unittest.model.Process;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.repository.EvaluationRepository;
import br.com.capgemini.rogersilva.unittest.repository.ProcessRepository;

@Service
@Transactional
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ProcessRepository processRepository;

    public EvaluationDto createEvaluation(EvaluationDto evaluationDto, User user)
            throws BadRequestException, NotFoundException {
        Process process = processRepository.findById(evaluationDto.getProcessId())
                .orElseThrow(() -> new BadRequestException(
                        String.format("Process with id %s not found", evaluationDto.getProcessId())));

        Evaluation evaluation = evaluationRepository
                .findById(EvaluationId.builder().evaluator(user).process(process).build())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Evaluation for user with id %s and process with id %s not found", user.getId(),
                                evaluationDto.getProcessId())));

        evaluation.setFeedback(evaluationDto.getFeedback());
        evaluation.setUpdatedAt(LocalDateTime.now());

        return convertToEvaluationDto(evaluationRepository.save(evaluation));
    }

    private EvaluationDto convertToEvaluationDto(Evaluation evaluation) {
        return EvaluationDto.builder().feedback(evaluation.getFeedback()).build();
    }
}