package com.example.demo.controllers;

import java.util.List;

import com.example.demo.util.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger logger = LogManager.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		logger.info("OrderController: submit cart execution started..");
		logger.info("OrderController: submit cart execution for {}", username);

		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("OrderController: submit cart failed for {} doesn't exist", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		logger.info("OrderController: submitted cart for {}!", username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		logger.info("OrderController: getOrderForUser started..");
		logger.info("OrderController: getOrderForUser execution for {}", username);

		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("OrderController: getOrderForUser failed for {} doesn't exist", username);
			return ResponseEntity.notFound().build();
		}
		logger.info("OrderController: getOrderForUser execution for {} succeeded!", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
