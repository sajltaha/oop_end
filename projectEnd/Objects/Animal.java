package Objects;

public class Animal {
    private int id;
    private int cageId;
    private String name;
    private boolean isPredator;

    public Animal() {
    }

    public int getId() {
        return id;
    }

    public int getCageId() {
        return cageId;
    }

    public String getName() {
        return name;
    }

    public boolean isPredator() {
        return isPredator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCageId(int cageId) {
        this.cageId = cageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPredator(boolean predator) {
        isPredator = predator;
    }

    public String toString() {
        return name + " (" + (isPredator ? "Predator" : "Non-Predator") + ")";
    }

    public Animal(int cageId, String name, boolean isPredator) {
        this.cageId = cageId;
        this.name = name;
        this.isPredator = isPredator;
    }

    public Animal(int id, int cageId, String name, boolean isPredator) {
        this.id = id;
        this.cageId = cageId;
        this.name = name;
        this.isPredator = isPredator;
    }
}