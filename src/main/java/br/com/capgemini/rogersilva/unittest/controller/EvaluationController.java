package br.com.capgemini.rogersilva.unittest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.capgemini.rogersilva.unittest.dto.EvaluationDto;
import br.com.capgemini.rogersilva.unittest.exception.BadRequestException;
import br.com.capgemini.rogersilva.unittest.exception.NotFoundException;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.service.EvaluationService;

@RestController
@Validated
@RequestMapping("/evaluation")
@PreAuthorize("hasAuthority('EVALUATOR')")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<EvaluationDto> createEvaluation(Authentication authentication,
            @Valid @RequestBody EvaluationDto evaluationDto) throws BadRequestException, NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluationService.createEvaluation(evaluationDto, (User) authentication.getPrincipal()));
    }
}