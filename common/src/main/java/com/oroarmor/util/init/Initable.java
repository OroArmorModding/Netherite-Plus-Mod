package com.oroarmor.util.init;

import java.lang.reflect.Method;

public interface Initable {
    static void initClass(Class<?> clazz) {
        try {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> _interface : interfaces) {
                if (_interface == Initable.class) {
                    Method init = clazz.getMethod("init");
                    init.invoke(null);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void initClasses(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            initClass(clazz);
        }
    }
}
