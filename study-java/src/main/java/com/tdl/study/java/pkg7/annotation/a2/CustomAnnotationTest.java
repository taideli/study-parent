package com.tdl.study.java.pkg7.annotation.a2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CustomAnnotationTest {
    public static void main(String[] args) {
        Class<AnnotatedClass> object = AnnotatedClass.class;

        // 从类检索所有的注解
        Annotation[] annotations = object.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        // checks if an annotation is present
        if (object.isAnnotationPresent(CustomAnnotationClass.class)) {
            Annotation annotation = object.getAnnotation(CustomAnnotationClass.class);
            System.out.println(annotation);
        }

        // the same for all methods of the class
        for (Method method: object.getDeclaredMethods()) {
            if (method.isAnnotationPresent(CustomAnnotationMethod.class)) {
                Annotation annotation = method.getAnnotation(CustomAnnotationMethod.class);
                System.out.println(annotation);
            }
        }
    }
}
