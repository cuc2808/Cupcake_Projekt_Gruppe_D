package app.persistence;


import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static void createUser(String username, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (username, password) VALUES(?,?)";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error with createUser", e.getMessage());
        }
    }
}