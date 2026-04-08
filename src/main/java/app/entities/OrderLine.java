package app.entities;

public class OrderLine {

    private int orderId;
    private String cupcakeName;
    private int amount;
    private double totalPrice;

    public OrderLine(int orderId, Cupcake cupcake, int amount, double totalPrice) {
        this.orderId = orderId;
        this.cupcakeName = cupcake.getCupcakeName();
        this.amount = amount;
        this.totalPrice = cupcake.getPrice() * amount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCupcakeName() {
        return cupcakeName;
    }

    public void setCupcakeName(String cupcakeName) {
        this.cupcakeName = cupcakeName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
