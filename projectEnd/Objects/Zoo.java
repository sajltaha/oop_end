package Objects;

public class Zoo {
    private int id;
    private String name;

    public Zoo() {
    }

    public Zoo(String name) {
        this.name = name;
    }

    public Zoo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}