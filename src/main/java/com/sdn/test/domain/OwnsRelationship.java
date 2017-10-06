package com.sdn.test.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "OWNS")
public class OwnsRelationship {
    @GraphId
    Long id;

    @StartNode
    Person person;

    @EndNode
    Car car;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "OwnsRelationship{" +
                "id=" + id +
                ", person=" + person +
                ", car=" + car +
                '}';
    }

}
