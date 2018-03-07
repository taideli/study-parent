package com.tdl.study.java.bean;


import com.tdl.study.java.anno.Column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseBean {

    public static <T extends BaseBean> T of(ResultSet rs, Class<T> clazz) throws IllegalAccessException, InstantiationException, SQLException {
        assert null != clazz;
        if (null == rs) return null;
        T instance = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (null == column) continue;
            String name = column.value();
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(instance, rs.getObject(name));
            field.setAccessible(accessible);
        }
        return instance;
    }

    public static <T extends BaseBean> List<T> toList(ResultSet rs, Class<T> clazz) {
        assert null != clazz;

        List<T> list = new LinkedList<>();
        if (null == rs) return list;
        try {
            while (rs.next()) {
                list.add(of(rs, clazz));
            }
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to collect ResultSet", e);
            //e.printStackTrace();
        }
        return list;
    }

    public Object get(String column) {
        Field[] fields = getClass().getDeclaredFields();
        Optional<Field> optional = Arrays.stream(fields)
                .filter(field -> column.equals(field.getAnnotation(Column.class).value())).findFirst();
        Object value = null;
        if (optional.isPresent()) {
            Field f = optional.get();
            boolean accessible = f.isAccessible();
            f.setAccessible(true);
            try {
                value = f.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            f.setAccessible(accessible);
        }
        return value;
    }

/*    public <T> T get(String column, Class<? extends T> clazz) {
        try {
            return clazz.getConstructor(String.class).newInstance(get(column));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }*/

    @Override
    public String toString() {
        Field[] fields = getClass().getDeclaredFields();
        return Arrays.stream(fields).map(field -> {
            String anno = field.getAnnotation(Column.class).value();
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            field.setAccessible(accessible);
            return anno + "=" + value;
        }).collect(Collectors.joining(", ", "[", "]"));
    }
}
