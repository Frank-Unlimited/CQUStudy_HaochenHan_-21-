package org.example.controller;

import org.example.entity.Person;
import org.example.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/person")
public class mongoController {

    @Autowired
    PersonService personService;

    @GetMapping(value = "/insert/{id}/{name}/{age}")
    public String insert(@PathVariable String id, @PathVariable String name, @PathVariable Integer age){
        return personService.insert(id, name, age);
    }

    @GetMapping(value = "/find/{id}")
    public String find(@PathVariable String id){
        Person p = personService.find(id);
        return p.toString();
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable String id){
        personService.delete(id);
        return "SUCCEED";
    }

}
