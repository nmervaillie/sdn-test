package com.sdn.test.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = "PERSON")
public class Person {

    @GraphId
    public Long id;

    @Property(name = "name")
    public String name;

    @Relationship(type = "OWNS", direction = Relationship.OUTGOING)
    public Set<OwnsRelationship> ownsRelationshipSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<OwnsRelationship> getOwnsRelationshipSet() {
        return ownsRelationshipSet;
    }

    public void setOwnsRelationshipSet(Set<OwnsRelationship> ownsRelationshipSet) {
        this.ownsRelationshipSet = ownsRelationshipSet;
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
