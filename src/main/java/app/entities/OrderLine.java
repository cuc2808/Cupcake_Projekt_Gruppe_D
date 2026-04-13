package app.entities;

public class OrderLine {

    private int orderId;
    private String cupcakeName;
    private int amount;
    private double totalPrice;
    private double pricePer;
    private int orderLineId;


    public OrderLine(int orderId, Cupcake cupcake, int amount) {
        this.orderId = orderId;
        this.cupcakeName = cupcake.getCupcakeName();
        this.amount = amount;
        this.totalPrice = cupcake.getPrice() * amount;
    }

    public OrderLine(int orderId, String cupcakeName, double pricePer, int amount, int orderLineId) {
        this.orderId = orderId;
        this.cupcakeName = cupcakeName;
        this.amount = amount;
        this.pricePer = pricePer;
        this.totalPrice = pricePer * amount;
        this.orderLineId = orderLineId;
    }

    public OrderLine(int orderId, String cupcakeName, double pricePer, int amount) {
        this.orderId = orderId;
        this.cupcakeName = cupcakeName;
        this.amount = amount;
        this.pricePer = pricePer;
        this.totalPrice = pricePer * amount;
    }

    public int getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public double getPricePer() {
        return pricePer;
    }

    public void setPricePer(double pricePer) {
        this.pricePer = pricePer;
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
