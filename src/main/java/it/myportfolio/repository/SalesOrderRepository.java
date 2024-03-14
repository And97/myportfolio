package it.myportfolio.repository;

import org.springframework.data.repository.CrudRepository;

import it.myportfolio.model.SalesOrder;

public interface SalesOrderRepository extends CrudRepository<SalesOrder, Long> {

}
