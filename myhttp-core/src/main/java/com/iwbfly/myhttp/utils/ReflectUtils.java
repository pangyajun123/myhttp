package com.iwbfly.myhttp.utils;


import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReflectUtils {

    /**
     * 从Type获取Class
     * @param genericType
     * @return
     */
    public static Class getClassByType(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            Class clz = ((Class) pt.getRawType());
            return clz;
        } else if (genericType instanceof TypeVariable) {
            TypeVariable tType = (TypeVariable) genericType;
            String className = tType.getGenericDeclaration().toString();
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
            }
            return null;
        } else {
            Class clz = (Class) genericType;
            return clz;
        }
    }

    public static boolean isPrimaryType(Class type) {
        if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
            return true;
        }
        if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            return true;
        }
        if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return true;
        }
        if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            return true;
        }
        if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return true;
        }
        if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return true;
        }
        if (BigDecimal.class.isAssignableFrom(type)) {
            return true;
        }
        if (BigInteger.class.isAssignableFrom(type)) {
            return true;
        }
        if (CharSequence.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    public static Map<String, Object> getAttributesFromAnnotation(Annotation ann) {
        Set<String> excludeMethodNames = new HashSet<>();
        excludeMethodNames.add("equals");
        excludeMethodNames.add("getClass");
        excludeMethodNames.add("annotationType");
        excludeMethodNames.add("notify");
        excludeMethodNames.add("notifyAll");
        excludeMethodNames.add("wait");
        excludeMethodNames.add("hashCode");
        excludeMethodNames.add("toString");
        excludeMethodNames.add("newProxyInstance");
        excludeMethodNames.add("newProxyClass");
        excludeMethodNames.add("getInvocationHandler");

        Map<String, Object> results = new HashMap<>();
        Class clazz = ann.getClass();
        Method[] methods = clazz.getMethods();
        Object[] args = new Object[0];
        for (Method method : methods) {
            String name = method.getName();
            if (excludeMethodNames.contains(name)) {
                continue;
            }
            if (method.getParameters().length > 0) {
                continue;
            }
            try {
                Object value = method.invoke(ann, args);
                results.put(name, value);
            } catch (IllegalAccessException e) {
                throw new MyhttpRuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new MyhttpRuntimeException(e);
            }
        }
        return results;
    }


    public static void copyAnnotationAttributes(Annotation source, Object target) {
        if (target == null) {
            return;
        }
        Map<String, Object> attrs = getAttributesFromAnnotation(source);
        Class targetClass = target.getClass();
        for (Field field : targetClass.getDeclaredFields()) {
            try {
                String methodName = NameUtils.setterName(field.getName());
                Method method = targetClass.getDeclaredMethod(methodName, field.getType());
                Object param = attrs.get(field.getName());
                if (param != null)
                    method.invoke(target, param);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }
    }

}
