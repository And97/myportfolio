package it.myportfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
	
	 @Query("SELECT w FROM Work w JOIN w.users u WHERE w.id = :workId AND u.id = :userId")
	    Optional<Work> findWorkByIdAndUserId(@Param("workId") Long workId, @Param("userId") Long userId);
	
	 @Query("SELECT w FROM Work w JOIN w.image i WHERE i.Id = :imageId")
		Work findWorkByImageId(@Param("imageId") Long imageId);
}
