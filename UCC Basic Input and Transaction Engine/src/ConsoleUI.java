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
 * MUST NOT (Section 8.5): do price math, touch the queue, read or write files,
 *   decide program flow, or call System.exit.
 *
 * HOW THE COMMENTS IN THIS FILE WORK
 *   - Comments that start with slash-star-star (like this one) are Javadoc
 *     comments. Tools (VS Code hover, the "javadoc" command) read them and
 *     show them as documentation.
 *   - Tags inside them: @param = what you pass in, @return = what you get
 *     back, @throws = when it fails on purpose.
 *   - Comments that start with // are normal notes for the reader only.
 */

import java.util.Scanner;

public class ConsoleUI {

    // ------------------------------------------------------------------
    // FIELDS AND CONSTANTS
    // ------------------------------------------------------------------

    // The ONLY Scanner in the whole program. Every keyboard read goes through it.
    private Scanner getInput = new Scanner(System.in);

    // Shown before every money value. Includes a trailing space on purpose (contract 8.5).
    private final String CURRENCY = "PHP ";

    /**
     * Creates the console UI. Nothing needs to be set up because the Scanner
     * and constants are created in the field declarations above.
     */
    public ConsoleUI() {

    }





    // ------------------------------------------------------------------
    // PRIVATE HELPER METHODS (only this class can call them)
    // ------------------------------------------------------------------

    /**
     * Prints blank lines. Used to add vertical spacing on screen.
     *
     * @param count how many empty lines to print
     */
    private void heightSpaces(int count) {
        for(int i=0; i<count; i++){
            System.out.println();
        }
    }

    /**
     * Reads one line from the keyboard. If the input stream has ended
     * (for example Ctrl+Z on Windows), the Scanner throws
     * NoSuchElementException; this method converts it into the
     * IllegalStateException("Input closed") that the contract requires,
     * so Main can catch it and shut down cleanly.
     *
     * @return the raw line typed by the user (not trimmed)
     * @throws IllegalStateException if there is no more input
     */
    private String nextLineOrThrow() {
        try {
            return getInput.nextLine();
        } catch (java.util.NoSuchElementException e) {
            throw new IllegalStateException("Input closed");
        }
    }

    /**
     * Clears the console screen using an ANSI escape code.
     * Currently not called anywhere; kept for use between screens if wanted.
     */
    private static void ClearCMDScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Turns a number into money text: CURRENCY + exactly 2 decimals.
     * This only FORMATS the value; it does no price math (contract rule).
     *
     * @param amount the value to display
     * @return text such as "PHP 65.00"
     */
    private String formatMoney(double amount) {
        return CURRENCY + String.format("%.2f", amount);
    }

    /**
     * Shortens text so it never breaks table alignment. If the text is longer
     * than maxLength, it is cut and "..." is added at the end.
     *
     * @param text      the text to shorten (null is treated as empty)
     * @param maxLength the most characters allowed in the result
     * @return the original text, or a shortened version ending in "..."
     */
    private String shorten(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }





    // ------------------------------------------------------------------
    // PUBLIC METHODS (the "endpoints" that Main is allowed to call)
    // ------------------------------------------------------------------

    /**
     * Shows the program banner and waits for Enter before continuing.
     * Called once at startup by Main.init().
     */
    public void showBanner(){
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
    }

    /**
     * Prints the six main menu options (numbers must match Main's menu
     * numbers in contract Section 8.6). It only prints; it reads nothing.
     * Main reads the choice afterwards with readInt(..., 1, 6).
     */
    public void showMainMenu(){
        System.out.println();
        System.out.println("========== MAIN MENU ==========");
        System.out.println("  1. View Menu");
        System.out.println("  2. Place Order");
        System.out.println("  3. View Pending Orders");
        System.out.println("  4. Process Next Order");
        System.out.println("  5. View Order History");
        System.out.println("  6. Exit");
        System.out.println("===============================");
    }

    /**
     * Asks for a whole number and keeps asking until the user gives a valid
     * one. Reads a whole line (never nextInt) so no leftover newline is
     * left behind, which keeps readInt and readLine safe to mix.
     *
     * @param prompt text shown before the user types (for example "Choice: ")
     * @param min    smallest accepted value
     * @param max    largest accepted value
     * @return a number from min to max (inclusive); never invalid
     * @throws IllegalArgumentException if min is greater than max (programmer error)
     * @throws IllegalStateException    if the input stream has ended
     */
    public int readInt(String prompt, int min, int max){
        if (min > max) {
            throw new IllegalArgumentException("min must not be greater than max");
        }
        while (true) {
            System.out.print(prompt);
            String line = nextLineOrThrow().trim();
            int value;
            try {
                value = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
                continue;
            }
            if (value < min || value > max) {
                System.out.println("Please enter a number from " + min + " to " + max + ".");
                continue;
            }
            return value;
        }
    }

    /**
     * Asks for a line of text.
     *
     * @param prompt text shown before the user types
     * @return what the user typed, with spaces at both ends removed (may be empty)
     * @throws IllegalStateException if the input stream has ended
     */
    public String readLine(String prompt){
        System.out.print(prompt);
        return nextLineOrThrow().trim();
    }

