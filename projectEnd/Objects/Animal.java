package Objects;

public class Animal {

    private int cageId;
    private  String name;
    private boolean isPredator;
    private int id; // Add an ID field

    public Animal(int cageId, String name, boolean isPredator) {
        this.cageId = cageId;
        this.name = name;
        this.isPredator = isPredator;
    }
    // Constructor with ID (important for database operations)
    public Animal(int cageId, String name, boolean isPredator, int id) {
        this.cageId = cageId;
        this.name = name;
        this.isPredator = isPredator;
        this.id = id;
    }

    // Getters
    public int getCageId() {
        return cageId;
    }

    public String getName() {
        return name;
    }

    public boolean isPredator() {
        return isPredator;
    }
    public int getId(){
        return id;
    }

    // Setters
    public void setCageId(int cageId) {
        this.cageId = cageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPredator(boolean predator) {
        isPredator = predator;
    }
    public void setId(int id){
        this.id = id;
    }
}