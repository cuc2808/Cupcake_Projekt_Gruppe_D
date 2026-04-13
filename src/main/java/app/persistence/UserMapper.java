package app.persistence;


import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User login(String username, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where username =? and password =?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("user_id");
                double balance = rs.getDouble("balance");
                boolean administrator = rs.getBoolean("administrator");

                return new User(id, username, password, balance, administrator);
            } else {
                throw new DatabaseException("Error with login");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Connection to db dosen't work", e.getMessage());
        }
    }

    public static User createUser(String username, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (username, password) VALUES(?,?)";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return login(username,password,connectionPool);//Auto logger
        } catch (SQLException e) {
            throw new DatabaseException("Error with createUser", e.getMessage());
        }
    }

    public static void updateBalance(int id, double newSaldo, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, newSaldo);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with updateBalanceAfterPurchase", e.getMessage());
        }
    }

    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                ){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                double bal = rs.getDouble("balance");
                boolean isAdmin = rs.getBoolean("administrator");
                userList.add(new User(id, username, "", bal, isAdmin));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error with finding users", e.getMessage());
        }
        return userList;
    }
}