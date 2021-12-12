package com.iwbfly.myhttp.converter.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/11/25 13:41
 **/
public class FastJsonConverter implements JsonConverter{

    /**
     * Fastjson序列化方式
     */
    private String serializerFeatureName = "DisableCircularReferenceDetect";

    private SerializerFeature serializerFeature;

    private static Field nameField;

    private static Method nameMethod;

    static {
        Class clazz = FieldInfo.class;
        try {
            nameField = clazz.getField("name");
        } catch (NoSuchFieldException e) {
            try {
                nameMethod = clazz.getMethod("getName", new Class[0]);
            } catch (NoSuchMethodException ex) {
            }
        }
    }

    public String getSerializerFeatureName() {
        return serializerFeatureName;
    }

    public void setSerializerFeatureName(String serializerFeatureName) {
        this.serializerFeatureName = serializerFeatureName;
        SerializerFeature feature = SerializerFeature.valueOf(serializerFeatureName);
        setSerializerFeature(feature);
    }

    public SerializerFeature getSerializerFeature() {
        return serializerFeature;
    }

    public FastJsonConverter() {
        setSerializerFeature(SerializerFeature.valueOf(serializerFeatureName));
    }

    public void setSerializerFeature(SerializerFeature serializerFeature) {
        this.serializerFeature = serializerFeature;
        if (serializerFeature == null) {
            this.serializerFeatureName = null;
        }
        else {
            this.serializerFeatureName = serializerFeature.name();
        }
    }
    @Override
    public <T> T decode(String source, Class<T> targetClass) {
        try {
            return JSON.parseObject(source, targetClass);
        } catch (Throwable th) {
            throw new MyhttpRuntimeException(th);
        }
    }

    @Override
    public <T> T decode(String source, Type targetType) {
        try {
            return JSON.parseObject(source, targetType);
        } catch (Throwable th) {
            throw new MyhttpRuntimeException(th);
        }
    }

    @Override
    public String encode(Object obj) {
        try {
            if (serializerFeature == null) {
                return JSON.toJSONString(obj);
            }
            return JSON.toJSONString(obj, serializerFeature);
        } catch (Throwable th) {
            throw new MyhttpRuntimeException(th);
        }
    }
    @Override
    public Map<String, Object> convertObjectToMap(Object obj) {
        if (nameField == null && nameMethod == null) {
            return defaultJsonMap(obj);
        }
        List<FieldInfo> getters = TypeUtils.computeGetters(obj.getClass(), null);
        JSONObject json = new JSONObject(getters.size(), true);
        try {
            for (FieldInfo field : getters) {
                Object value = field.get(obj);
                Object jsonValue = JSON.toJSON(value);
                if (nameField != null) {
                    json.put((String) nameField.get(field), jsonValue);
                } else if (nameMethod != null) {
                    json.put((String) nameMethod.invoke(field), jsonValue);
                }
            }
            return json;
        } catch (IllegalAccessException e) {
            return defaultJsonMap(obj);
        } catch (InvocationTargetException e) {
            return defaultJsonMap(obj);
        }
    }
    public Map<String, Object> defaultJsonMap(Object obj) {
        Object jsonObj = JSON.toJSON(obj);
        return (Map<String, Object>) jsonObj;
    }

}
