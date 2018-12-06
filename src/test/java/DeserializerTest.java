import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeserializerTest {

    @Test
    public void testSimpleParse() throws JsonParseException{
        String json = "{\"age\":20,\"name\":\"cute dog\"}";
        Parser parser = new Parser();
        Animal result = (Animal) parser.parse(json, Animal.class);
        Assert.assertEquals(result.getAge().longValue(), 20);
        Assert.assertEquals(result.getName(), "cute dog");
    }

    @Test
    public void testIgnorseFIledWithoutAnnotations() throws JsonParseException {
        String json = "{\"age\":20,\"name\":\"cute dog\",\"weight\":21}";
        Parser parser = new Parser();
        Animal result = (Animal) parser.parse(json, Animal.class);
        Assert.assertEquals(result.getAge().longValue(), 20);
        Assert.assertEquals(result.getName(), "cute dog");
        Assert.assertEquals(result.getWeight(), null);
    }

    @Test
    public void testArrayObjects() throws JsonParseException{
        String json = "{\"testClasses\":[{\"testField\":2},{\"testField\":3},{\"testField\":4}]}";
        Parser parser = new Parser();
        Animal animal = (Animal) parser.parse(json, Animal.class);
        Assert.assertEquals(animal.getTestClasses()[0].getTestField().intValue(),2);
        Assert.assertEquals(animal.getTestClasses()[1].getTestField().intValue(),3);
        Assert.assertEquals(animal.getTestClasses()[2].getTestField().intValue(),4);
    }

    @Test
    public void testSimpleArray() throws JsonParseException{
        String json = "{\"age\":20,\"name\":\"cute dog\",\"days\":[2,3,4],\"weight\":21}";
        Parser parser = new Parser();
        Animal animal = (Animal) parser.parse(json,Animal.class);
        Assert.assertEquals(animal.getAge().longValue(), 20);
        Assert.assertEquals(animal.getName(), "cute dog");
        Assert.assertEquals(animal.getDays()[0].intValue(), 2);
    }

    @Test
    public void testObjectArray() throws JsonParseException{
        String json = "[{\"age\":20,\"name\":\"cute dog\"},{\"age\":3,\"name\":\"cute cat\"}]";
        Parser parser = new Parser();
        List<Animal> animals = (List<Animal>) parser.parseObjectArray(json, Animal.class);
        Assert.assertEquals(animals.get(0).getAge().longValue(), 20);
        Assert.assertEquals(animals.get(0).getName(), "cute dog");
        Assert.assertEquals(animals.get(1).getAge().longValue(), 3);
        Assert.assertEquals(animals.get(1).getName(), "cute cat");
    }

    @Test
    public void testInternal() throws JsonParseException {
        String json = "{\"age\":20,\"name\":\"cute dog\",\"testClass\":{\"testField\":2},\"weight\":21}";
        Parser parser = new Parser();
        Animal result = (Animal) parser.parse(json, Animal.class);
        Assert.assertEquals(result.getAge().longValue(), 20);
        Assert.assertEquals(result.getName(), "cute dog");
        Assert.assertEquals(result.getWeight(), null);
        Assert.assertEquals(result.getTestClass(),(new TestClass(2)));
        Assert.assertEquals(result.getTestClass().getTestField(),(new TestClass(2).getTestField()));
    }

    @Test(expected = JsonParseException.class)
    public void testIncorrectJsonThrowsException() throws JsonParseException {
        String json = "{13dsadsadsa{ddd}}";
        Parser parser = new Parser();
        parser.parse(json,Animal.class);
    }

}
