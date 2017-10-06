package com.sdn.test.repository;

import com.sdn.test.domain.OwnsRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnsRepository extends GraphRepository<OwnsRelationship>{

    @Query("MATCH (n)-[rel:OWNS]->(p) WHERE ID(n) = {0} AND ID(p) = {1} RETURN rel")
    public OwnsRelationship getRelationship(Long personId, Long carId);

}
