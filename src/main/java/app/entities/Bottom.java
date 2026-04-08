package app.entities;

public class Bottom {

    private int bottomId;
    private String bottomName;
    private String description;
    private double price;

    public Bottom(int bottomId, String bottomName, String description, double price) {
        this.bottomId = bottomId;
        this.bottomName = bottomName;
        this.description = description;
        this.price = price;
    }

    public Bottom(String bottomName, String description, double price) {
        this.bottomName = bottomName;
        this.description = description;
        this.price = price;
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public String getBottomName() {
        return bottomName;
    }

    public void setBottomName(String bottomName) {
        this.bottomName = bottomName;
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
