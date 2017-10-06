package com.sdn.test.repository;

import com.sdn.test.domain.Person;
import com.sdn.test.domain.result.CollectCustomResult;
import com.sdn.test.domain.result.CustomResult;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends GraphRepository<Person> {

}
