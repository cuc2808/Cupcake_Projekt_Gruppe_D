package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
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

    public static void createOrderLine(OrderLine orderLine, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orderlines (order_id, cupcake_name, amount, total_price, price_per) VALUES(?,?,?,?,?)";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setInt(1, orderLine.getOrderId());
            ps.setString(2, orderLine.getCupcakeName());
            ps.setInt(3, orderLine.getAmount());
            ps.setDouble(4, orderLine.getTotalPrice());
            ps.setDouble(5, orderLine.getPricePer());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with createOrderLine", e.getMessage());
        }
    }

    public static List<OrderLine> getOrderlines(ConnectionPool connectionPool, Context ctx) throws DatabaseException {
        List<OrderLine> orderLines = new ArrayList<>();

        Order order = getCurrentOrder(connectionPool, ctx);
        int id = order.getOrderId();
        String sql = "SELECT * FROM orderlines WHERE order_id = " + id;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String cupcakeName = rs.getString("cupcake_name");
                int amount = rs.getInt("amount");
                String totalPrice = rs.getString("total_price");
                double pricePer = rs.getDouble("price_per");
                int orderLineId = rs.getInt("orderline_id");
                orderLines.add(new OrderLine(orderId, cupcakeName, pricePer, amount, orderLineId));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af brugerens ordre", e.getMessage());
        }
        return orderLines;
    }



    public static void deleteOrder(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sqlOrderLine = "DELETE FROM orderlines WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlOrderLine)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with deleteOrderline", e.getMessage());
        }

        String sqlOrder = "DELETE FROM orders WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlOrder)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with deleteOrder", e.getMessage());
        }
    }

    public static void deleteOrderLine(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orderlines WHERE orderline_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with deleteOrderLine", e.getMessage());
        }
    }

    public static void completeOrder(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET status = ?, date = ? WHERE order_id = ?";
        LocalDate today = LocalDate.now();
        Date date = Date.valueOf(today);

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "complete");
            ps.setDate(2, date);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with editOrder", e.getMessage());
        }
    }

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
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

    public static List<Order> getAllOrdersFromUser(int id, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> allOrders = new ArrayList<>();
        String sql = "Select * from orders WHERE user_id = " + id;
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

    public static List<OrderLine> getAllOrderlines(ConnectionPool connectionPool) throws DatabaseException {
        List<OrderLine> orderLines = new ArrayList<>();

        String sql = "SELECT * FROM orderlines";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String cupcakeName = rs.getString("cupcake_name");
                int amount = rs.getInt("amount");
                String totalPrice = rs.getString("total_price");
                double pricePer = rs.getDouble("price_per");
                int orderLineId = rs.getInt("orderline_id");
                orderLines.add(new OrderLine(orderId, cupcakeName, pricePer, amount, orderLineId));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af brugerens orderlines", e.getMessage());
        }
        return orderLines;
    }
}
