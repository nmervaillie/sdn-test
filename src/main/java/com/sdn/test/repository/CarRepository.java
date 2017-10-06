package com.sdn.test.repository;

import com.sdn.test.domain.Car;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends GraphRepository<Car> {

}
