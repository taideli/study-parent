package com.tdl.study.spring.t2;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
//@Component   //这几种注解好像差别不大?
//@Repository
//@Controller
public class FunctionService {
    public String sayHello(String word) {
        return "Hello " + word + " !";
    }
}
