package br.com.capgemini.rogersilva.unittest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.capgemini.rogersilva.unittest.model.Evaluation;
import br.com.capgemini.rogersilva.unittest.model.EvaluationId;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, EvaluationId> {

    Optional<List<Evaluation>> findByIdEvaluatorId(Long evaluatorId);
}