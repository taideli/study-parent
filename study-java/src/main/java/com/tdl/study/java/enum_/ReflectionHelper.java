package com.tdl.study.java.enum_;

import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {
    private static final String MODIFIERS_FIELD = "modifiers";
    private static final ReflectionFactory reflection = ReflectionFactory.getReflectionFactory();

    public static void setStaticFinalField(Field field, Object value) throws NoSuchFieldException, IllegalAccessException {
        // 获得public权限
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        // 将modifiers域设为非final,以进行后续修改
        Field modifiersField = Field.class.getDeclaredField(MODIFIERS_FIELD);
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);
        // 去掉final标识
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);
        FieldAccessor fa = reflection.newFieldAccessor(field, false);
        fa.set(null, value);
    }
}
