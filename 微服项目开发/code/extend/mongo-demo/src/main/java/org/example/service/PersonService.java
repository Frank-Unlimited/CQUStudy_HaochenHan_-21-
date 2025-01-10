package org.example.service;

import org.example.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Service
public class PersonService {
    @Autowired
    private MongoTemplate mongoOps;

    public String insert(String id, String name, Integer age){
        Person p = new Person(id,name, age);
        mongoOps.insert(p);
        System.out.println("Insert: " + p);
        return p.toString();
    }

    public Person find(String id){
        Person p = mongoOps.findById(id, Person.class);
        return  p;
    }

    public void delete(String id){
        Person p = find(id);
        mongoOps.remove(p);
    }

}
