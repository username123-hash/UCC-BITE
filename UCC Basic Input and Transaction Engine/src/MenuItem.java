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

    // TODO (Ampater): add the private fields, constructor, getters, and toString
    //                 exactly as listed above.

}
