package RAR;

import Objects.Cage;
import DBConnection.DBConnection_inner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CageRAR {

    public void addCage(Cage cage) throws SQLException {
        String sql = "INSERT INTO Cage (zoo_id, number, size, max_animals, current_animals) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cage.getZooId());
            stmt.setInt(2, cage.getNumber());
            stmt.setInt(3, cage.getSize());
            stmt.setInt(4, cage.getMaxAnimals());
            stmt.setInt(5, cage.getCurrentAnimals());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cage.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Cage getCageById(int cageId) throws SQLException {
        String sql = "SELECT * FROM Cage WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cage(
                            rs.getInt("id"),
                            rs.getInt("zoo_id"),
                            rs.getInt("number"),
                            rs.getInt("size"),
                            rs.getInt("max_animals"),
                            rs.getInt("current_animals"));
                }
            }
        }
        return null;
    }

    public List<Cage> getCagesByZooId(int zooId) throws SQLException {
        List<Cage> cages = new ArrayList<>();
        String sql = "SELECT * FROM Cage WHERE zoo_id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zooId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cage cage = new Cage(
                            rs.getInt("id"),
                            rs.getInt("zoo_id"),
                            rs.getInt("number"),
                            rs.getInt("size"),
                            rs.getInt("max_animals"),
                            rs.getInt("current_animals"));
                    cages.add(cage);
                }
            }
        }
        return cages;
    }

    public void incrementCurrentAnimals(int cageId) throws SQLException {
        String sql = "UPDATE Cage SET current_animals = current_animals + 1 WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cageId);
            stmt.executeUpdate();
        }
    }

    public void updateCage(Cage cage) throws SQLException {
        String sql = "UPDATE Cage SET number = ?, size = ?, max_animals = ? WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cage.getNumber());
            stmt.setInt(2, cage.getSize());
            stmt.setInt(3, cage.getMaxAnimals());
            stmt.setInt(4, cage.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteCage(int cageId) throws SQLException {
        String sql = "DELETE FROM Cage WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cageId);
            stmt.executeUpdate();
        }
    }

    public void decrementCurrentAnimals(int cageId) throws SQLException {
        String sql = "UPDATE Cage SET current_animals = current_animals - 1 WHERE id = ? AND current_animals > 0";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cageId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Current animals count is already zero for cage ID: " + cageId);
            }
        }
    }

    public List<Cage> getAllCages() throws SQLException {
        List<Cage> cages = new ArrayList<>();
        String sql = "SELECT * FROM Cage";
        try (Connection conn = DBConnection_inner.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cages.add(new Cage(
                        rs.getInt("id"),
                        rs.getInt("zoo_id"),
                        rs.getInt("number"),
                        rs.getInt("size"),
                        rs.getInt("max_animals"),
                        rs.getInt("current_animals")));
            }
        }
        return cages;
    }
    
    public boolean isCageAtMaxCapacity(int cageId) throws SQLException {
        String sql = "SELECT max_animals, current_animals FROM Cage WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int maxAnimals = rs.getInt("max_animals");
                    int currentAnimals = rs.getInt("current_animals");
                    return currentAnimals >= maxAnimals;
                }
            }
        }
        return true;
    }    
}

// package RAR;

// import Objects.Cage;
// import Objects.Zoo;

// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.ArrayList;
// import java.util.List;

// import DBConnection.DBConnection_inner;

// public class CageRAR {

//     public  void addCage(Cage cage) throws SQLException {
//         String sql = "INSERT INTO cage (zoo_id, number, size,max_animal) VALUES (?, ?, ?, ?)"; // Changed to 'cage'
//         try (PreparedStatement preparedStatement = DBConnection_inner.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//             preparedStatement.setInt(1, cage.getZooId());
//             preparedStatement.setInt(2, cage.getNumber());
//             preparedStatement.setInt(3, cage.getSize());
//             preparedStatement.setInt(4, cage.getMaxAnimal());
//             preparedStatement.executeUpdate();
//             // Get the generated ID (important for later operations)
//             try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
//                 if(generatedKeys.next()){
//                     cage.setId(generatedKeys.getInt(1));// Assuming 'setId' exists

//                 }
//                 else{
//                     throw new SQLException("Creating cage failed, no ID obtained.");
//                 }
//             }
//         }
//     }

//      public  void updateCage(Cage cage) throws SQLException{
//         String sql = "UPDATE cage SET number = ?, size = ?, max_animal = ? WHERE id = ?"; // Changed to 'cage'
//         try (PreparedStatement preparedStatement = DBConnection_inner.getConnection().prepareStatement(sql)) {
//             preparedStatement.setInt(1, cage.getNumber());
//             preparedStatement.setInt(2, cage.getSize());
//             preparedStatement.setInt(3,cage.getMaxAnimal());
//             preparedStatement.setInt(4, cage.getId()); // Use the ID for the WHERE clause
//             preparedStatement.executeUpdate();
//         }
//     }
//      public  void deleteCage(int cageId) throws  SQLException{
//          String sql = "DELETE FROM cage WHERE id = ?"; // Changed to 'cage'
//          try(PreparedStatement preparedStatement = DBConnection_inner.getConnection().prepareStatement(sql)){
//              preparedStatement.setInt(1,cageId);
//              preparedStatement.executeUpdate();
//          }
//      }

//     public List<Cage> getCagesByZooId(int zooId) throws SQLException {
//         List<Cage> cages = new ArrayList<>();
//         String sql = "SELECT * FROM cage WHERE zoo_id = ?"; // Changed to 'cage'
//         try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql)) {
//             pstmt.setInt(1, zooId);
//             try (ResultSet rs = pstmt.executeQuery()) {
//                 while (rs.next()) {
//                     // Use the constructor *with* the ID
//                     cages.add(new Cage(rs.getInt("zoo_id"), rs.getInt("number"), rs.getInt("size"), rs.getInt("max_animal"), rs.getInt("id")));
//                 }
//             }
//         }
//         return cages;
//     }


//     public Cage getCageById(int cageId) throws SQLException {
//         String sql = "SELECT * FROM cage WHERE id = ?"; // Changed to 'cage'
//         try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql)) {
//             pstmt.setInt(1, cageId);
//             try (ResultSet rs = pstmt.executeQuery()) {
//                 if (rs.next()) {
//                     // Use the constructor *with* the ID
//                     return new Cage(rs.getInt("zoo_id"), rs.getInt("number"), rs.getInt("size"), rs.getInt("max_animal"), rs.getInt("id"));
//                 }
//             }
//         }
//         return null; // Or throw an exception if the cage is not found
//     }


//     public Cage getCageByNumberAndZooId(int number, int zooId) throws SQLException {
//         String sql = "SELECT * FROM cage WHERE number = ? AND zoo_id = ?"; // Changed to 'cage'
//         try (PreparedStatement pstmt = DBConnection_inner.getConnection().prepareStatement(sql)) {
//             pstmt.setInt(1, number);
//             pstmt.setInt(2, zooId);
//             try (ResultSet rs = pstmt.executeQuery()) {
//                 if (rs.next()) {
//                     // Use the constructor *with* the ID
//                     return new Cage(rs.getInt("zoo_id"), rs.getInt("number"), rs.getInt("size"), rs.getInt("max_animal"), rs.getInt("id"));
//                 }
//             }
//         }
//         return null; // Or throw a custom exception
//     }
// }