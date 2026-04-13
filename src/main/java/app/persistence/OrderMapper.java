package app.persistence;

import app.entities.Bottom;
import app.entities.Order;
import app.entities.Top;
import app.entities.User;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

    public static List<Order> getAllUsersOrders(ConnectionPool connectionPool, Context ctx) throws DatabaseException {
        List<Order> orders = new ArrayList<>();
        User user = ctx.sessionAttribute("currentUser");
        int id = user.getUserId();
        String sql = "SELECT * FROM orders WHERE user_id = " + id;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Date date = rs.getDate("date");
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");
                orders.add(new Order(orderId, date, userId, status));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af brugerens ordre", e.getMessage());
        }
        return orders;

    }

    public static Order getCurrentOrder(ConnectionPool connectionPool, Context ctx) throws DatabaseException {
        Order currentOrder = null;
        List<Order> orders = getAllUsersOrders(connectionPool, ctx);

        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase("draft")) {
                currentOrder = order;
            }
        }

        return currentOrder;
    }

    public static void createOrder(Order order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id, status) VALUES(?,?)";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setInt(1, order.getUserId());
            ps.setString(2, "draft");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with createOrder", e.getMessage());
        }
    }

    public static List<Order> getAllOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> allOrders = new ArrayList<>();
        String sql = "Select * from orders";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Date orderDate = rs.getDate("date");
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");
                allOrders.add(new Order(orderId, orderDate, userId, status));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error with getting all orders", e.getMessage());
        }
        return allOrders;
    }
}
