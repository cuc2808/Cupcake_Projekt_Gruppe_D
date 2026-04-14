package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/order", ctx -> orderPage(ctx, connectionPool));
        app.post("/add_to_cart", ctx -> addToCart(ctx, connectionPool));
        app.post("/checkout", ctx -> checkout(ctx, connectionPool));
        app.post("/remove_from_cart", ctx -> removeFromCart(ctx, connectionPool));
        app.get("/cart", ctx -> showCart(ctx, connectionPool));
    }

    public static void orderChecker(Context ctx, ConnectionPool connectionPool) {
        if(ctx.sessionAttribute("currentUser") != null) {
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
        } else {
            ctx.redirect("/");
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
            double pricePer = cupcake.getPrice();
            OrderLine orderLine = new OrderLine(currentOrderId, cupcake.getCupcakeName(), pricePer, amount);

            OrderMapper.createOrderLine(orderLine, connectionPool);

            ctx.redirect("/cart");
        } catch (Exception e) {
            ctx.attribute("msg", "Der skete en fejl ved tilføjelse til kurv: " + e.getMessage());
            ctx.render("error.html");
        }
    }


    public static void checkout(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        User user = ctx.sessionAttribute("currentUser");
        Order order = null;
        double totalPrice = 0;
        List<OrderLine> orderLines = OrderMapper.getOrderlines(connectionPool,ctx);
        for (OrderLine orderLine : orderLines) {
            totalPrice += orderLine.getTotalPrice();
        }

        double currentBalance = user.getBalance();
        System.out.println(currentBalance);
        double newBalance = currentBalance - totalPrice;
        UserMapper.updateBalance(user.getUserId(),newBalance,connectionPool);
        user.setBalance(newBalance);
        ctx.sessionAttribute("currentUser",user);

        try {
            order = OrderMapper.getCurrentOrder(connectionPool,ctx);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        int id = order.getOrderId();
        try {
            OrderMapper.completeOrder(id,connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.render("checkout.html");
    }

    public static void showCart(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        if(ctx.sessionAttribute("currentUser") != null) {
            orderChecker(ctx, connectionPool);
            List<OrderLine> orderLines = OrderMapper.getOrderlines(connectionPool, ctx);

            double totalPrice = 0;
            for (OrderLine orderLine : orderLines) {
                totalPrice += orderLine.getTotalPrice();
            }

            User user = ctx.sessionAttribute("currentUser");
            double balance = user.getBalance();
            double newSaldo = balance - totalPrice;

            ctx.sessionAttribute("newSaldo", newSaldo);
            ctx.sessionAttribute("userBalance", balance);
            ctx.sessionAttribute("totalPrice", totalPrice);
            ctx.sessionAttribute("orderLinesList", orderLines);

            ctx.attribute("newSaldo", newSaldo);
            ctx.attribute("userBalance", balance);
            ctx.attribute("totalPrice", totalPrice);
            ctx.attribute("orderLinesList", orderLines);
            ctx.render("cart.html");
        } else  {
            ctx.redirect("/");
        }
    }

    public static void removeFromCart(Context ctx, ConnectionPool connectionPool) {
        int orderLineId = Integer.parseInt(ctx.formParam("selectOrderLineId"));
        try {
            OrderMapper.deleteOrderLine(orderLineId, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        ctx.redirect("/cart");
    }
}