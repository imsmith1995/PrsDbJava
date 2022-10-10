package com.bootcamp.capstone.prs.requestline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.capstone.prs.product.ProductRepository;
import com.bootcamp.capstone.prs.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlinesController {

	@Autowired
	private RequestlineRepository rlnRepo;
	
	@Autowired
	private RequestRepository reqRepo;
	
	@Autowired
	private ProductRepository prodRepo;
	
	@GetMapping
	private ResponseEntity<Iterable<Requestline>> getRequestlines(){
		var rlns = rlnRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(rlns, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	private ResponseEntity<Requestline> getRequestlineById(@PathVariable int id){
		var rln = rlnRepo.findById(id);
		if(rln.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(rln.get(), HttpStatus.OK);
	}
	
	@PostMapping
	private ResponseEntity<Requestline>	postRequestline(@RequestBody Requestline requestline) throws Exception{
		if(requestline.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var rln = rlnRepo.save(requestline);
		RecalculateRequestTotal(requestline.getRequest().getId());
		return new ResponseEntity<Requestline>(rln, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	private ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline) throws Exception {
		if(requestline.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var rln = rlnRepo.findById(id);
		var oldId = rln.get().getRequest().getId();
		if(rln.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		rlnRepo.save(requestline);
		if(requestline.getRequest().getId() != oldId) {
			RecalculateRequestTotal(oldId);
		};
		RecalculateRequestTotal(requestline.getRequest().getId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	private ResponseEntity deleteRequestline(@PathVariable int id) throws Exception {
		var rln = rlnRepo.findById(id);
		if(rln.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		rlnRepo.delete(rln.get());
		RecalculateRequestTotal(rln.get().getRequest().getId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	private void RecalculateRequestTotal(int requestId) throws Exception {
		var rlns = rlnRepo.findByRequestId(requestId);
		var req = reqRepo.findById(requestId);
		
		if(rlns.isEmpty() || req.isEmpty()) {
			throw new Exception ("Recalculate failed!");
		}
		var total = 0;
		
		for(Requestline rln : rlns.get()) {
			var prod = prodRepo.findById(rln.getProduct().getId());
			if(prod.isEmpty()) {
				throw new Exception ("Recalculate failed! Product not found!");
			}
			var price = prod.get().getPrice();
			var quantity = rln.getQuantity();
			total += (price * quantity);
		}
		req.get().setTotal(total);
		reqRepo.save(req.get());
		
	}
}






