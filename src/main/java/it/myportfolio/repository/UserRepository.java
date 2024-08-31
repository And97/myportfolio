package it.myportfolio.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.User;
import it.myportfolio.model.Work;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT w FROM User u JOIN u.visibleWorks w WHERE u.id = :userId")
	public Set<Work> findVisibleWorksByUserId(@Param("userId") Long userId);
		
	  Optional<User> findByUsername(String username);

	  Boolean existsByUsername(String username);

	  Boolean existsByEmail(String email);

	  Optional<User> findByUsernameAndEnable(String username, boolean enable);
}
