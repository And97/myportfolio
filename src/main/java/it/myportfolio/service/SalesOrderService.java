package it.myportfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.myportfolio.model.SalesOrder;
import it.myportfolio.model.User;
import it.myportfolio.repository.SalesOrderRepository;

@Service
public class SalesOrderService {

	@Autowired
    private SalesOrderRepository salesOrderRepository;

    public List<SalesOrder> getOrdersByUser(User user) {
        return salesOrderRepository.findByUser(user);
    }
    
    public List<SalesOrder> getAllOrders() {
        return salesOrderRepository.findAll();
    }
    
    public Optional<SalesOrder> getOrderById(Long id) {
    	return salesOrderRepository.findById(id);
    }
    
    public SalesOrder addSalesOrder(SalesOrder salesOrder) {
		return salesOrderRepository.save(salesOrder);
	}
}
