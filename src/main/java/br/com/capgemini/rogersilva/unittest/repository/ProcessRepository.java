package br.com.capgemini.rogersilva.unittest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.capgemini.rogersilva.unittest.model.Process;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
}