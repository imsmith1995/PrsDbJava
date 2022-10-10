package com.bootcamp.capstone.prs.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestsController {
	
	private final String REVIEW = "REVIEW";
	private final String APPROVED = "APPROVED";
	private final String REJECTED = "REJECTED";

	@Autowired
	private RequestRepository reqRepo;
	
	@GetMapping
	private ResponseEntity<Iterable<Request>> getRequests(){
		var reqs = reqRepo.findAll();
		return new ResponseEntity<Iterable<Request>>(reqs, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	private ResponseEntity<Request> getRequestById(@PathVariable int id){
		var req = reqRepo.findById(id);
		if(req.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Request>(req.get(), HttpStatus.OK);
	}
	
	@GetMapping("reviews/{userId}")
	private ResponseEntity<Iterable<Request>> getRequestsInReview(@PathVariable int userId){
		var flag = reqRepo.findFirst1ByUserId(userId);
		if(flag.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var reqs = reqRepo.getRequestsByStatusAndUserIdNot(REVIEW, userId);
		return new ResponseEntity<Iterable<Request>> (reqs.get(), HttpStatus.OK);
	}
	
	@PostMapping
	private ResponseEntity<Request> postRequest(@RequestBody Request request){
		if(request.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var req = reqRepo.save(request);
		return new ResponseEntity<Request>(req, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	private ResponseEntity putRequest(@PathVariable int id, @RequestBody Request request) {
		if(request.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var req = reqRepo.findById(id);
		if(req.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		reqRepo.save(request);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	private ResponseEntity reviewRequest(@PathVariable int id) {
		var req = reqRepo.findById(id);
		if(req.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if(req.get().getTotal() <= 50) {
			approveRequest(id);
		}
		else {
		req.get().setStatus(REVIEW);
		};
		putRequest(id, req.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	private ResponseEntity approveRequest(@PathVariable int id) {
		var req = reqRepo.findById(id);
		if(req.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		req.get().setStatus(APPROVED);
		putRequest(id, req.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	private ResponseEntity rejectRequest(@PathVariable int id) {
		var req = reqRepo.findById(id);
		if(req.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		req.get().setStatus(REJECTED);
		putRequest(id, req.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	private ResponseEntity deleteRequest(@PathVariable int id) {
		var req = reqRepo.findById(id);
		if(req.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		reqRepo.delete(req.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}




