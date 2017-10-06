package com.sdn.test.service;

import com.sdn.test.Stats;

import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;
import org.neo4j.driver.v1.exceptions.SessionExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

	@Autowired AppService service;

	public Stats read(long i) {
		return service.read(i);
	}

	@Retryable(value = {SessionExpiredException.class, ServiceUnavailableException.class, InvalidDataAccessApiUsageException.class}, maxAttempts=10, backoff=@Backoff(delay= 3000, multiplier = 2))
	public Stats crud(long i) {
		return service.crud(i);
	}

	@Retryable(value = {SessionExpiredException.class, ServiceUnavailableException.class, InvalidDataAccessApiUsageException.class}, maxAttempts=10, backoff=@Backoff(delay= 3000, multiplier = 2))
	public Stats deleteById(Long id) {
		return service.deleteById(id);
	}

	@Retryable(value = {SessionExpiredException.class, ServiceUnavailableException.class, InvalidDataAccessApiUsageException.class}, maxAttempts=10, backoff=@Backoff(delay= 3000, multiplier = 2))
	public void createPersonOwnsCarNode() {
		service.createPersonOwnsCarNode();
	}

}
