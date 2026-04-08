package app.entities;

public class Cupcake {

    private String cupcakeName;
    private Top topType;
    private Bottom bottomType;
    private double price;

    public Cupcake(String cupcakeName, Top topType, Bottom bottomType) {
        this.cupcakeName = bottomType.getBottomName() + " " + topType.getTopName();
        this.topType = topType;
        this.bottomType = bottomType;
        this.price = topType.getPrice() + bottomType.getPrice();
    }

    public String getCupcakeName() {
        return cupcakeName;
    }

    public void setCupcakeName(String cupcakeName) {
        this.cupcakeName = cupcakeName;
    }

    public Top getTopType() {
        return topType;
    }

    public void setTopType(Top topType) {
        this.topType = topType;
    }

    public Bottom getBottomType() {
        return bottomType;
    }

    public void setBottomType(Bottom bottomType) {
        this.bottomType = bottomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
