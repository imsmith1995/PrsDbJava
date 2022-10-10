package com.bootcamp.capstone.prs.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/vendors")
public class VendorsController {

	@Autowired
	private VendorRepository venRepo;
	
	@GetMapping
	private ResponseEntity<Iterable<Vendor>> getVendors(){
		var vens = venRepo.findAll();
		return new ResponseEntity<Iterable<Vendor>>(vens, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	private ResponseEntity<Vendor> getVendorById(@PathVariable int id){
		var ven = venRepo.findById(id);
		if(ven.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(ven.get(), HttpStatus.OK);
	}
	
	@PostMapping
	private ResponseEntity<Vendor> postVendor(@RequestBody Vendor vendor) {
		if(vendor.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newVen = venRepo.save(vendor);
		return new ResponseEntity<>(newVen, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	private ResponseEntity putVendor(@PathVariable int id, @RequestBody Vendor vendor) {
		if(vendor.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var ven = venRepo.findById(id);
		if(ven.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		venRepo.save(vendor);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	private ResponseEntity deleteVendor(@PathVariable int id) {
		var ven = venRepo.findById(id);
		if(ven.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		venRepo.delete(ven.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
}





