import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Serializer {


    public String toJsonString(Object o) throws JsonParseException {
        StringBuilder result = new StringBuilder();
        Field[] fields = o.getClass().getDeclaredFields();
        result.append("{");
        for (Field f : fields)
            if (f.getAnnotation(JsonIgnored.class) == null) {
                Object value;
                try {
                    value = ParseUtils.getGetter(o.getClass(), f).invoke(o);
                    if (value != null) {
                        result.append("\"").append(f.getName()).append("\":");
                        if(value.getClass().isArray()||isCollection(value.getClass())){
                            result.append(toJsonArray(value)).append(",");
                        }else
                            result.append(objToString(value)).append(",");
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new JsonParseException(e.getMessage());
                }
            }
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("}");
        return result.toString();
    }

    public String toJsonArray(Object o) throws JsonParseException {
        StringBuilder result = new StringBuilder();
        Object[] oArr;
        if (isCollection(o.getClass())) {
            Collection coll = (Collection) o;
            oArr = coll.toArray();
        } else oArr = (Object[]) o;

        result.append("[");
        for (Object el : oArr) {
            result.append(objToString(el)).append(",");
        }
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");
        return result.toString();
    }

    private String objToString(Object value) throws JsonParseException {
        StringBuilder result = new StringBuilder();

        if (ParseUtils.isPrimitiveOrWrapper(value.getClass())) {
            if (ParseUtils.isChars(value.getClass())) {
                result.append("\"").append(value.toString()).append("\"");
            } else
                result.append(value.toString());

        } else {
            result.append(toJsonString(value));
        }

        return result.toString();
    }

    private boolean isCollection(Class o) {
        Class<?>[] superClasses = o.getInterfaces();
        for (Class c : superClasses) {
            if (Arrays.asList(superClasses).contains(Collection.class)) {
                return true;
            } else {
                if (isCollection(c)) return true;
            }
        }
        return false;
    }
}

