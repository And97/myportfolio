package it.myportfolio.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.myportfolio.model.User;
import it.myportfolio.model.Work;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT w FROM User u JOIN u.visibleWorks w WHERE u.id = :userId")
	public Set<Work> findVisibleWorksByUserId(@Param("userId") Long userId);
}
