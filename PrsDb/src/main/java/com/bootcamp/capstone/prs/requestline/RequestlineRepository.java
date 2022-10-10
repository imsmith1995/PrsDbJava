package com.bootcamp.capstone.prs.requestline;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RequestlineRepository extends CrudRepository<Requestline, Integer>{

	Optional<Iterable<Requestline>> findByRequestId(int requestId);
}
