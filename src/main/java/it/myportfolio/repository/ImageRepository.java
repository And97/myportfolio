package it.myportfolio.repository;

import org.springframework.data.repository.CrudRepository;

import it.myportfolio.model.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {

}
