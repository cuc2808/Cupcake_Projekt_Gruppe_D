package app.controllers;

import app.entities.Order;
import app.entities.OrderLine;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/createUser", ctx -> ctx.render("login.html"));
        app.post("/createUser", ctx -> registerUser(ctx, connectionPool));
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));
        app.get("/logout", ctx -> logout(ctx));
        app.get("/valid_user", ctx -> isLoggedIn(ctx));
        app.get("/profile", ctx -> myProfile(ctx, connectionPool));

    }

    public static void registerUser(Context ctx, ConnectionPool connectionPool) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {
            User user = UserMapper.createUser(username, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.render("index.html");
        } catch (DatabaseException e) {
            ctx.attribute("msg", e.getMessage());
            ctx.render("registrerbruger.html");
        }
    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {
            User user = UserMapper.login(username, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.render("index.html");
        } catch (DatabaseException e) {
            ctx.attribute("msg", e.getMessage());
            ctx.render("login.html");
        }
    }

    public static void isLoggedIn(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.redirect("/");
        }
    }

    public static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void myProfile(Context ctx, ConnectionPool connectionPool) {
        isLoggedIn(ctx);
        User user = ctx.sessionAttribute("currentUser");
        System.out.println(user.getUserId());

        try {
            List<Order> getAllUsersOrders = OrderMapper.getAllUsersOrders(connectionPool, ctx);
            List<OrderLine> getAllUsersOrderLines = OrderMapper.getOrderlines(connectionPool, ctx);

            ctx.attribute("getAllUsersOrders", getAllUsersOrders);
            ctx.sessionAttribute("getAllUsersOrders", getAllUsersOrders);

            ctx.attribute("getAllUsersOrderlines", getAllUsersOrderLines);
            ctx.sessionAttribute("getAllUsersOrderlines", getAllUsersOrderLines);

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.redirect("myprofile.html");

    }
}
