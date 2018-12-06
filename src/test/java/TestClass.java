public class TestClass {
    private Integer testField;

    public Integer getTestField() {
        return testField;
    }

    public TestClass(Integer testField) {
        this.testField = testField;
    }

    public TestClass() {
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "testField=" + testField +
                '}';
    }

    public void setTestField(Integer testField) {
        this.testField = testField;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestClass testClass = (TestClass) o;

        return testField == testClass.testField;
    }

    @Override
    public int hashCode() {
        return testField;
    }
}
