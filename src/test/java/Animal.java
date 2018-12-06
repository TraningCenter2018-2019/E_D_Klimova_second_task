import java.util.Arrays;

public class Animal {


    private Long age;

    private String name;
    @JsonIgnored
    private Integer weight;

    private TestClass testClass;

    private Integer[] days ;

    public Integer[] getDays() {
        return days;
    }
    private TestClass[] testClasses;

    public TestClass[] getTestClasses() {
        return testClasses;
    }

    public Animal(Long age, String name) {
        this.age = age;
        this.name = name;
    }

    public Animal() {
    }

    public void setTestClasses(TestClass[] testClasses) {
        this.testClasses = testClasses;
    }

    public void setDays(Integer[] days) {
        this.days = days;
    }

    public TestClass getTestClass() {
        return testClass;
    }

    public void setTestClass(TestClass testClass) {
        this.testClass = testClass;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", testClass=" + testClass +
                ", days=" + Arrays.toString(days) +
                ", testClasses=" + Arrays.toString(testClasses) +
                '}';
    }
}
