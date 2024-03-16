package it.myportfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
