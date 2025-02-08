package RAR;

import Objects.Zoo;
import DBConnection.DBConnection_inner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZooRAR {

    public void addZoo(Zoo zoo) throws SQLException {
        String sql = "INSERT INTO Zoo (name) VALUES (?)";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, zoo.getName());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    zoo.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Zoo getZooById(int zooId) throws SQLException {
        String sql = "SELECT * FROM Zoo WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zooId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Zoo(
                            rs.getInt("id"),
                            rs.getString("name"));
                }
            }
        }
        return null;
    }

    public List<Zoo> getAllZoos() throws SQLException {
        List<Zoo> zoos = new ArrayList<>();
        String sql = "SELECT * FROM Zoo";
        try (Connection conn = DBConnection_inner.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Zoo zoo = new Zoo(
                        rs.getInt("id"),
                        rs.getString("name"));
                zoos.add(zoo);
            }
        }
        return zoos;
    }

    public void updateZoo(Zoo zoo) throws SQLException {
        String sql = "UPDATE Zoo SET name = ? WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, zoo.getName());
            stmt.setInt(2, zoo.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteZoo(int zooId) throws SQLException {
        String sql = "DELETE FROM Zoo WHERE id = ?";
        try (Connection conn = DBConnection_inner.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zooId);
            stmt.executeUpdate();
        }
    }
}