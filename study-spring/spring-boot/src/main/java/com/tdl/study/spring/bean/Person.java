package com.tdl.study.spring.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Person {

    @Value("${person.name}")
    private String name;

    @Value("${person.gender}")
    private String gender;

    @Value("${person.age}")
    private int age;

    @Value("${person.address}")
    private String address;

}
