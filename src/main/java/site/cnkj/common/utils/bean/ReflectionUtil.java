package site.cnkj.common.utils.bean;

import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ReflectionUtil extends ReflectionUtils {

    /**
     * 类上是否包含注解
     *
     * @date 2021-08-10 15:30:34
     * @author liuzhengyang
     * @param clazz  类Class
     * @param aClazz 注解Class
     * @return boolean
     */
    public static boolean containsAnnotation(Class<?> clazz, Class<? extends Annotation> aClazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == aClazz) {
                return true;
            }
        }
        return false;
    }

    public static Type getGenericParameter(Class<?> clazz) {
        while(clazz != null && !(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
        }
        if (clazz != null) {
            return ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("Invalid argument, argument's super class must specify type parameters");
        }
    }

    public static Class getSuperClassGenericType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        for(Class superClass = clazz; !(genType instanceof ParameterizedType); genType = superClass.getGenericSuperclass()) {
            if (superClass == Object.class) {
                return Object.class;
            }
            superClass = superClass.getSuperclass();
        }
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        if (index < params.length && index >= 0) {
            if (!(params[index] instanceof Class) && params[index] instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType)params[index];
                return (Class)pType.getRawType();
            } else {
                return (Class)params[index];
            }
        } else {
            return Object.class;
        }
    }

    public static Class getSuperClassGenericTypeCollectionGenType(final Class clazz, final int index, final int collectionGenindex) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class) && params[index] instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType)params[index];
                    return (Class)pType.getActualTypeArguments()[collectionGenindex];
                } else {
                    return (Class)params[index];
                }
            } else {
                return Object.class;
            }
        }
    }

    public static Object newInstance(final Class clazz, final int index) {
        try {
            return getSuperClassGenericType(clazz, index).newInstance();
        } catch (IllegalAccessException | InstantiationException var3) {
            var3.printStackTrace();
        }
        return null;
    }

    public static List<Field> getAllField(Object obj) {
        List<Field> fieldList = new ArrayList();
        for(Class tempClass = obj.getClass(); tempClass != null; tempClass = tempClass.getSuperclass()) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
        }
        return fieldList;
    }

    public static List<Field> getAllFieldByClazz(Class clazz) {
        List<Field> fieldList = new ArrayList();

        for(Class tempClass = clazz; tempClass != null; tempClass = tempClass.getSuperclass()) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
        }
        return fieldList;
    }

    public static Field findFieldForAll(Object obj, String name) {
        List<Field> fieldList = getAllField(obj);
        Iterator var3 = fieldList.iterator();
        Field f;
        do {
            if (!var3.hasNext()) {
                return null;
            }
            f = (Field)var3.next();
        } while(!f.getName().equals(name));
        return f;
    }

    public static Object getFieldValueForAll(Object obj, String name) {
        Field f = findFieldForAll(obj, name);
        f.setAccessible(true);
        try {
            return f.get(obj);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return null;
    }


}
