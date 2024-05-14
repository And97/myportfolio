package it.myportfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.myportfolio.model.SalesOrder;
import it.myportfolio.model.User;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
	List<SalesOrder> findByUser(User user); 
	//List<SalesOrder> findByUserId(Long userId);
	List<SalesOrder> findAll();
	Optional<SalesOrder> findById(Long id);

}
