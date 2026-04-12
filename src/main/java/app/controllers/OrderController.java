package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // Show site where you "build" cupcake
        app.get("/order", ctx -> orderPage(ctx, connectionPool));

        // Handle when customer presses add to cart
        app.post("/add_to_cart", ctx -> addToCart(ctx, connectionPool));

        // Handle payment/order
        app.post("/checkout", ctx -> checkout(ctx, connectionPool));

        app.post("/cart/remove", ctx -> removeFromCart(ctx));

        app.get("/cart", ctx -> showCart(ctx));
    }

    public static void orderChecker(Context ctx, ConnectionPool connectionPool) {
        Order incompleteOrder = null;

        try {
            List<Order> userOrders = OrderMapper.getAllUsersOrders(connectionPool, ctx);
            for (Order userOrder : userOrders) {
                if (userOrder.getStatus().equalsIgnoreCase("draft")) {
                    incompleteOrder = userOrder;
                }
            }
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        if (incompleteOrder == null) {
            User user = ctx.sessionAttribute("currentUser");
            int userId = user.getUserId();
            Order order = new Order(userId, null, "draft");
            try {
                OrderMapper.createOrder(order, connectionPool);
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void orderPage(Context ctx, ConnectionPool connectionPool) {
        orderChecker(ctx, connectionPool);

        List<Top> tops;
        try {
            tops = OrderMapper.getAllTops(connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        List<Bottom> bottoms;
        try {
            bottoms = OrderMapper.getAllBottoms(connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException();
        }

        ctx.attribute("tops", tops);
        ctx.attribute("bottoms", bottoms);

        ctx.sessionAttribute("tops", tops);
        ctx.sessionAttribute("bottoms", bottoms);
        ctx.render("order.html");
    }


    public static void addToCart(Context ctx, ConnectionPool connectionPool) {
        try {
            int topId = Integer.parseInt(ctx.formParam("topselect"));
            int bottomId = Integer.parseInt(ctx.formParam("bottomselect"));
            int amount = Integer.parseInt(ctx.formParam("amount"));

            List<Top> tops = ctx.sessionAttribute("tops");
            List<Bottom> bottoms = ctx.sessionAttribute("bottoms");

            Top selectedTop = null;
            for (Top t : tops) {
                if (t.getTopId() == topId) {
                    selectedTop = t;
                    break;
                }
            }

            Bottom selectedBottom = null;
            for (Bottom b : bottoms) {
                if (b.getBottomId() == bottomId) {
                    selectedBottom = b;
                    break;
                }
            }

            Cupcake cupcake = new Cupcake(selectedTop, selectedBottom);
            int currentOrderId = OrderMapper.getCurrentOrder(connectionPool, ctx).getOrderId();
            OrderLine orderLine = new OrderLine(currentOrderId, cupcake, amount);

            System.out.println(orderLine.getOrderId());
            System.out.println(orderLine.getCupcakeName());
            System.out.println(orderLine.getAmount());
            System.out.println(orderLine.getTotalPrice());

            ctx.redirect("/cart");
        } catch (Exception e) {
            ctx.attribute("msg", "Der skete en fejl ved tilføjelse til kurv: " + e.getMessage());
            ctx.render("error.html");
        }
    }


    public static void checkout(Context ctx, ConnectionPool connectionPool) {
        try {
            List<OrderLine> cart = ctx.sessionAttribute("cart");
            if (cart == null || cart.isEmpty()) {
                ctx.attribute("msg", "Kurven er tom!");
                ctx.render("error.html");
                return;
            }

            ctx.sessionAttribute("cart", new ArrayList<OrderLine>());

            ctx.attribute("msg", "Tak for din bestilling!");
            ctx.render("confirmation.html");
        } catch (Exception e) {
            ctx.attribute("msg", "Der skete en fejl under checkout: " + e.getMessage());
            ctx.render("error.html");
        }
    }

    public static void showCart(Context ctx) {
        List<OrderLine> cart = ctx.sessionAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        double total = 0;
        for (OrderLine line : cart) {
            total += line.getTotalPrice();
        }

        ctx.attribute("cart", cart);
        ctx.attribute("total", total);

        ctx.render("cart.html");
    }

    public static void removeFromCart(Context ctx) {
        try {
            int index = Integer.parseInt(ctx.formParam("index")); // index af linjen i listen
            List<OrderLine> cart = ctx.sessionAttribute("cart");
            if (cart != null && index >= 0 && index < cart.size()) {
                cart.remove(index);
                ctx.sessionAttribute("cart", cart);
            }
            ctx.redirect("/cart"); // gå tilbage til kurven
        } catch (Exception e) {
            ctx.attribute("msg", "Fejl ved fjernelse: " + e.getMessage());
            ctx.render("error.html");
        }
    }
}