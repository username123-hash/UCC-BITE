import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    //CONSTANTS Definition
    private static final int MAX_QUANTITY = 99;     //Defined Max Units of Items per Order.
    private final String CURRENCY = "PHP ";         //Defined Currency used and it's format.

    //Objects Instantiation (declared here, created in init())
        //Implementation of Local Object
        private ArrayList<Order> orders;            //Instantiation of the Local Object "Order", for complete history, ascending orderId

        //Local Objects
        private CsvManager csvManager;              //All File Reading/Writing
        private OrderQueue orderQueue;              //Pending orders, FIFO
        private MenuItem[] menuItems;               //The menu loaded from menu.csv

        //Scanner Object
        private Scanner scanner;                    //Input Getter

    //Class Variable
        private int nextOrderId;                    //Number the next new order will get





    //Methods:
        //Helpers: For Display

            //1. Prints blank lines. Used to add vertical spacing on screen.
            private void heightSpaces(int count) {
                for(int i=0; i<count; i++){
                    System.out.println();
                }
            }

            //2. Reads one line from the keyboard. [If the input stream has ended (for example Ctrl+Z on Windows), the Scanner throws NoSuchElementException; this method turns it into IllegalStateException("Input closed") so main() can catch it and shut down cleanly.]
            private String nextLineOrThrow() {
                try {
                    return scanner.nextLine();
                } catch (NoSuchElementException e) {
                    throw new IllegalStateException("Input closed");
                }
            }

            //3. Turns a number into money text: CURRENCY + exactly 2 decimals (Locale.US makes sure the decimal separator is always a dot).
            private String formatMoney(double amount) {
                return CURRENCY + String.format(Locale.US, "%.2f", amount);
            }

            //4. Shortens text so it never breaks table alignment. If the text is longer than maxLength, it is cut and "..." is added at the end.
            private String shorten(String text, int maxLength) {
                if (text == null) {
                    return "";
                }
                if (text.length() <= maxLength) {
                    return text;
                }
                return text.substring(0, maxLength - 3) + "...";
            }

            //5. Asks a yes/no question and keeps asking until the answer is y, yes, n or no (any letter case).
            private boolean UIconfirm(String prompt){
                while (true) {
                    System.out.print(prompt + " (y/n): ");
                    String answer = nextLineOrThrow().trim().toLowerCase();

                    if (answer.equals("y") || answer.equals("yes")) {return true;}
                    if (answer.equals("n") || answer.equals("no")) {return false;}

                    System.out.println("Please type y or n.");
                }
            }

            //6. Turns an order id into display text: "#" plus 4 digits padded with zeros (7 becomes "#0007").
            private String formatOrderNumber(int orderId) {
                return String.format("#%04d", orderId);
            }

            //7. Clears the console screen using an ANSI escape code. Called before each new screen so the old text does not pile up.
            private void UIClearCMDScreen(){
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }

            //8. Stops the program until the user presses Enter, so a screen can be read before the next one appears.
            private void UIpause(){
                System.out.print("Press Enter to continue...");
                nextLineOrThrow();
            }

            //9. Asks for a whole number from min to max and keeps asking until the input is valid.
            //   It reads a whole line (never nextInt) so no leftover newline is left behind.
            private int UIreadInt(String prompt, int min, int max){
                while (true) {
                    System.out.print(prompt);
                    String line = nextLineOrThrow().trim();

                    try {
                        int value = Integer.parseInt(line);
                        if (value >= min && value <= max) {
                            return value;
                        }
                        System.out.println("Please enter a number from " + min + " to " + max + ".");
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a whole number.");
                    }
                }
            }

            //10. Shows the program banner and waits for Enter, then clears the screen so the banner does not stay on screen.
            private void UIshowBanner(){
                System.out.println(" __    __  _______   ______  ________  ________ ");
                System.out.println("/  |  /  |/       \\ /      |/        |/        |");
                System.out.println("$$ |  $$ |$$$$$$$  |$$$$$$/ $$$$$$$$/ $$$$$$$$/ ");
                System.out.println("$$ |  $$ |$$ |__$$ |  $$ |     $$ |   $$ |__ ");
                System.out.println("$$ |  $$ |$$    $$<   $$ |     $$ |   $$    |");
                System.out.println("$$ |  $$ |$$$$$$$  |  $$ |     $$ |   $$$$$/    ");
                System.out.println("$$ \\__$$ |$$ |__$$ | _$$ |_    $$ |   $$ |_____ ");
                System.out.println("$$    $$/ $$    $$/ / $$   |   $$ |   $$       |");
                System.out.println(" $$$$$$/  $$$$$$$/  $$$$$$/    $$/    $$$$$$$$/ ");

                heightSpaces(1); System.out.println("    UCC Basic Input and Transaction Engine");
                heightSpaces(3); System.out.println("         [Press Enter to Continue.]");

                nextLineOrThrow();
                UIClearCMDScreen();
            }

            //11. Prints the six main menu options. It only prints; run() reads the choice.
            private void UIshowMainMenu(){
                System.out.println();
                System.out.println("============= MAIN MENU =============");
                System.out.println("  1. View Menu");
                System.out.println("  2. Place Order");
                System.out.println("  3. View Pending Orders");
                System.out.println("  4. Process Next Order     (Kitchen)");
                System.out.println("  5. View Order History     (Manager)");
                System.out.println("  6. Exit");
                System.out.println("=====================================");
            }

            //12. Prints the menu as a table: item number (the id the customer types), name and price. Unavailable items are marked SOLD OUT.
            private void UIprintMenu(MenuItem[] items){
                System.out.println();
                System.out.println("----------------------- MENU ----------------------");

                if (items == null || items.length == 0) {
                    System.out.println("The menu is empty.");
                    return;
                }

                //Column widths: No. = 5, Item = 22, Price = 12 (right-aligned)
                String format = "%-5s %-22s %12s  %s%n";

                System.out.printf(format, "No.", "Item", "Price", "");
                System.out.println("---------------------------------------------------");

                for (MenuItem item : items) {
                    String status = item.isAvailable() ? "" : "SOLD OUT";
                    System.out.printf(format,
                            String.valueOf(item.getId()),
                            shorten(item.getName(), 22),
                            formatMoney(item.getPrice()),
                            status);
                }
                System.out.println("---------------------------------------------------");
            }

            //13. Prints a titled table of orders. All values come from the Order getters; nothing is calculated here.
            private void UIprintOrders(String title, Order[] ordersToShow){
                System.out.println();
                System.out.println("=== " + title + " ===");

                if (ordersToShow == null || ordersToShow.length == 0) {
                    System.out.println("(no orders to show)");
                    return;
                }

                //Column widths: Order No. = 10, Item = 22, Qty = 4, prices = 12 (right-aligned)
                String format = "%-10s %-22s %4s %12s %12s  %s%n";

                System.out.printf(format, "Order No.", "Item", "Qty", "Unit Price", "Total", "Status");
                System.out.println("---------------------------------------------------------------------------");

                for (Order order : ordersToShow) {
                    System.out.printf(format,
                            formatOrderNumber(order.getOrderId()),
                            shorten(order.getItemName(), 22),
                            String.valueOf(order.getQuantity()),
                            formatMoney(order.getUnitPrice()),
                            formatMoney(order.getTotal()),
                            order.getStatus());
                }
                System.out.println("---------------------------------------------------------------------------");
            }

            //14. Prints the receipt shown right after an order is placed (queuePosition 1 = next to be served).
            private void UIprintReceipt(Order order, int queuePosition){
                System.out.println();
                System.out.println("============== RECEIPT ==============");
                System.out.println("Order No.  : " + formatOrderNumber(order.getOrderId()));
                System.out.println("Item       : " + order.getItemName());
                System.out.println("Quantity   : " + order.getQuantity());
                System.out.println("Unit Price : " + formatMoney(order.getUnitPrice()));
                System.out.println("Total      : " + formatMoney(order.getTotal()));
                System.out.println("-------------------------------------");
                System.out.println("Your position in the queue: " + queuePosition);
                System.out.println("=====================================");
            }

            //15. Prints a neutral message, prefixed with [INFO].
            private void UIshowInfo(String message){
                System.out.println("[INFO] " + message);
            }

            //16. Prints a good-news message, prefixed with [OK].
            private void UIshowSuccess(String message){
                System.out.println("[OK] " + message);
            }

            //17. Prints a caution message, prefixed with [WARNING]. For problems that do not stop the program (for example a skipped CSV row).
            private void UIshowWarning(String message){
                System.out.println("[WARNING] " + message);
            }

            //18. Prints an error message, prefixed with [ERROR]. It only prints; what happens next is decided by the caller.
            private void UIshowError(String message){
                System.out.println("[ERROR] " + message);
            }

            //19. Prints the closing message when the program ends.
            private void UIshowGoodbye(){
                System.out.println();
                System.out.println("Thank you for using UBITE. Goodbye!");
            }





        //Helpers: For Workflow

            //1. Asks for an item number until it is valid. Returns the chosen MenuItem, or null if the customer typed 0 to cancel.
            //   The upper limit is Integer.MAX_VALUE on purpose: whether the item exists is checked here, not by UIreadInt.
            //   The screen is NOT cleared here, so the menu stays visible after a wrong choice.
            private MenuItem askForItem() {
                while (true) {
                    int itemId = UIreadInt("Enter item number (0 to cancel): ", 0, Integer.MAX_VALUE);

                    if (itemId == 0) {
                        return null;
                    }

                    MenuItem found = findMenuItemById(itemId);
                    if (found == null) {
                        UIshowError("Item number " + itemId + " does not exist.");
                    } else if (!found.isAvailable()) {
                        UIshowError(found.getName() + " is sold out.");
                    } else {
                        return found;
                    }
                }
            }

            //2. Looks for a menu item by id. Returns the matching MenuItem, or null if none matches.
            private MenuItem findMenuItemById(int id) {
                for (MenuItem item : menuItems) {
                    if (item.getId() == id) {
                        return item;
                    }
                }
                return null;
            }

            //3. Returns the highest orderId in orders plus 1, or 1 if orders is empty.
            private int computeNextOrderId() {
                int highest = 0;
                for (Order order : orders) {
                    if (order.getOrderId() > highest) {
                        highest = order.getOrderId();
                    }
                }
                return highest + 1;
            }

            //4. Clears the queue, then enqueues every PENDING order in list order (this restores FIFO after a restart).
            private void rebuildQueue() {
                orderQueue.clear();

                for (Order order : orders) {
                    //enqueue() returns false when the queue is full
                    if (order.isPending() && !orderQueue.enqueue(order)) {
                        UIshowWarning("Order " + formatOrderNumber(order.getOrderId()) + " could not be added to the queue.");
                    }
                }
            }

            //5. Saves the whole history to orders.csv. If saving fails, it shows an error and the program keeps running with its in-memory data.
            private void saveAll() {
                if (!csvManager.saveOrders(orders)) {
                    UIshowError("Your orders could not be saved. If orders.csv is open in another program (such as Excel), please close it. The program will keep running.");
                }
            }





        //Program Flow

            //1. STARTUP (contract Section 9.1)
            private void init() {
                //Step 0: create the objects that were declared at the top
                scanner = new Scanner(System.in);
                csvManager = new CsvManager();
                orderQueue = new OrderQueue();

                //Step 1
                UIshowBanner();

                //Step 2: without the data files the program cannot work, so it stops here.
                if (!csvManager.ensureDataFiles()) {
                    UIshowError("The data files could not be created or opened. UBITE cannot start.");
                    for (String warning : csvManager.getWarnings()) {
                        UIshowError(warning);
                    }
                    System.exit(1);
                }

                //Step 3
                menuItems = csvManager.loadMenu();
                orders = csvManager.loadOrders();

                //Step 4: show every warning collected while loading. Pause only if there were any (before clearing the list).
                List<String> warnings = csvManager.getWarnings();
                for (String warning : warnings) {
                    UIshowWarning(warning);
                }
                if (!warnings.isEmpty()) {
                    UIpause();
                }
                csvManager.clearWarnings();

                //Steps 5 and 6: this is what restores the pending orders after a restart
                nextOrderId = computeNextOrderId();
                rebuildQueue();

                //Step 7: the program keeps running even with an empty menu.
                if (menuItems.length == 0) {
                    UIshowError("The menu is empty, so orders cannot be placed until menu.csv is fixed.");
                    UIpause();
                }
            }

            //2. MAIN LOOP: show the main menu, read a choice (1 to 6), run its handler, and repeat until the user picks 6 (Exit).
            private void run() {
                int choice = 0;

                while (choice != 6) {
                    UIClearCMDScreen();
                    UIshowMainMenu();
                    choice = UIreadInt("Choice: ", 1, 6);

                    if (choice == 1)      {handleViewMenu();}
                    else if (choice == 2) {handlePlaceOrder();}
                    else if (choice == 3) {handleViewPending();}
                    else if (choice == 4) {handleProcessNext();}
                    else if (choice == 5) {handleViewHistory();}
                    //choice 6 (Exit): nothing to do, the loop ends by itself
                }
            }

            //3. SHUTDOWN: save, say goodbye, close the Scanner. Called once, last.
            private void shutdown() {
                saveAll();
                UIshowGoodbye();
                scanner.close();
            }





        //Menu Handlers (one method per main menu choice)

            //1. VIEW MENU
            private void handleViewMenu() {
                UIClearCMDScreen();
                UIprintMenu(menuItems);
                UIpause();
            }

            //2. PLACE ORDER (contract Section 9.2)
            private void handlePlaceOrder() {
                //Step 0
                UIClearCMDScreen();

                //Step 1
                if (menuItems.length == 0) {
                    UIshowError("The menu is empty. Orders cannot be placed until menu.csv is fixed.");
                    UIpause();
                    return;
                }

                //Step 2
                UIprintMenu(menuItems);

                //Step 3: askForItem() keeps asking until the item is valid. null means the customer typed 0 to cancel.
                MenuItem item = askForItem();
                if (item == null) {
                    return;
                }

                //Step 4
                int quantity = UIreadInt("Quantity (1 to " + MAX_QUANTITY + "): ", 1, MAX_QUANTITY);

                //Step 5: check BEFORE creating the order so no order number is wasted.
                if (orderQueue.isFull()) {
                    UIshowError("The queue is full. Please process some orders first.");
                    UIpause();
                    return;
                }

                //Step 6: create the order but do not store it yet. Order.getTotal() gives the total, so Main does no price math.
                Order order = new Order(nextOrderId, item, quantity);
                UIprintOrders("Order Summary", new Order[] { order });
                if (!UIconfirm("Place this order?")) {
                    UIshowInfo("Order cancelled.");
                    UIpause();
                    return;
                }

                //Step 7: now the order becomes real. isFull() was checked in step 5, so enqueue() cannot fail here.
                orders.add(order);
                orderQueue.enqueue(order);
                nextOrderId++;

                //Steps 8 and 9
                saveAll();
                UIprintReceipt(order, orderQueue.size());
                UIpause();
            }

            //3. VIEW PENDING (oldest first)
            private void handleViewPending() {
                UIClearCMDScreen();
                UIprintOrders("Pending Orders (oldest first)", orderQueue.toArray());
                UIpause();
            }

            //4. PROCESS NEXT (contract Section 9.3)
            private void handleProcessNext() {
                //Step 0
                UIClearCMDScreen();

                //Step 1
                if (orderQueue.isEmpty()) {
                    UIshowInfo("No pending orders.");
                    UIpause();
                    return;
                }

                //Step 2: the queue and the orders list hold the SAME Order object, so marking it completed also updates the history.
                Order order = orderQueue.dequeue();
                order.markCompleted();

                //Steps 3 and 4
                saveAll();
                UIshowSuccess("Order " + formatOrderNumber(order.getOrderId()) + " (" + order.getItemName() + ") has been processed.");
                UIpause();
            }

            //5. VIEW HISTORY (the orders that are no longer pending)
            private void handleViewHistory() {
                UIClearCMDScreen();

                //Going through the list in order keeps the completed orders in ascending order number.
                ArrayList<Order> completed = new ArrayList<>();
                for (Order order : orders) {
                    if (!order.isPending()) {
                        completed.add(order);
                    }
                }

                //toArray(new Order[0]) turns the ArrayList into an Order[] for UIprintOrders.
                UIprintOrders("Completed Orders", completed.toArray(new Order[0]));
                UIpause();
            }





    public static void main(String[] args) {
        Main app = new Main();
        app.init();

        try {
            app.run();
        } catch (IllegalStateException e) {
            //The input stream was closed: just continue to shutdown() so the data is saved.
        }

        app.shutdown();
    }
}