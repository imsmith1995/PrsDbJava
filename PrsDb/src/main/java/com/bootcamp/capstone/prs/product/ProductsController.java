package com.bootcamp.capstone.prs.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductsController {
	
	@Autowired
	private ProductRepository prodRepo;
	
	@GetMapping
	private ResponseEntity<Iterable<Product>> getProducts(){
		var prod = prodRepo.findAll();
		return new ResponseEntity<Iterable<Product>>(prod, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	private ResponseEntity<Product> getProductById(@PathVariable int id){
		var prod = prodRepo.findById(id);
		if(prod.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(prod.get(), HttpStatus.OK);
	}
	
	@PostMapping
	private ResponseEntity<Product> postProduct(@RequestBody Product product){
		if(product.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var prod = prodRepo.save(product);
		return new ResponseEntity<Product>(prod, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	private ResponseEntity putProduct(@PathVariable int id, @RequestBody Product product) {
		if(product.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		prodRepo.save(product);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	private ResponseEntity deleteProduct(@PathVariable int id) {
		var prod = prodRepo.findById(id);
		if(prod.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		prodRepo.delete(prod.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}



