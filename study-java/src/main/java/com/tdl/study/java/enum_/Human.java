package com.tdl.study.java.enum_;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 在运行时动态增加enum
 * http://www.hankcs.com/program/java/enum-java-examples-of-dynamic-modification.html
 */
public class Human {
    public void sing(HumanState state) {
        switch (state) {
            case HAPPY:
                singHappySong();
                break;
            case SAD:
                singDirge();
                break;
            default:
                throw new IllegalStateException("Invalid state: " + state);
        }
    }

    private void singHappySong() {
        System.out.println("When you're happy and you know it ...");
    }

    private void singDirge() {
        System.out.println("Don't cry for me Argentina, ...");
    }


    public static void main1(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException {
        Constructor cstr = HumanState.class.getDeclaredConstructor(String.class, int.class);
        ReflectionFactory reflection = ReflectionFactory.getReflectionFactory();
        HumanState e = (HumanState) reflection.newConstructorAccessor(cstr).newInstance(new Object[] {"ANGRY", 3});
        System.out.println(String.format("%s = %d", e.toString(), e.ordinal()));

        Human human = new Human();
        human.sing(e);
    }

    public static void main2(String[] args) {
        EnumBuster<HumanState> buster = new EnumBuster<>(HumanState.class, Human.class);
//        HumanState ANGRY = buster.make("ANGRY");
        HumanState ANGRY = buster.make("ANGRY", 1);
        buster.addByValue(ANGRY);
        System.out.println(Arrays.toString(HumanState.values()));

        Human human = new Human();
        human.sing(ANGRY);
    }

    public static void main(String[] args) {
//        try { main1(args); } catch (Exception e) { e.printStackTrace(); }
        main2(args);
    }


}
