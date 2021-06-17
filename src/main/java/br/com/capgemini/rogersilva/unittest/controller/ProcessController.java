package br.com.capgemini.rogersilva.unittest.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.capgemini.rogersilva.unittest.dto.ProcessDto;
import br.com.capgemini.rogersilva.unittest.exception.BadRequestException;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.service.ProcessService;

@RestController
@Validated
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @GetMapping
    @PreAuthorize("hasAuthority('GRADER') or hasAuthority('EVALUATOR')")
    public ResponseEntity<List<ProcessDto>> findProcesses(Authentication authentication) {
        return ResponseEntity.ok().body(processService.findProcesses((User) authentication.getPrincipal()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GRADER')")
    public ResponseEntity<ProcessDto> createProcess(Authentication authentication,
            @Valid @RequestBody ProcessDto processDto) throws BadRequestException {
        return ResponseEntity.status(CREATED)
                .body(processService.createProcess(processDto, (User) authentication.getPrincipal()));
    }
}