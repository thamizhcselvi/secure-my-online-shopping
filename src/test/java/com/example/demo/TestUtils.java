package com.example.demo;

import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target,String fieldName,Object toInject){
        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if(!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target,toInject);
            if(wasPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(){
        String username = "admin";
        String password = "adminPass";
        return new User(username,password,null);
    }
}
