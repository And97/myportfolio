package it.myportfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.ImageProject;

@Repository
public interface ImageRepository extends JpaRepository<ImageProject, Long> {

}
