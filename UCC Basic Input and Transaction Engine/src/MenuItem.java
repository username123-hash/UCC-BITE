/**
 * MenuItem - one food item, i.e. one row of menu.csv.
 *
 * OWNER      : Ampater
 * CONTRACT   : Section 8.1 (class contract), Section 7.1 (menu.csv format)
 * DEPENDS ON : nothing
 * USED BY    : Order, CsvManager, ConsoleUI, Main
 *
 * PURPOSE
 *   Immutable object (no setters) that never changes after it is created.
 *   Section 8.1 "Purpose".
 *
 * PRIVATE FIELDS (Section 8.1)
 *   int id; String name; double price; boolean available;
 *
 * PUBLIC API (frozen signatures, Section 8.1)
 *   public MenuItem(int id, String name, double price, boolean available)
 *   public int getId()
 *   public String getName()
 *   public double getPrice()
 *   public boolean isAvailable()
 *   public String toString()
 *
 * BEHAVIOR (Section 8.1 "Behavior")
 *   - Constructor trims the name.
 *   - Throws IllegalArgumentException if: id <= 0, name null/blank,
 *     name has a comma or line break, price <= 0. (Section 6: clear message)
 *   - toString = short one-line debug string (id, name, price, availability).
 *     Not used for screen layout.
 *
 * MUST NOT (Section 8.1): read files, print anything, know about Order or the queue.
 *
 * SELF-TEST (Section 8.1): valid item builds; each invalid argument throws;
 *   " Fried Chicken " is stored as "Fried Chicken".
 *
 * ALSO SEE: Section 6 (global conventions), Section 11 (skeleton first),
 *   Section 14 (what you must be able to explain).
 */
public class MenuItem {

    // ------------------------------------------------------------------
    // PRIVATE FIELDS (Section 8.1). All private; read them through getters.
    // ------------------------------------------------------------------
    private int id;
    private String name;
    private double price;
    private boolean available;

    /**
     * Creates one menu item (one row of menu.csv).
     *
     * HOW IT MUST BEHAVE
     *   1. Check id: if id <= 0, throw IllegalArgumentException("...clear message...").
     *   2. Check name: if null or blank, throw IllegalArgumentException.
     *   3. Trim the name (" Fried Chicken " becomes "Fried Chicken"), THEN check
     *      that it has no comma (',') and no line break ('\n' or '\r');
     *      if it does, throw IllegalArgumentException.
     *   4. Check price: if price <= 0, throw IllegalArgumentException.
     *   5. Only when everything is valid, store the four values in the fields.
     *
     * @param id        whole number greater than 0 (unique across the menu)
     * @param name      item name; stored trimmed
     * @param price     greater than 0
     * @param available true if the item can be ordered, false if SOLD OUT
     * @throws IllegalArgumentException if any argument breaks the rules above
     */
    public MenuItem(int id, String name, double price, boolean available) {
        // TODO (Ampater): validate the arguments, then assign the fields.
    }

    /**
     * @return the item's id (the number the customer types when ordering)
     */
    public int getId() {
        // TODO (Ampater): return the id field.
        return 0;
    }

    /**
     * @return the trimmed item name
     */
    public String getName() {
        // TODO (Ampater): return the name field.
        return null;
    }

    /**
     * @return the price (greater than 0). Display formatting is ConsoleUI's job.
     */
    public double getPrice() {
        // TODO (Ampater): return the price field.
        return 0.0;
    }

    /**
     * @return true if the item can be ordered, false if it is sold out
     */
    public boolean isAvailable() {
        // TODO (Ampater): return the available field.
        return false;
    }

    /**
     * Short one-line DEBUG text (id, name, price, availability), for example
     * "MenuItem{id=1, name=Siomai Rice, price=45.00, available=true}".
     * It is NOT used for screen layout (ConsoleUI does that).
     *
     * @return a one-line description of this item
     */
    public String toString() {
        // TODO (Ampater): build and return the debug string.
        return "";
    }
}
