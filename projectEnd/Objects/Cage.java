package Objects;

public class Cage {
    private int id;
    private int zooId;
    private int number;
    private int size;
    private int maxAnimals;
    private int currentAnimals;

    public Cage() {
    }

    public int getId() {
        return id;
    }

    public int getZooId() {
        return zooId;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public int getMaxAnimals() {
        return maxAnimals;
    }

    public int getCurrentAnimals() {
        return currentAnimals;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZooId(int zooId) {
        this.zooId = zooId;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setMaxAnimals(int maxAnimals) {
        this.maxAnimals = maxAnimals;
    }

    public void setCurrentAnimals(int currentAnimals) {
        this.currentAnimals = currentAnimals;
    }

    public String toString() {
        return "Cage " + number + " (Current/Max Animals: " + currentAnimals + "/" + maxAnimals + ")";
    }

    public Cage(int zooId, int number, int size, int maxAnimals) {
        this.zooId = zooId;
        this.number = number;
        this.size = size;
        this.maxAnimals = maxAnimals;
        this.currentAnimals = 0;
    }

    public Cage(int id, int zooId, int number, int size, int maxAnimals, int currentAnimals) {
        this.id = id;
        this.zooId = zooId;
        this.number = number;
        this.size = size;
        this.maxAnimals = maxAnimals;
        this.currentAnimals = currentAnimals;
    }
}