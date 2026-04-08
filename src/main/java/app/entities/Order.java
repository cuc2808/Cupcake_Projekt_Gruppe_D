package app.entities;

import java.util.Date;

public class Order {

    private int orderId;
    private Date date;
    private int userId;
    private String status;

    public Order(int orderId, Date date, int userId, String status) {
        this.orderId = orderId;
        this.date = date;
        this.userId = userId;
        this.status = status;
    }

    public Order(String status, int userId, Date date) {
        this.status = status;
        this.userId = userId;
        this.date = date;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
