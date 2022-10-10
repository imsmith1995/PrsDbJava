package com.bootcamp.capstone.prs.request;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Integer>{

	Optional<Request> findFirst1ByUserId(int userId);
	
	Optional<Iterable<Request>> getRequestsByStatusAndUserIdNot(String status, int userId);
	
}
