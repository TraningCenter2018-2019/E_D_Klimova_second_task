import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ParseUtils {
    public static Method getSetter(Class clazz, Field field, Class... paramTypes) throws NoSuchMethodException {
        String fieldName = field.getName();
        String name = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
        return clazz.getMethod(name, paramTypes);
    }

    public static Method getGetter(Class clazz, Field field, Class... paramTypes) throws NoSuchMethodException {
        String fieldName = field.getName();
        String name = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
        return clazz.getMethod(name, paramTypes);
    }

    public static Object castValueToFieldType(Field field, String fieldValue) throws JsonParseException {
        Type t ;
        if(field.getType().isArray()){
             t  = field.getType().getComponentType();
        }
        else  t = field.getType();

        if (t.equals(Long.class)){
            return Long.valueOf(fieldValue);
        }

        if (t.equals(Integer.class)) {
            return Integer.valueOf(fieldValue);
        }

        if (t.equals(Float.class)) {
            return Float.valueOf(fieldValue);
        }

        if (t.equals(Double.class)) {
            return Double.valueOf(fieldValue);
        }
        if (t.equals(Boolean.class)) {
            return Boolean.valueOf(fieldValue);
        }
        if (t.equals(Byte.class)) {
            return Byte.valueOf(fieldValue);
        }
        if (t.equals(Character.class)) {
            if (fieldValue.length() > 1) {
                throw new JsonParseException("cannot parse char field " + fieldValue);
            }
            return Character.valueOf(fieldValue.charAt(0));
        }
        return fieldValue;
    }

    public static boolean isChars(Class c) {
        return (c.equals(char.class) || c.equals(Character.class) || c.equals(String.class));
    }
    public static boolean isPrimitiveOrWrapper( Class c ){
        return (c.isPrimitive()||c.equals(Long.class)|| c.equals(Integer.class)|| c.equals(Float.class) ||
                c.equals(Double.class) || c.equals(Boolean.class) || c.equals(Byte.class) || c.equals(Character.class)
                || c.equals(String.class));
    }
}