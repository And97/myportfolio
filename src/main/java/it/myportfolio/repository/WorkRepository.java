package it.myportfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
	
}
