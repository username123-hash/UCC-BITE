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
        // Order Validation Input
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be greater than 0.");
        }
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be empty.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        //
        this.orderId = orderId;
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.unitPrice = item.getPrice();
        this.quantity = quantity;

        this.total = Math.round(this.unitPrice * this.quantity * 100.0) / 100.0;
        this.status = Status.PENDING;
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
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be greater than 0.");
        }
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be greater than 0.");
        }
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be blank.");
        }
        if (itemName.contains(",")) {
            throw new IllegalArgumentException("Item name cannot contain a comma.");
        }
        if (unitPrice <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than 0.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.total = Math.round(unitPrice * quantity * 100.0) / 100.0;
        this.status = status;
    }

    /**
     * @return the unique order number
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @return the id of the menu item that was ordered
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * @return the item name copied at the time of ordering
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @return the price per unit copied at the time of ordering
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * @return the quantity ordered (greater than 0)
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return unitPrice x quantity, rounded to 2 decimals (computed once, in the constructor)
     */
    public double getTotal() {
        return total;
    }

    /**
     * @return the current status (PENDING or COMPLETED)
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return true when the status is PENDING, false when COMPLETED
     */
    public boolean isPending() {
        return status == Status.PENDING;
    }

    /**
     * Changes the status from PENDING to COMPLETED. This is the only method
     * that changes an Order after creation.
     *
     * @throws IllegalStateException if the order is already COMPLETED
     */
    public void markCompleted() {
        if (status == Status.COMPLETED) {
            throw new IllegalStateException("Order is already completed.");
        }

        status = Status.COMPLETED;
    }

    /**
     * One-line DEBUG text, for example
     * "Order{id=1, item=Siomai Rice x2, total=90.00, status=PENDING}".
     * NOT screen formatting (ConsoleUI does that).
     *
     * @return a one-line description of this order
     */
    @Override
    public String toString() {
        return String.format(
                java.util.Locale.US,
                "Order{id=%d, item=%s x%d, total=%.2f, status=%s}",
                orderId, itemName, quantity, total, status
        );
    }
}