    /**
     * Asks a yes/no question and keeps asking until the answer is y, yes,
     * n or no (any letter case).
     *
     * @param prompt the question; " (y/n): " is added automatically
     * @return true for yes, false for no
     * @throws IllegalStateException if the input stream has ended
     */
    public boolean confirm(String prompt){
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String answer = nextLineOrThrow().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Please type y or n.");
        }
    }

    /**
     * Stops the program until the user presses Enter, so a screen can be
     * read before the next one appears.
     *
     * @throws IllegalStateException if the input stream has ended
     */
    public void pause(){
        System.out.print("Press Enter to continue...");
        nextLineOrThrow();
    }

    /**
     * Prints the menu as a table: item number, name and price. Items that are
     * not available are marked SOLD OUT. The number shown is the item's id
     * (getId()), which is what the customer types when ordering.
     *
     * @param menu the menu items to show; an empty (or null) array prints
     *             "The menu is empty."
     */
    public void printMenu(MenuItem[] menu){
        System.out.println();
        System.out.println("-------------- MENU --------------");

        if (menu == null || menu.length == 0) {
            System.out.println("The menu is empty.");
            return;
        }

        // Column widths: No. = 5, Item = 22, Price = 12 (right-aligned)
        String format = "%-5s %-22s %12s  %s%n";

        System.out.printf(format, "No.", "Item", "Price", "");
        System.out.println("---------------------------------------------------");

        for (MenuItem item : menu) {
            String status = item.isAvailable() ? "" : "SOLD OUT";
            System.out.printf(format,
                    String.valueOf(item.getId()),
                    shorten(item.getName(), 22),
                    formatMoney(item.getPrice()),
                    status);
        }
        System.out.println("---------------------------------------------------");
    }

    /**
     * Prints a titled table of orders: Order No., Item, Qty, Unit Price,
     * Total and Status. The order number is display-only: "#" plus 4 digits
     * padded with zeros (order 7 shows as #0007). Long item names are
     * shortened so the columns stay aligned. All values come from the Order
     * getters; nothing is calculated here.
     *
     * @param title  heading printed above the table
     * @param orders the orders to show, in the order given; an empty (or
     *               null) array prints "(no orders to show)"
     */
    public void printOrders(String title, Order[] orders){
        System.out.println();
        System.out.println("=== " + title + " ===");

        if (orders == null || orders.length == 0) {
            System.out.println("(no orders to show)");
            return;
        }

        // Column widths: Order No. = 10, Item = 22, Qty = 4, prices = 12 (right-aligned)
        String format = "%-10s %-22s %4s %12s %12s  %s%n";

        System.out.printf(format, "Order No.", "Item", "Qty", "Unit Price", "Total", "Status");
        System.out.println("---------------------------------------------------------------------------");

        for (Order order : orders) {
            System.out.printf(format,
                    String.format("#%04d", order.getOrderId()),
                    shorten(order.getItemName(), 22),
                    String.valueOf(order.getQuantity()),
                    formatMoney(order.getUnitPrice()),
                    formatMoney(order.getTotal()),
                    order.getStatus());
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    /**
     * Prints the receipt shown right after an order is placed: order number,
     * item, quantity, unit price, total and the customer's place in the queue.
     *
     * @param order         the order that was just created
     * @param queuePosition the order's position in the queue (1 = next to be served)
     */
    public void printReceipt(Order order, int queuePosition){
        System.out.println();
        System.out.println("============== RECEIPT ==============");
        System.out.println("Order No.  : " + String.format("#%04d", order.getOrderId()));
        System.out.println("Item       : " + order.getItemName());
        System.out.println("Quantity   : " + order.getQuantity());
        System.out.println("Unit Price : " + formatMoney(order.getUnitPrice()));
        System.out.println("Total      : " + formatMoney(order.getTotal()));
        System.out.println("-------------------------------------");
        System.out.println("Your position in the queue: " + queuePosition);
        System.out.println("=====================================");
    }

    /**
     * Prints a neutral message, prefixed with [INFO].
     *
     * @param message the text to show
     */
    public void showInfo(String message){
        System.out.println("[INFO] " + message);
    }

    /**
     * Prints a good-news message, prefixed with [OK].
     *
     * @param message the text to show
     */
    public void showSuccess(String message){
        System.out.println("[OK] " + message);
    }

    /**
     * Prints a caution message, prefixed with [WARNING]. Used for problems
     * that do not stop the program (for example a skipped CSV row).
     *
     * @param message the text to show
     */
    public void showWarning(String message){
        System.out.println("[WARNING] " + message);
    }

    /**
     * Prints an error message, prefixed with [ERROR]. Used when an action
     * could not be done (for example a sold-out item). It only prints; the
     * decision of what happens next belongs to Main.
     *
     * @param message the text to show
     */
    public void showError(String message){
        System.out.println("[ERROR] " + message);
    }

    /**
     * Prints the closing message when the program ends.
     * Called by Main.shutdown().
     */
    public void showGoodbye(){
        System.out.println();
        System.out.println("Thank you for using UBITE. Goodbye!");
    }

    /**
     * Closes the Scanner. Call it last, once, from Main.shutdown(); after
     * this, no read method may be used.
     */
    public void close(){
        getInput.close();
    }
}