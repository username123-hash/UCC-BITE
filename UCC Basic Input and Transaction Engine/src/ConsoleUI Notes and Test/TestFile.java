/**
 * TestFile - throwaway tests for ConsoleUI (contract Section 8.5 self-test).
 * NOT part of the final program: do not copy it into src/.
 *
 * HOW IT WORKS (the two tricks)
 *   1. Fake keyboard: ConsoleUI reads from System.in. Before creating each
 *      ConsoleUI we call System.setIn(...) with a ByteArrayInputStream that
 *      holds the text a user would type (every "\n" is one Enter key press).
 *   2. Fake screen: ConsoleUI prints with System.out. We call System.setOut(...)
 *      so everything it prints is saved in a ByteArrayOutputStream, and then
 *      we can check that the saved text contains what we expect.
 *   The test results themselves are printed with REAL_OUT (the real screen).
 *
 * WHAT YOU SEE
 *   [PASS] / [FAIL] for every check, a total at the end, and then a
 *   "VISUAL CHECK" where the tables are printed for real so you can look at
 *   the alignment with your own eyes.
 *
 * It uses test-double versions of MenuItem and Order that live in this same
 * folder, so these tests do not depend on the teammates' files.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class TestFile {

    // ------------------------------------------------------------------
    // TEST TOOLS
    // ------------------------------------------------------------------

    /** One block of test code. "throws Exception" lets tests skip try/catch noise. */
    private interface Step {
        void run() throws Exception;
    }

    private static final PrintStream REAL_OUT = System.out;
    private static final InputStream REAL_IN = System.in;

    private static int passed = 0;
    private static int failed = 0;

    // Everything the current ConsoleUI printed since makeUi() was called.
    private static ByteArrayOutputStream captured = new ByteArrayOutputStream();

    /**
     * Creates a ConsoleUI whose keyboard is the given text and whose screen is
     * captured. Call it again for each new scenario (each call resets both).
     *
     * @param input what the "user" types; use \n for each Enter press
     */
    private static ConsoleUI makeUi(String input) throws Exception {
        System.setIn(new ByteArrayInputStream(input.getBytes("UTF-8")));
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, "UTF-8"));
        return new ConsoleUI();   // its Scanner is created NOW, so it sees the fake input
    }

    /** Everything printed since the last makeUi(). */
    private static String output() throws Exception {
        return captured.toString("UTF-8");
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            REAL_OUT.println("  [PASS] " + name);
        } else {
            failed++;
            REAL_OUT.println("  [FAIL] " + name);
        }
    }

    private static void checkEquals(String name, Object expected, Object actual) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            passed++;
            REAL_OUT.println("  [PASS] " + name);
        } else {
            failed++;
            REAL_OUT.println("  [FAIL] " + name + "   expected <" + expected + "> but got <" + actual + ">");
        }
    }

    /**
     * Passes only if the step throws the given exception type.
     *
     * @param message if not null, the exception message must equal it
     */
    private static void checkThrows(String name, Class<? extends Throwable> type,
                                    String message, Step step) {
        try {
            step.run();
        } catch (Throwable t) {
            boolean typeOk = type.isInstance(t);
            boolean messageOk = (message == null) || message.equals(t.getMessage());
            if (typeOk && messageOk) {
                passed++;
                REAL_OUT.println("  [PASS] " + name);
            } else {
                failed++;
                REAL_OUT.println("  [FAIL] " + name + "   got " + t);
            }
            return;
        }
        failed++;
        REAL_OUT.println("  [FAIL] " + name + "   nothing was thrown");
    }

    /** Runs one group of tests; an unexpected crash counts as one failure and the next group still runs. */
    private static void group(String title, Step step) {
        REAL_OUT.println();
        REAL_OUT.println("== " + title);
        try {
            step.run();
        } catch (Throwable t) {
            failed++;
            REAL_OUT.println("  [FAIL] unexpected exception: " + t);
        }
    }

    private static int count(String text, String part) {
        int total = 0;
        int from = 0;
        while ((from = text.indexOf(part, from)) != -1) {
            total++;
            from += part.length();
        }
        return total;
    }

    private static String[] lines(String text) {
        return text.split("\\r?\\n");
    }

    // ------------------------------------------------------------------
    // SAMPLE DATA
    // ------------------------------------------------------------------

    private static MenuItem[] sampleMenu() {
        return new MenuItem[] {
            new MenuItem(1, "Siomai Rice", 45.00, true),
            new MenuItem(2, "Chicken Adobo with Rice", 65.00, true),   // 23 letters: must be shortened
            new MenuItem(6, "Spaghetti", 40.00, false),                // the SOLD OUT one
            new MenuItem(7, "Iced Tea", 1234.50, true)                 // wide price
        };
    }

    private static Order[] sampleOrders() {
        return new Order[] {
            new Order(7, 2, "Chicken Adobo with Rice", 65.00, 2, Order.Status.PENDING),
            new Order(12, 1, "Siomai Rice", 45.00, 1, Order.Status.COMPLETED),
            new Order(123, 5, "Fried Chicken", 19.99, 3, Order.Status.PENDING)   // 59.97
        };
    }

    // ------------------------------------------------------------------
    // TESTS
    // ------------------------------------------------------------------

    private static void testReadInt() throws Exception {
        ConsoleUI ui = makeUi("3\n");
        checkEquals("valid number is returned", 3, ui.readInt("Choice: ", 1, 6));
        check("prompt is printed", output().contains("Choice: "));

        ui = makeUi("1\n6\n");
        checkEquals("min is accepted (inclusive)", 1, ui.readInt("> ", 1, 6));
        checkEquals("max is accepted (inclusive)", 6, ui.readInt("> ", 1, 6));

        ui = makeUi("  5  \n");
        checkEquals("spaces around the number are trimmed", 5, ui.readInt("> ", 1, 9));

        ui = makeUi("-3\n");
        checkEquals("negative numbers work when the range allows them", -3, ui.readInt("> ", -5, 5));

        ui = makeUi("abc\n4\n");
        checkEquals("letters are rejected, then valid input is returned", 4, ui.readInt("> ", 1, 6));
        check("shows 'Please enter a whole number.'", output().contains("Please enter a whole number."));
        checkEquals("prompt is shown again after the bad input", 2, count(output(), "> "));

        ui = makeUi("2.5\n3\n");
        checkEquals("decimal number is rejected", 3, ui.readInt("> ", 1, 6));
        check("decimal gets the whole-number message", output().contains("Please enter a whole number."));

        ui = makeUi("\n\n2\n");
        checkEquals("blank lines are rejected", 2, ui.readInt("> ", 1, 6));
        checkEquals("blank input gets the message each time", 2, count(output(), "Please enter a whole number."));

        ui = makeUi("9\n0\n-1\n2\n");
        checkEquals("out-of-range numbers are rejected until valid", 2, ui.readInt("> ", 1, 6));
        checkEquals("range message shown for each bad number", 3,
                count(output(), "Please enter a number from 1 to 6."));

        checkThrows("min greater than max throws IllegalArgumentException",
                IllegalArgumentException.class, null,
                () -> makeUi("1\n").readInt("> ", 6, 1));

        checkThrows("closed input throws IllegalStateException(\"Input closed\")",
                IllegalStateException.class, "Input closed",
                () -> makeUi("").readInt("> ", 1, 6));

        checkThrows("input that ends after a bad value also throws Input closed",
                IllegalStateException.class, "Input closed",
                () -> makeUi("abc\n").readInt("> ", 1, 6));
    }

    private static void testReadLine() throws Exception {
        ConsoleUI ui = makeUi("  hello  \n");
        checkEquals("text is trimmed", "hello", ui.readLine("Name: "));
        check("prompt is printed", output().contains("Name: "));

        ui = makeUi("\n");
        checkEquals("blank line returns an empty string", "", ui.readLine("> "));

        ui = makeUi("hello world\n");
        checkEquals("spaces inside the text are kept", "hello world", ui.readLine("> "));

        checkThrows("closed input throws Input closed",
                IllegalStateException.class, "Input closed",
                () -> makeUi("").readLine("> "));
    }

    private static void testMixingReads() throws Exception {
        ConsoleUI ui = makeUi("5\nhello world\n3\n");
        checkEquals("readInt first", 5, ui.readInt("> ", 1, 9));
        checkEquals("readLine right after readInt gets the NEXT line (no skipped input)",
                "hello world", ui.readLine("> "));
        checkEquals("readInt after readLine", 3, ui.readInt("> ", 1, 9));
    }

    private static void testConfirm() throws Exception {
        ConsoleUI ui = makeUi("y\nYES\nn\nNo\n  Y  \n");
        check("'y' is true", ui.confirm("Save?"));
        check("'YES' is true (any letter case)", ui.confirm("Save?"));
        check("'n' is false", !ui.confirm("Save?"));
        check("'No' is false (any letter case)", !ui.confirm("Save?"));
        check("'  Y  ' is true (trimmed)", ui.confirm("Save?"));
        check("prompt gets ' (y/n): ' added", output().contains("Save? (y/n): "));

        ui = makeUi("maybe\n\ny\n");
        check("invalid answers are ignored until y/n", ui.confirm("Continue?"));
        checkEquals("'Please type y or n.' shown for each bad answer", 2,
                count(output(), "Please type y or n."));

        checkThrows("closed input throws Input closed",
                IllegalStateException.class, "Input closed",
                () -> makeUi("").confirm("Sure?"));
    }

    private static void testPause() throws Exception {
        ConsoleUI ui = makeUi("\nnext\n");
        ui.pause();
        check("prints 'Press Enter to continue...'", output().contains("Press Enter to continue..."));
        checkEquals("consumes exactly ONE line", "next", ui.readLine(""));

        checkThrows("closed input throws Input closed",
                IllegalStateException.class, "Input closed",
                () -> makeUi("").pause());
    }

    private static void testBanner() throws Exception {
        ConsoleUI ui = makeUi("\nafter\n");
        ui.showBanner();
        check("shows the project name", output().contains("UCC Basic Input and Transaction Engine"));
        check("shows the Enter prompt", output().contains("[Press Enter to Continue.]"));
        checkEquals("consumes exactly ONE line (waits for Enter)", "after", ui.readLine(""));

        checkThrows("closed input throws Input closed",
                IllegalStateException.class, "Input closed",
                () -> makeUi("").showBanner());
    }

    private static void testMainMenu() throws Exception {
        ConsoleUI ui = makeUi("");   // no input at all: showMainMenu must not read anything
        ui.showMainMenu();
        String out = output();
        check("1. View Menu", out.contains("1. View Menu"));
        check("2. Place Order", out.contains("2. Place Order"));
        check("3. View Pending Orders", out.contains("3. View Pending Orders"));
        check("4. Process Next Order", out.contains("4. Process Next Order"));
        check("5. View Order History", out.contains("5. View Order History"));
        check("6. Exit", out.contains("6. Exit"));
    }

    private static void testPrintMenu() throws Exception {
        ConsoleUI ui = makeUi("");
        ui.printMenu(null);
        check("null menu prints 'The menu is empty.'", output().contains("The menu is empty."));

        ui = makeUi("");
        ui.printMenu(new MenuItem[0]);
        check("empty menu prints 'The menu is empty.'", output().contains("The menu is empty."));

        ui = makeUi("");
        ui.printMenu(sampleMenu());
        String out = output();
        check("shows the item names", out.contains("Siomai Rice") && out.contains("Spaghetti"));
        check("money has PHP and 2 decimals", out.contains("PHP 45.00") && out.contains("PHP 65.00"));
        check("big price is formatted too", out.contains("PHP 1234.50"));
        checkEquals("SOLD OUT appears exactly once (only the unavailable item)", 1, count(out, "SOLD OUT"));
        check("long name is shortened with '...'", out.contains("..."));
        check("the full long name is NOT shown", !out.contains("Chicken Adobo with Rice"));

        boolean aligned = true;
        int priceRows = 0;
        for (String line : lines(out)) {
            if (line.contains("PHP ")) {
                priceRows++;
                // price column is right-aligned and ends at index 40 (5+1+22+1+12 = 41 characters)
                if (line.length() < 41 || !Character.isDigit(line.charAt(40))) {
                    aligned = false;
                }
            }
        }
        checkEquals("4 item rows were printed", 4, priceRows);
        check("price column ends at the same position on every row", aligned);
    }

    private static void testPrintOrders() throws Exception {
        ConsoleUI ui = makeUi("");
        ui.printOrders("Pending Orders", null);
        check("null shows the title", output().contains("=== Pending Orders ==="));
        check("null prints '(no orders to show)'", output().contains("(no orders to show)"));

        ui = makeUi("");
        ui.printOrders("Completed Orders", new Order[0]);
        check("empty array prints '(no orders to show)'", output().contains("(no orders to show)"));

        ui = makeUi("");
        ui.printOrders("Pending Orders (oldest first)", sampleOrders());
        String out = output();
        check("shows the title", out.contains("=== Pending Orders (oldest first) ==="));
        check("header has all six column names",
                out.contains("Order No.") && out.contains("Item") && out.contains("Qty")
                        && out.contains("Unit Price") && out.contains("Total") && out.contains("Status"));
        check("order 7 shows as #0007", out.contains("#0007"));
        check("order 12 shows as #0012", out.contains("#0012"));
        check("order 123 shows as #0123", out.contains("#0123"));
        check("unit price and total are formatted", out.contains("PHP 65.00") && out.contains("PHP 130.00"));
        check("total comes from the Order (19.99 x 3 = 59.97)", out.contains("PHP 59.97"));
        check("shows both statuses", out.contains("PENDING") && out.contains("COMPLETED"));
        check("long name is shortened", out.contains("...") && !out.contains("Chicken Adobo with Rice"));
        check("orders are printed in the order given", out.indexOf("#0007") < out.indexOf("#0012")
                && out.indexOf("#0012") < out.indexOf("#0123"));

        boolean aligned = true;
        int rows = 0;
        for (String line : lines(out)) {
            if (line.startsWith("#")) {
                rows++;
                // Total column is right-aligned and ends at index 63 (10+1+22+1+4+1+12+1+12 = 64 characters)
                if (line.length() < 64 || !Character.isDigit(line.charAt(63))) {
                    aligned = false;
                }
            }
        }
        checkEquals("3 order rows were printed", 3, rows);
        check("Total column ends at the same position on every row", aligned);
    }

    private static void testPrintReceipt() throws Exception {
        ConsoleUI ui = makeUi("");
        ui.printReceipt(sampleOrders()[0], 3);
        String out = output();
        check("order number #0007", out.contains("#0007"));
        check("FULL item name (receipt does not shorten)", out.contains("Chicken Adobo with Rice"));
        check("quantity", out.contains("Quantity   : 2"));
        check("unit price", out.contains("PHP 65.00"));
        check("total", out.contains("PHP 130.00"));
        check("queue position", out.contains("Your position in the queue: 3"));
    }

    private static void testMessages() throws Exception {
        ConsoleUI ui = makeUi("");
        ui.showInfo("hello");
        checkEquals("showInfo", "[INFO] hello", output().trim());

        ui = makeUi("");
        ui.showSuccess("done");
        checkEquals("showSuccess", "[OK] done", output().trim());

        ui = makeUi("");
        ui.showWarning("careful");
        checkEquals("showWarning", "[WARNING] careful", output().trim());

        ui = makeUi("");
        ui.showError("oops");
        checkEquals("showError", "[ERROR] oops", output().trim());

        ui = makeUi("");
        ui.showGoodbye();
        check("showGoodbye says Goodbye", output().contains("Goodbye"));
    }

    private static void testClose() throws Exception {
        ConsoleUI ui = makeUi("hello\n");
        ui.close();
        try {
            ui.readLine("> ");
            check("reading after close() is refused", false);
        } catch (IllegalStateException e) {
            check("reading after close() is refused", true);
        }
    }

    // ------------------------------------------------------------------
    // MAIN
    // ------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        REAL_OUT.println("ConsoleUI tests");
        REAL_OUT.println("===============");

        group("readInt", TestFile::testReadInt);
        group("readLine", TestFile::testReadLine);
        group("mixing readInt and readLine", TestFile::testMixingReads);
        group("confirm", TestFile::testConfirm);
        group("pause", TestFile::testPause);
        group("showBanner", TestFile::testBanner);
        group("showMainMenu", TestFile::testMainMenu);
        group("printMenu", TestFile::testPrintMenu);
        group("printOrders", TestFile::testPrintOrders);
        group("printReceipt", TestFile::testPrintReceipt);
        group("showInfo / showSuccess / showWarning / showError / showGoodbye", TestFile::testMessages);
        group("close", TestFile::testClose);

        REAL_OUT.println();
        REAL_OUT.println("===============");
        REAL_OUT.println("Passed: " + passed + "    Failed: " + failed);
        REAL_OUT.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED - read the [FAIL] lines above");

        // ---- VISUAL CHECK: print the real screens so you can look at them ----
        System.setOut(REAL_OUT);
        System.setIn(REAL_IN);
        ConsoleUI ui = new ConsoleUI();   // only printing methods below, so it never waits for a key

        REAL_OUT.println();
        REAL_OUT.println("################ VISUAL CHECK (look at the alignment) ################");
        ui.showMainMenu();
        ui.printMenu(sampleMenu());
        ui.printOrders("Pending Orders (oldest first)", sampleOrders());
        ui.printReceipt(sampleOrders()[0], 3);
        ui.showInfo("This is an info message.");
        ui.showSuccess("This is a success message.");
        ui.showWarning("This is a warning message.");
        ui.showError("This is an error message.");
        ui.showGoodbye();
    }
}
