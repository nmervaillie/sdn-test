package com.sdn.test.domain.result;

import com.sdn.test.domain.Car;
import com.sdn.test.domain.Person;
import com.sdn.test.domain.OwnsRelationship;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Set;

@QueryResult
public class CollectCustomResult {
    public Set<Person> owner;
    public Set<Car> car;
    public Set<OwnsRelationship> rel;

    public Set<Person> getOwner() {
        return owner;
    }

    public void setOwner(Set<Person> owner) {
        this.owner = owner;
    }

    public Set<Car> getCar() {
        return car;
    }

    public void setCar(Set<Car> car) {
        this.car = car;
    }

    public Set<OwnsRelationship> getRel() {
        return rel;
    }

    public void setRel(Set<OwnsRelationship> rel) {
        this.rel = rel;
    }
}
