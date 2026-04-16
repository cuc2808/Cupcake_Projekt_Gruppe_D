package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/admin", ctx -> adminPage(ctx, connectionPool));
        app.post("/deleteOrder", ctx -> deleteOrder(ctx, connectionPool));
        app.post("/updateUser", ctx -> updateUser(ctx, connectionPool));

    }

    public static void adminPage(Context ctx, ConnectionPool connectionPool) {
        try {
            List<User> getAllUsers = UserMapper.getAllUsers(connectionPool);
            List<Order> getAllOrders = OrderMapper.getAllOrders(connectionPool);
            List<OrderLine> getAllOrderlines = OrderMapper.getAllOrderlines(connectionPool);

            ctx.attribute("getAllUsers", getAllUsers);
            ctx.sessionAttribute("getAllUsers", getAllUsers);

            ctx.attribute("getAllOrders", getAllOrders);
            ctx.sessionAttribute("getAllOrders", getAllOrders);

            ctx.attribute("getAllOrderlines", getAllOrderlines);
            ctx.sessionAttribute("getAllOrderlines", getAllOrderlines);

            ctx.render("admin.html");
        } catch (DatabaseException e) {
            ctx.attribute("msg", e);
            ctx.render("error.html");
        }
    }

    public static void updateUser (Context ctx, ConnectionPool connectionPool) {
        try {
            int userId = Integer.parseInt(ctx.formParam("userId"));
            double balance = Double.parseDouble(ctx.formParam("balance"));
            boolean isAdmin = Boolean.parseBoolean(ctx.formParam("administrator"));
            UserMapper.updateUserInfo(userId, balance, isAdmin, connectionPool);

            User currentUser = ctx.sessionAttribute("currentUser");
            if(userId == currentUser.getUserId()) {
                String username = currentUser.getUsername();
                String password = currentUser.getPassword();

                User user = new User(userId, username, password, balance, isAdmin);

                ctx.sessionAttribute("currentUser", user);
            }

            ctx.redirect("/admin");

        } catch (DatabaseException e) {
            ctx.attribute("msg", "Failed to update user: " + e.getMessage());
            ctx.render("error.html");
        }
    }

    public static void deleteOrder(Context ctx, ConnectionPool connectionPool) {
        int id = Integer.parseInt(ctx.formParam("deleteOrderId"));
        try {
            OrderMapper.deleteOrder(id,connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        ctx.redirect("/admin");
    }
}
