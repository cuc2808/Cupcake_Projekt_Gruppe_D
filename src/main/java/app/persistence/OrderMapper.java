package app.persistence;

import app.entities.Bottom;
import app.entities.Order;
import app.entities.Top;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Top> getAllTops(ConnectionPool connectionPool) throws DatabaseException {
        List<Top> topList = new ArrayList<>();
        String sql = "SELECT * FROM tops";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("top_id");
                String name = rs.getString("top_name");
                double price = rs.getDouble("price");
                topList.add(new Top(id, name, "", price));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af toppe", e.getMessage());
        }
        return topList;
    }

    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottomList = new ArrayList<>();
        String sql = "SELECT * FROM bottoms";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("bottom_id");
                String name = rs.getString("bottom_name");
                double price = rs.getDouble("price");
                bottomList.add(new Bottom(id, name, "", price));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af bunde", e.getMessage());
        }
        return bottomList;
    }
}
