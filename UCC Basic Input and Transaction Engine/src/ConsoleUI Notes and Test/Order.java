/**
 * TEST DOUBLE - a tiny stand-in for the real Order (Jontilano's file).
 * Same constructor shape as the contract's "restore" constructor and the
 * getters ConsoleUI needs, but NO validation.
 * This copy is only used inside this test folder.
 */
public class Order {
    public enum Status { PENDING, COMPLETED }

    private int orderId;
    private int itemId;
    private String itemName;
    private double unitPrice;
    private int quantity;
    private double total;
    private Status status;

    public Order(int orderId, int itemId, String itemName,
                 double unitPrice, int quantity, Status status) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.total = Math.round(unitPrice * quantity * 100) / 100.0;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
    public Status getStatus() { return status; }
}
