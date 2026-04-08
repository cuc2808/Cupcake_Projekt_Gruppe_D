package app.entities;

public class Top {

    private int topId;
    private String topName;
    private String description;
    private double price;

    public Top(int topId, String topName, String description, double price) {
        this.topId = topId;
        this.topName = topName;
        this.description = description;
        this.price = price;
    }

    public Top(String topName, String description, double price) {
        this.topName = topName;
        this.description = description;
        this.price = price;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
