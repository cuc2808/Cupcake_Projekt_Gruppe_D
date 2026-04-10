package app.controllers;

import app.entities.Bottom;
import app.entities.OrderLine;
import app.entities.Top;
import app.entities.Cupcake;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // Show site where you "build" cupcake
        app.get("/cupcake", ctx -> showCupcakePage(ctx, connectionPool));

        // Handle when customer presses add to cart
        app.post("/add-to-cart", ctx -> addToCart(ctx));

        // Handle payment/order
        app.post("/checkout", ctx -> checkout(ctx, connectionPool));

        // Fjern en linje fra kurven
        app.post("/cart/remove", ctx -> removeFromCart(ctx));

        app.get("/cart", ctx -> showCart(ctx));
    }


    public static void showCupcakePage(Context ctx, ConnectionPool connectionPool) {
        List<Top> tops = ctx.sessionAttribute("topList");
        List<Bottom> bottoms = ctx.sessionAttribute("bottomList");

        ctx.attribute("tops", tops);
        ctx.attribute("bottoms", bottoms);
        ctx.render("cupcake.html");
    }



    public static void addToCart(Context ctx) {
        try {
            int topId = Integer.parseInt(ctx.formParam("topselect"));
            int bottomId = Integer.parseInt(ctx.formParam("bottomselect"));
            int amount = Integer.parseInt(ctx.formParam("amount"));

            List<Top> tops = ctx.sessionAttribute("topList");
            List<Bottom> bottoms = ctx.sessionAttribute("bottomList");

            Top selectedTop = null;
            for (Top t : tops) {
                if (t.getTopId() == topId) { selectedTop = t; break; }
            }

            Bottom selectedBottom = null;
            for (Bottom b : bottoms) {
                if (b.getBottomId() == bottomId) { selectedBottom = b; break; }
            }

            if (selectedTop != null && selectedBottom != null) {
                Cupcake cupcake = new Cupcake("", selectedTop, selectedBottom);
                OrderLine orderLine = new OrderLine(0, cupcake, amount, cupcake.getPrice() * amount);

                List<OrderLine> cart = ctx.sessionAttribute("cart");
                if (cart == null) {
                    cart = new ArrayList<>();
                }

                cart.add(orderLine);
                ctx.sessionAttribute("cart", cart);

                ctx.redirect("/cupcake");
            } else {
                ctx.attribute("msg", "Valgt top eller bund findes ikke.");
                ctx.render("error.html");
            }
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