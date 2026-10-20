/**
 * Main - starts the program and coordinates the other classes. Holds the
 * workflow and the business rules, but NO file parsing, NO queue logic,
 * NO Scanner and NO System.out.
 *
 * OWNER      : Bolivar
 * CONTRACT   : Section 8.6 (class contract), Section 9 (end-to-end flows:
 *              9.1 startup, 9.2 place order, 9.3 process next, 9.4 restart
 *              restoration), Section 10 (validation matrix)
 * DEPENDS ON : MenuItem, Order, OrderQueue, CsvManager, ConsoleUI
 * USED BY    : entry point (launched by start.bat, Section 5)
 *
 * PRIVATE FIELDS (Section 8.6)
 *   ConsoleUI ui; CsvManager csv; OrderQueue queue; MenuItem[] menu;
 *   ArrayList<Order> orders (full history, always ascending orderId);
 *   int nextOrderId
 *
 * CONSTANTS (Section 8.6)
 *   MAX_QUANTITY = 99
 *   Main menu numbers: 1 View Menu, 2 Place Order, 3 View Pending Orders,
 *   4 Process Next Order, 5 View Order History, 6 Exit
 *
 * METHODS (frozen signatures, Section 8.6)
 *   public static void main(String[] args)
 *   private void init()                  -> Section 9.1
 *   private void run()                   -> main loop, readInt(1 to 6)
 *   private void handleViewMenu()        -> printMenu, then pause
 *   private void handlePlaceOrder()      -> Section 9.2
 *   private void handleViewPending()     -> printOrders("Pending Orders (oldest first)", queue.toArray())
 *   private void handleProcessNext()     -> Section 9.3
 *   private void handleViewHistory()     -> non-pending orders, "Completed Orders"
 *   private void shutdown()              -> saveAll, showGoodbye, ui.close
 *   private MenuItem findMenuItemById(int id)   -> match or null
 *   private int computeNextOrderId()     -> highest orderId + 1, or 1 if empty
 *   private void rebuildQueue()          -> clear queue, enqueue every PENDING
 *                                           order in list order; warn if enqueue fails
 *   private boolean saveAll()            -> csv.saveOrders(orders); on false show
 *                                           error (suggest closing orders.csv) and
 *                                           keep running
 *
 * BEHAVIOR (Section 8.6 "Behavior")
 *   - main: create objects, call init, then run inside try/catch for
 *     IllegalStateException (input closed), then shutdown.
 *   - Main is the ONLY class allowed to call System.exit (Section 1 rule 4).
 *   - Business rules that live here: availability check, MAX_QUANTITY,
 *     queue-full check BEFORE creating an order, order numbering.
 *
 * MUST NOT (Section 8.6): parse files, implement queue logic, use Scanner,
 *   or use System.out.
 *
 * ALSO SEE: Section 10 (who detects which error), Section 12 (schedule),
 *   Section 13 (acceptance tests), Section 14 (explain the overall workflow).
 */
public class Main {

    // TODO (Bolivar): add the private fields, MAX_QUANTITY, main(), and the
    //                 private methods listed above.

}
