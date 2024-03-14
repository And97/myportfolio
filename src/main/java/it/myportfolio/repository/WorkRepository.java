package it.myportfolio.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import it.myportfolio.model.Work;

public interface WorkRepository extends CrudRepository<Work, Long> {
	


}
