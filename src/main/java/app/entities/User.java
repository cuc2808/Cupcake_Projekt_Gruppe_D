package app.entities;

public class User {

    private int userId;
    private String username;
    private String password;
    private double balance;
    private boolean administrator;

    public User(int userId, String username, String password, double balance, boolean administrator) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.administrator = administrator;
    }

    public User(String username, String password, double balance, boolean administrator) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.administrator = administrator;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}
