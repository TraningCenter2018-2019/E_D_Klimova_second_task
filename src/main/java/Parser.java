
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Parser {

    public Object parse(String json, Class clazz) throws JsonParseException {

        StringBuilder input = new StringBuilder(json);
        Object result;
        try {
            result = clazz.getConstructor().newInstance();
            cutFirstAndLastSymbol(input);
            while (input.length() > 0) {
                extractAndProcessField(input, clazz, result);
            }
            return result;
        } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                InstantiationException | StringIndexOutOfBoundsException e) {
            throw new JsonParseException(e);
        }
    }


    private void extractAndProcessField(StringBuilder input, Class clazz, Object result) throws IllegalAccessException,
            InvocationTargetException, JsonParseException, NoSuchMethodException {

        int endIndex = input.indexOf(String.valueOf(Symbols.DOUBLE_QUOTE), 1);
        String fieldName = input.substring(1, endIndex);

        Field field ;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return;
        }
        int startIndex = input.indexOf(String.valueOf(Symbols.SEPARATOR), endIndex);
        String fieldValue;
        Object castedFieldValue;

        if (Symbols.OPEN_OBJECT.equals(input.charAt(startIndex + 1))) {
            int indexInternal = startIndex + 1;
            int endIndexInternal = 0;
            int count_internal = 1;
            StringBuilder internalStr = new StringBuilder(input.substring(1));
            while (count_internal > 0) {
                endIndexInternal = internalStr.indexOf(String.valueOf(Symbols.OPEN_OBJECT), indexInternal + 1);
                if (endIndexInternal >= 0) {
                    count_internal++;
                }
                endIndexInternal = internalStr.indexOf(String.valueOf(Symbols.CLOSE_OBJECT), endIndexInternal + 1);
                if (endIndexInternal >= 0) {
                    count_internal--;
                }
            }
            castedFieldValue = parse(input.substring(indexInternal, endIndexInternal + 2), field.getType());
            input.delete(0, endIndexInternal + 3);
        } else {
            if (Symbols.ARRAY_START.equals(input.charAt(startIndex + 1))) {
                int endSymbol = input.indexOf(String.valueOf(Symbols.ARRAY_END));
                String[] collection = input.substring(startIndex + 2, endSymbol).split(",");
                Object[] collectionItems = new Object[collection.length];
                if (Symbols.OPEN_OBJECT.equals(input.charAt(startIndex + 2))) {
                    for (int i = 0; i < collection.length; i++) {
                        collectionItems[i] = parse(collection[i], field.getType().getComponentType());
                    }
                } else {
                    for (int i = 0; i < collection.length; i++) {
                        collectionItems[i] = (ParseUtils.castValueToFieldType(field, collection[i]));
                    }
                }
                input.delete(0, endSymbol + 2);
                castedFieldValue = toArray(collectionItems);
            } else {
                endIndex = input.indexOf(String.valueOf(Symbols.SPLIT_FIELDS), startIndex + 1);
                if (endIndex >= 0) {
                    fieldValue = input.substring(startIndex + 1, endIndex);
                    input.delete(0, endIndex + 1);
                } else {
                    fieldValue = input.substring(startIndex + 1);
                    input.delete(0, input.length());
                }
                if (fieldValue.contains(String.valueOf(Symbols.DOUBLE_QUOTE))) {
                    fieldValue = fieldValue.substring(1, fieldValue.length() - 1);
                }
                castedFieldValue = ParseUtils.castValueToFieldType(field, fieldValue);
            }
        }
        if (field.getDeclaredAnnotation(JsonIgnored.class) == null) {
            ParseUtils.getSetter(clazz, field, castedFieldValue.getClass()).invoke(result, castedFieldValue);

        }
    }

    private <T> T[] toArray(Object[] list) {
        Class clazz = list[0].getClass();
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(clazz, list.length);
        for (int i = 0; i < list.length; i++) {
            newArray[i] = (T) list[i];
        }
        return newArray;
    }

    public Collection parseObjectArray(String json, Class clazz) throws JsonParseException {
        List<Object> list = new ArrayList<>();
        String newJsonStr = cutFirstAndLastSymbol(json);
        do {
            int endIndex = newJsonStr.indexOf(Symbols.CLOSE_OBJECT);
            list.add(parse(newJsonStr.substring(newJsonStr.indexOf(Symbols.OPEN_OBJECT), endIndex + 1), clazz));
            newJsonStr = newJsonStr.substring(endIndex + 2);
        } while (!newJsonStr.equals(""));
        return list;
    }

    private void cutFirstAndLastSymbol(StringBuilder input) {
        input.deleteCharAt(input.length() - 1).deleteCharAt(0);
    }

    private String cutFirstAndLastSymbol(String input) {
        return input.substring(1, input.length());
    }

}
