/**
 * ConsoleUI - everything the user sees and types. Formats output and collects
 * validated input. Contains NO business rules.
 *
 * OWNER      : Bolivar
 * CONTRACT   : Section 8.5 (class contract); Section 1 rule 3 (only this class
 *              may use System.out, System.err or Scanner)
 * DEPENDS ON : MenuItem, Order
 * USED BY    : Main
 *
 * PRIVATE FIELDS (Section 8.5)
 *   One Scanner on System.in (the only Scanner in the whole program);
 *   constant CURRENCY = "PHP " (no peso sign unless start.bat sets UTF-8)
 *
 * PUBLIC API (frozen signatures, Section 8.5)
 *   public ConsoleUI()
 *   public void showBanner()
 *   public void showMainMenu()
 *   public int readInt(String prompt, int min, int max)
 *   public String readLine(String prompt)
 *   public boolean confirm(String prompt)
 *   public void pause()
 *   public void printMenu(MenuItem[] menu)
 *   public void printOrders(String title, Order[] orders)
 *   public void printReceipt(Order order, int queuePosition)
 *   public void showInfo(String message)
 *   public void showSuccess(String message)
 *   public void showWarning(String message)
 *   public void showError(String message)
 *   public void showGoodbye()
 *   public void close()
 *
 * BEHAVIOR (Section 8.5 "Behavior")
 *   - showBanner: "UBITE - UCC Basic Input and Transaction Engine" + subtitle.
 *   - showMainMenu: prints the six options listed in Section 8.6; reads nothing.
 *   - readInt: always nextLine (never nextInt), trimmed. Not a whole number ->
 *     "Please enter a whole number." and ask again. Outside min..max ->
 *     "Please enter a number from MIN to MAX." and ask again. min > max throws
 *     IllegalArgumentException.
 *   - readLine: prompt, returns the trimmed line.
 *   - confirm: "prompt (y/n): ", accepts y/yes/n/no in any case, else asks again.
 *   - pause: "Press Enter to continue..." and waits.
 *   - printMenu: item number, name, price (CURRENCY + 2 decimals); unavailable
 *     items marked SOLD OUT; empty array shows "The menu is empty."
 *   - printOrders: title, then table: Order No. (#0007, 4-digit zero-padded,
 *     display only), Item, Qty, Unit Price, Total, Status. Empty array prints
 *     "(no orders to show)". Long item names shortened to keep columns aligned.
 *   - printReceipt: order number, item, quantity, unit price, total,
 *     "Your position in the queue: N".
 *   - showInfo / showSuccess / showWarning / showError: prefixes [INFO], [OK],
 *     [WARNING], [ERROR].
 *   - showGoodbye: closing message. close: closes the Scanner.
 *   - End of input: readInt, readLine, confirm and pause throw
 *     IllegalStateException("Input closed"); Main catches it (Section 10).
 *
 * MUST NOT (Section 8.5): do price math, touch the queue, read or write files,
 *   decide program flow, or call System.exit.
 *
 * SELF-TEST (Section 8.5): letters, blank, negatives and out-of-range numbers
 *   all re-prompt; mixing readInt and readLine does not skip input; table
 *   columns stay aligned for short and long item names.
 *
 * ALSO SEE: Section 6 (money format), Section 10 (validation matrix),
 *   Section 14 (why the UI is separated from Main).
 */
public class ConsoleUI {

    // TODO (Bolivar): add the Scanner field, CURRENCY constant, constructor,
    //                 and the methods listed above.

}
