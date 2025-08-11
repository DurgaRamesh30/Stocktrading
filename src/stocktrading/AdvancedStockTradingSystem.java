package stocktrading;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class AdvancedStockTradingSystem {
    private Map<String, User> users;
    private Map<String, Double> marketPrices;
    private Scanner scanner;
    private static final String DATA_FILE = "trading_data.json";

    public AdvancedStockTradingSystem() {
        users = new HashMap<>();
        marketPrices = new HashMap<>();
        scanner = new Scanner(System.in);
        loadData();
        initializeMarketPrices();
    }

    private void initializeMarketPrices() {
        marketPrices.put("AAPL", 150.00);
        marketPrices.put("GOOGL", 2800.00);
        marketPrices.put("TSLA", 700.00);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Advanced Stock Trading System ===");
            System.out.println("1. Create Account");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Market Prices");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> buyStock();
                case 3 -> sellStock();
                case 4 -> viewPortfolio();
                case 5 -> viewMarketPrices();
                case 6 -> {
                    saveData();
                    System.out.println("Data saved. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        users.put(username, new User(username, password));
        System.out.println("Account created for " + username);
    }

    private User authenticateUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);

        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("Invalid username or password.");
            return null;
        }
        return user;
    }

    private void buyStock() {
        User user = authenticateUser();
        if (user == null) return;

        viewMarketPrices();
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!marketPrices.containsKey(symbol)) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        double price = marketPrices.get(symbol) * quantity;

        if (user.withdraw(price)) {
            user.getPortfolio().put(symbol, user.getPortfolio().getOrDefault(symbol, 0) + quantity);
            System.out.println("Bought " + quantity + " shares of " + symbol);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    private void sellStock() {
        User user = authenticateUser();
        if (user == null) return;

        System.out.println("\n=== Portfolio for " + user.getUsername() + " ===");
        System.out.println("Balance: $" + user.getBalance());
        for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " shares");
        }

        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!user.getPortfolio().containsKey(symbol)) {
            System.out.println("You don't own this stock.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        int owned = user.getPortfolio().get(symbol);
        if (quantity > owned) {
            System.out.println("You don't have enough shares.");
            return;
        }

        double price = marketPrices.get(symbol) * quantity;
        user.deposit(price);

        if (quantity == owned) {
            user.getPortfolio().remove(symbol);
        } else {
            user.getPortfolio().put(symbol, owned - quantity);
        }

        System.out.println("Sold " + quantity + " shares of " + symbol);
    }

    private void viewPortfolio() {
        User user = authenticateUser();
        if (user == null) return;

        System.out.println("\n=== Portfolio for " + user.getUsername() + " ===");
        System.out.println("Balance: $" + user.getBalance());
        for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " shares");
        }
    }

    private void viewMarketPrices() {
        System.out.println("\n=== Market Prices ===");
        for (Map.Entry<String, Double> entry : marketPrices.entrySet()) {
            System.out.println(entry.getKey() + ": $" + (entry.getValue() + Math.random()));
        }
    }

    private void saveData() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            Gson gson = new Gson();
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try (Reader reader = new FileReader(DATA_FILE)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, User>>() {}.getType();
            users = gson.fromJson(reader, type);
            if (users == null) {
                users = new HashMap<>();
            }
        } catch (IOException e) {
            users = new HashMap<>();
        }
    }

    public static void main(String[] args) {
        new AdvancedStockTradingSystem().start();
    }
}
