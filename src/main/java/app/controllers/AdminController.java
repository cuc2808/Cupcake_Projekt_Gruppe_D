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
        app.get("/getAllOrders", ctx -> getAllOrders(ctx,connectionPool));


    }

    public static void getAllUsers (Context ctx, ConnectionPool connectionPool) {
        List<User> allUsers;

         try {
            allUsers = UserMapper.getAllUsers(ctx, connectionPool);
             System.out.println("Antal brugere fundet: " + allUsers.size());
             ctx.attribute("allUsers", allUsers);
             ctx.render("admin.html");
        } catch (DatabaseException e){
            ctx.attribute("msg",e);
            ctx.render("error.html");
        }

    }

    public static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Order> getAllOrders = new ArrayList<>(OrderMapper.getAllOrders(ctx, connectionPool));
            if (getAllOrders == null || getAllOrders.isEmpty()) {
                ctx.attribute("msg", "No Orders yet");
                return;
            }
            ctx.attribute("getAllOrders",getAllOrders);
            ctx.render("admin.html");
        }catch (DatabaseException e){
            ctx.attribute("msg",e);
            ctx.render("error.html");
        }

    }

}
