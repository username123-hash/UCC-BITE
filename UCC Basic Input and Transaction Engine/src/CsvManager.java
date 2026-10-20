/**
 * CsvManager - the ONLY class that knows how data is stored on disk.
 * Loads and saves menu.csv and orders.csv and keeps a list of warnings.
 *
 * OWNER      : Dizon
 * CONTRACT   : Section 8.4 (class contract), Section 7 (data file specification:
 *              7.1 menu.csv, 7.2 orders.csv, 7.3 rules for both files)
 * DEPENDS ON : MenuItem, Order
 * USED BY    : Main
 *
 * PRIVATE FIELDS (Section 8.4)
 *   String dataFolder, menuPath, ordersPath; ArrayList<String> warnings
 *
 * PUBLIC API (frozen signatures, Section 8.4)
 *   public static final String DEFAULT_DATA_FOLDER = "data";
 *
 *   public CsvManager()
 *   public CsvManager(String dataFolder)
 *   public boolean ensureDataFiles()
 *   public MenuItem[] loadMenu()
 *   public ArrayList<Order> loadOrders()
 *   public boolean saveOrders(List<Order> orders)
 *   public List<String> getWarnings()
 *   public void clearWarnings()
 *
 * BEHAVIOR (Section 8.4 "Behavior")
 *   - No-arg constructor uses folder "data", relative to the working directory
 *     that start.bat sets (Section 5).
 *   - ensureDataFiles: create data folder if missing; create menu.csv (header +
 *     default seed menu, Section 7.1) if missing; create orders.csv (header only)
 *     if missing. NEVER overwrites an existing file. Returns true if both files
 *     exist and are usable, else false with a warning.
 *   - loadMenu: MenuItem[] in file order (rules in Section 7.3). Catches
 *     IllegalArgumentException and number-format errors per row, skips the row,
 *     adds a warning. Repeated id skipped with a warning. Never null (empty or
 *     unreadable file = zero-length array + warning).
 *   - loadOrders: ArrayList<Order> sorted ascending by orderId, built with the
 *     Order RESTORE constructor. total column ignored. Unknown status text =
 *     skipped row. Repeated orderId skipped with a warning. Never null.
 *   - saveOrders: header + one row per order in the order given. Write to a
 *     temporary file first, then replace orders.csv. Returns true on success;
 *     on I/O failure (e.g. file locked in Excel) returns false + warning and
 *     never throws for I/O problems. Null list throws IllegalArgumentException.
 *   - Numbers written/parsed with a dot decimal separator (explicit US locale).
 *   - getWarnings: unmodifiable list, in order. clearWarnings: empties it.
 *     Warning style: "orders.csv line 7 skipped: quantity is not a whole number".
 *
 * FILE RULES (Section 7.3): first non-blank line is the header and is skipped;
 *   strip UTF-8 BOM; skip blank lines; trim every field; quoted fields not
 *   supported; wrong column count / unparseable number / constructor rejection
 *   = skipped row + warning (file name, line number, reason); save rewrites the
 *   whole file; the next order number is NOT stored (Main computes it).
 *
 * MUST NOT (Section 8.4): print to the console, decide what to do about
 *   warnings, call System.exit, or keep orders in memory beyond a single call.
 *
 * SELF-TEST (Section 8.4): deleted data folder is recreated; header-only
 *   orders.csv loads as empty; letters in quantity = skipped row with the line
 *   number in the warning; BOM file loads correctly; save then load gives
 *   identical orders; locked file makes saveOrders return false.
 *
 * ALSO SEE: Section 10 (error matrix), Section 9.4 (restart restoration),
 *   Section 11 point 4 (handy placeholder skeleton).
 */
public class CsvManager {

    // TODO (Dizon): add DEFAULT_DATA_FOLDER, the private fields, both
    //               constructors, and the methods listed above.

}
