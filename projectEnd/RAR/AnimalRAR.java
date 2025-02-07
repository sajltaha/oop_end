package RAR;

import Objects.Animal;
import DBConnection.DBConnection_inner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalRAR {

    public void addAnimal(Animal animal) throws SQLException {
        String sql = "INSERT INTO Animal (cage_id, name, is_predator) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, animal.getCageId());
            stmt.setString(2, animal.getName());
            stmt.setBoolean(3, animal.isPredator());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    animal.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Animal> getAnimalsByCageId(int cageId) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM Animal WHERE cage_id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cageId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Animal animal = new Animal(
                            rs.getInt("id"),
                            rs.getInt("cage_id"),
                            rs.getString("name"),
                            rs.getBoolean("is_predator"));
                    animals.add(animal);
                }
            }
        }
        return animals;
    }
    
    public List<Animal> searchAnimalsByNameInZoo(String name, int zooId) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT a.* FROM Animal a JOIN Cage c ON a.cage_id = c.id WHERE a.name LIKE ? AND c.zoo_id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            stmt.setInt(2, zooId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Animal animal = new Animal(
                            rs.getInt("id"),
                            rs.getInt("cage_id"),
                            rs.getString("name"),
                            rs.getBoolean("is_predator"));
                    animals.add(animal);
                }
            }
        }
        return animals;
    }
    
    public List<Animal> getAllAnimals() throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM Animal";
        try (Connection conn = DBConnection_inner.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Animal animal = new Animal(
                        rs.getInt("id"),
                        rs.getInt("cage_id"),
                        rs.getString("name"),
                        rs.getBoolean("is_predator"));
                animals.add(animal);
            }
        }
        return animals;
    }
    
    public List<Animal> getAnimalsByZooId(int zooId) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT a.* FROM Animal a JOIN Cage c ON a.cage_id = c.id WHERE c.zoo_id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zooId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Animal animal = new Animal(
                            rs.getInt("id"),
                            rs.getInt("cage_id"),
                            rs.getString("name"),
                            rs.getBoolean("is_predator"));
                    animals.add(animal);
                }
            }
        }
        return animals;
    }
    
    public void updateAnimal(Animal animal) throws SQLException {
        String sql = "UPDATE Animal SET name = ?, is_predator = ?, cage_id = ? WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, animal.getName());
            stmt.setBoolean(2, animal.isPredator());
            stmt.setInt(3, animal.getCageId());
            stmt.setInt(4, animal.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteAnimal(int animalId) throws SQLException {
        String sql = "DELETE FROM Animal WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, animalId);
            stmt.executeUpdate();
        }
    }

    public List<Animal> searchAnimalsByName(String name, int zooId) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT a.* FROM Animal a JOIN Cage c ON a.cage_id = c.id WHERE a.name LIKE ? AND c.zoo_id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            stmt.setInt(2, zooId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Animal animal = new Animal(
                            rs.getInt("id"),
                            rs.getInt("cage_id"),
                            rs.getString("name"),
                            rs.getBoolean("is_predator"));
                    animals.add(animal);
                }
            }
        }
        return animals;
    }
}