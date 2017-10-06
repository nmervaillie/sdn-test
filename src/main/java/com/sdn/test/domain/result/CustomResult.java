package com.sdn.test.domain.result;

import com.sdn.test.domain.Car;
import com.sdn.test.domain.Person;
import com.sdn.test.domain.OwnsRelationship;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class CustomResult {
    public Person owner;
    public Car car;
    public OwnsRelationship rel;

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public OwnsRelationship getRel() {
        return rel;
    }

    public void setRel(OwnsRelationship rel) {
        this.rel = rel;
    }
}
