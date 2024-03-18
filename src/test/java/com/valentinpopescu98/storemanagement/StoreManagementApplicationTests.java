package com.valentinpopescu98.storemanagement;

import com.valentinpopescu98.storemanagement.store.ProductController;
import com.valentinpopescu98.storemanagement.store.ProductRepository;
import com.valentinpopescu98.storemanagement.store.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StoreManagementApplicationTests {

	@Autowired
	private ProductController controller;
	@Autowired
	private ProductService service;
	@Autowired
	private ProductRepository repository;

	@Test
	void contextLoads() {
		assertNotNull(controller);
		assertNotNull(service);
		assertNotNull(repository);
	}

	@Test
	void shouldTestMain() {
		StoreManagementApplication.main(new String[] {});
	}

}
