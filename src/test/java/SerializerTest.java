import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SerializerTest {

    @Test
    public void simpleJsonStringTest() throws JsonParseException {
        Serializer sl = new Serializer();
        Assert.assertEquals(sl.toJsonString(new Animal(20L, "name")), "{\"age\":20,\"name\":\"name\"}");
    }

    @Test
    public void internalJsonStringTest() throws JsonParseException {
        Serializer sl = new Serializer();
        Animal a =new Animal(20L, "name");
        TestClass tc = new TestClass();
        tc.setTestField(2);
        a.setTestClass(tc);
        a.setDays(new Integer[]{1,2,3});
        Assert.assertNotEquals(sl.toJsonString(a), "{\"age\":20,\"name\":\"name\"}");
        Assert.assertEquals(sl.toJsonString(a),"{\"age\":20,\"name\":\"name\",\"testClass\":{\"testField\":2},\"days\":[1,2,3]}");
    }

    @Test
    public void internalJsonArrayTest() throws JsonParseException {
        Serializer sl = new Serializer();
        Animal a =new Animal(20L, "name");
        TestClass tc1 = new TestClass();
        tc1.setTestField(1);
        TestClass tc2 = new TestClass();
        tc2.setTestField(2);
        TestClass tc3 = new TestClass();
        tc3.setTestField(3);
        a.setTestClasses(new TestClass[]{tc1,tc2,tc3});
        Assert.assertNotEquals(sl.toJsonString(a), "{\"age\":20,\"name\":\"name\"}");
        Assert.assertEquals(sl.toJsonString(a),"{\"age\":20,\"name\":\"name\",\"testClasses\":[{\"testField\":1},{\"testField\":2},{\"testField\":3}]}");
    }

    @Test
    public void toJsonArray() throws JsonParseException {
        StringBuilder result;
        List<Integer> coll = new ArrayList<>();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        Serializer sl = new Serializer();
        Assert.assertEquals(sl.toJsonArray(coll),"[1,2,3]");
    }
}