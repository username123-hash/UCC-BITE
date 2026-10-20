/**
 * Order - one customer transaction, i.e. one row of orders.csv.
 *
 * OWNER      : Jontilano
 * CONTRACT   : Section 8.2 (class contract), Section 7.2 (orders.csv format)
 * DEPENDS ON : MenuItem
 * USED BY    : OrderQueue, CsvManager, ConsoleUI, Main
 *
 * PRIVATE FIELDS (Section 8.2)
 *   int orderId; int itemId; String itemName; double unitPrice;
 *   int quantity; double total; Status status (the only field that can change)
 *
 * PUBLIC API (frozen signatures, Section 8.2)
 *   public enum Status { PENDING, COMPLETED }      <- nested enum, Section 3
 *
 *   public Order(int orderId, MenuItem item, int quantity)
 *   public Order(int orderId, int itemId, String itemName,
 *                double unitPrice, int quantity, Status status)
 *
 *   public int getOrderId()
 *   public int getItemId()
 *   public String getItemName()
 *   public double getUnitPrice()
 *   public int getQuantity()
 *   public double getTotal()
 *   public Status getStatus()
 *   public boolean isPending()
 *   public void markCompleted()
 *   public String toString()
 *
 * BEHAVIOR (Section 8.2 "Behavior")
 *   - New-order constructor (used by Main): copies the item's id, name and
 *     price; total = unitPrice x quantity rounded to 2 decimals; status PENDING.
 *     Does NOT check availability (Main does). Throws IllegalArgumentException
 *     if orderId <= 0, item is null, or quantity <= 0.
 *   - Restore constructor (used ONLY by CsvManager): takes every stored field
 *     except total, recomputes total the same way, uses the given status.
 *     Validates: ids > 0, name not blank and no commas, price > 0,
 *     quantity > 0, status not null.
 *   - markCompleted(): PENDING -> COMPLETED. Already COMPLETED throws
 *     IllegalStateException.
 *   - isPending(): true when status is PENDING.
 *   - toString = one-line debug string, not screen formatting.
 *   - The max quantity (99) is a Main rule, not an Order rule (Section 8.2).
 *   - Why name and price are copied: Section 7.2 (old orders keep the price paid).
 *
 * MUST NOT (Section 8.2): read files, print anything, or keep a reference to the
 *   MenuItem after construction (copies of the values only).
 *
 * SELF-TEST (Section 8.2): 65.00 x 2 = 130.00; 19.99 x 3 = 59.97 (rounding);
 *   new order is PENDING; markCompleted twice throws; quantity 0 and -1 throw;
 *   null item throws.
 *
 * ALSO SEE: Section 6 (money = double, 2 decimals), Section 14 (explain-ready points).
 */
public class Order {

    // Nested enum - part of the frozen API (Section 8.2, Section 3).
    public enum Status { PENDING, COMPLETED }

    // ------------------------------------------------------------------
    // PRIVATE FIELDS (Section 8.2). Only "status" may change after creation.
    // ------------------------------------------------------------------
    private int orderId;
    private int itemId;
    private String itemName;
    private double unitPrice;
    private int quantity;
    private double total;
    private Status status;

    /**
     * NEW-ORDER constructor. Used by Main when a customer places an order.
     *
     * HOW IT MUST BEHAVE
     *   1. Throw IllegalArgumentException if orderId <= 0, item is null, or
     *      quantity <= 0.
     *   2. Copy item.getId(), item.getName() and item.getPrice() into this
     *      order's own fields. Do NOT keep the MenuItem itself (so a later menu
     *      price change cannot alter an old order - Section 7.2).
     *   3. total = unitPrice x quantity, rounded to 2 decimals
     *      (19.99 x 3 must give 59.97). Suggested: Math.round(x * 100) / 100.0
     *   4. status = Status.PENDING.
     *   5. Do NOT check availability and do NOT check the 99 limit (Main's job).
     *
     * @param orderId  unique order number, greater than 0
     * @param item     the menu item being ordered (not null)
     * @param quantity how many, greater than 0
     * @throws IllegalArgumentException if a rule above is broken
     */
    public Order(int orderId, MenuItem item, int quantity) {
        // TODO (Jontilano): validate, copy the item's values, compute total, set PENDING.
    }

    /**
     * RESTORE constructor. Used ONLY by CsvManager when reading orders.csv.
     *
     * HOW IT MUST BEHAVE
     *   1. Validate like the new-order constructor, plus: orderId > 0,
     *      itemId > 0, itemName not null/blank and without a comma,
     *      unitPrice > 0, quantity > 0, status not null.
     *      Any failure throws IllegalArgumentException with a clear message
     *      (CsvManager catches it and skips that CSV row).
     *   2. There is NO total parameter: compute total the same way as above
     *      (unitPrice x quantity, rounded to 2 decimals).
     *   3. Use the given status as-is (it may be PENDING or COMPLETED).
     *
     * @param orderId   order number, greater than 0
     * @param itemId    menu item id, greater than 0
     * @param itemName  item name as saved in the file (not blank, no comma)
     * @param unitPrice price paid per unit, greater than 0
     * @param quantity  how many, greater than 0
     * @param status    PENDING or COMPLETED (not null)
     * @throws IllegalArgumentException if a rule above is broken
     */
    public Order(int orderId, int itemId, String itemName,
                 double unitPrice, int quantity, Status status) {
        // TODO (Jontilano): validate, assign the fields, compute total.
    }

    /**
     * @return the unique order number
     */
    public int getOrderId() {
        // TODO (Jontilano): return the orderId field.
        return 0;
    }

    /**
     * @return the id of the menu item that was ordered
     */
    public int getItemId() {
        // TODO (Jontilano): return the itemId field.
        return 0;
    }

    /**
     * @return the item name copied at the time of ordering
     */
    public String getItemName() {
        // TODO (Jontilano): return the itemName field.
        return null;
    }

    /**
     * @return the price per unit copied at the time of ordering
     */
    public double getUnitPrice() {
        // TODO (Jontilano): return the unitPrice field.
        return 0.0;
    }

    /**
     * @return the quantity ordered (greater than 0)
     */
    public int getQuantity() {
        // TODO (Jontilano): return the quantity field.
        return 0;
    }

    /**
     * @return unitPrice x quantity, rounded to 2 decimals (computed once, in the constructor)
     */
    public double getTotal() {
        // TODO (Jontilano): return the total field.
        return 0.0;
    }

    /**
     * @return the current status (PENDING or COMPLETED)
     */
    public Status getStatus() {
        // TODO (Jontilano): return the status field.
        return null;
    }

    /**
     * @return true when the status is PENDING, false when COMPLETED
     */
    public boolean isPending() {
        // TODO (Jontilano): compare status with Status.PENDING.
        return false;
    }

    /**
     * Changes the status from PENDING to COMPLETED. This is the only method
     * that changes an Order after creation.
     *
     * @throws IllegalStateException if the order is already COMPLETED
     */
    public void markCompleted() {
        // TODO (Jontilano): if already COMPLETED throw IllegalStateException,
        //                   otherwise set status = Status.COMPLETED.
    }

    /**
     * One-line DEBUG text, for example
     * "Order{id=1, item=Siomai Rice x2, total=90.00, status=PENDING}".
     * NOT screen formatting (ConsoleUI does that).
     *
     * @return a one-line description of this order
     */
    public String toString() {
        // TODO (Jontilano): build and return the debug string.
        return "";
    }
}
