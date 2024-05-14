package it.myportfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.Cart;
import it.myportfolio.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	
	public Optional<Cart> getCartByUser (User user);

	public void deleteByUserId(Long userId);

}