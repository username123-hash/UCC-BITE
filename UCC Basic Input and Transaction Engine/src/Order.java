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

    // TODO (Jontilano): add the private fields, both constructors, getters,
    //                   isPending, markCompleted, and toString as listed above.

}
