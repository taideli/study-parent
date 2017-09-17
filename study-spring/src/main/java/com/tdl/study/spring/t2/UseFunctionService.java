package com.tdl.study.spring.t2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //声明当前类是Spring管理的一个
public class UseFunctionService {

    @Autowired //将FunctionService的实体Bean注入到此类中Bean,不用new了
    FunctionService service;

    public String sayHello(String word) {
        return service.sayHello(word);
    }
}
