public class Cat extends Animal {
    private String breed;
    private int cntLives;

    public Cat(int age, String name,String breed) {
        cntLives =9;
        this.breed = breed;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getCntLives() {
        return cntLives;
    }

    public void setCntLives(int cntLives) {
        this.cntLives = cntLives;
    }
}
