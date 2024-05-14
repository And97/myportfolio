package it.myportfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.ShopableImage;

@Repository
public interface ShopableImageRepository extends JpaRepository<ShopableImage, Long> {
	 public List<ShopableImage> findByIsSoldFalse();

	
}
