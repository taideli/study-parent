package com.tdl.study.spring.ch8.e2.ctrl;

import com.tdl.study.spring.ch8.e2.entity.Person;
import com.tdl.study.spring.ch8.e2.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    @Autowired
    PersonRepository repository;

    @RequestMapping("/save")
    public Person save(String name, Integer age, String address) {
        return repository.save(new Person(null, name, age, address));
    }

    @RequestMapping("/q1")
    public List<Person> q1(String address) {
        return repository.findAllByAddress(address);
    }

    @RequestMapping("/q2")
    public Person q2(String name, String address) {
        return repository.findByNameAndAddress(name, address);
    }

    @RequestMapping("/q3")
    public Person q3(String name, String address) {
        return repository.withNameAndAddressQuery(name, address);
    }

    @RequestMapping("/q4")
    public List<Person> q4(String name, String address) {
        return repository.withNameAndAddressNamedQuery(name, address);
    }

    @RequestMapping("/sort")
    public List<Person> sort() {
        return repository.findAll(new Sort(Sort.Direction.ASC, "age"));
    }

    @RequestMapping("/page")
    public Page<Person> page() {
        return repository.findAll(new PageRequest(1, 2));
    }
}
