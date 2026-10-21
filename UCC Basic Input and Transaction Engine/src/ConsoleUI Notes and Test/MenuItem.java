/**
 * TEST DOUBLE - a tiny stand-in for the real MenuItem (Ampater's file).
 * It has the same constructor and getters ConsoleUI needs, but NO validation,
 * so the ConsoleUI tests do not depend on the teammates' files being finished.
 * This copy is only used inside this test folder.
 */
public class MenuItem {
    private int id;
    private String name;
    private double price;
    private boolean available;

    public MenuItem(int id, String name, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
}
