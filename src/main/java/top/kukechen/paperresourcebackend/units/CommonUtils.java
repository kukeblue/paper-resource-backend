package top.kukechen.paperresourcebackend.units;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public  class CommonUtils {
    public static Map<String, Object> getObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String keyName = field.getName();
            Object value = field.get(obj);
            if (value != null) {
                map.put(keyName, value);
            }
        }
        return map;
    }
}
