package stocktrading;

class Order {
    String stockSymbol;
    double price;
    int quantity;
    String orderType;  // "buy", "sell", "short", "cover", "limit"

    public Order(String stockSymbol, double price, int quantity, String orderType) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.quantity = quantity;
        this.orderType = orderType;
    }


}
