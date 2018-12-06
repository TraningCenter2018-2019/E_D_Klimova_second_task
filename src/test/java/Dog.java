public class Dog extends Animal {
    private float tailLength;

    public Dog(int age, String name,float tailLength) {
        this.tailLength = tailLength;
    }

    public float getTailLength() {
        return tailLength;
    }

    public void setTailLength(float tailLength) {
        this.tailLength = tailLength;
    }
}
