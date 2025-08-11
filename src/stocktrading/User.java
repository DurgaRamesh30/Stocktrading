package stocktrading;
import java.util.HashMap;
import java.util.Map;
public class User {
    private String username;
    private String password;
    private double balance;
    private Map<String, Integer> portfolio;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 10000.0;
        this.portfolio = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public Map<String, Integer> getPortfolio() {
        return portfolio;
    }
}
