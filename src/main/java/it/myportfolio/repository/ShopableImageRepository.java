package it.myportfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.ShopableImage;

@Repository
public interface ShopableImageRepository extends JpaRepository<ShopableImage, Long> {

}
