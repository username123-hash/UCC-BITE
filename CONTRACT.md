# UBITE Development Contract

**UBITE (UCC BITE): UCC Basic Input and Transaction Engine**

A Java command-line canteen ordering and queue management system | BSCS 2A, University of Caloocan City

Version 1.0 | Oct 21, 2026 | Prepared by Greg Andrei Bolivar, Project Leader and Integrator

---

## 1. Purpose and How to Use This Contract

This document is the single agreement every member codes against. For each Java file it defines what the file is responsible for, the exact public methods (the 'endpoints' other files may call), and how each method must behave. Because the signatures are fixed in advance, nobody has to wait for anyone else: write your own file and call the other files as if they already exist.

Rules:

1. The public signatures in Section 8 are frozen once everyone signs off (Section 14). Do not rename methods, reorder parameters, or change return types.
2. Private helper methods may be added freely. A new public method may be added only after telling the leader and recording it in the Change Log (Section 15).
3. Only `ConsoleUI.java` reads from or prints to the console. No other class may use `System.out`, `System.err`, or `Scanner`. Temporary test code is fine but must be removed before integration.
4. No class except `Main` may call `System.exit`.
5. Every owner must be able to explain their own file during the presentation.
6. If something in this contract is unclear or wrong, tell the leader instead of guessing. We fix the contract first, then the code.
7. After sign-off, this file in the GitHub repo (`UCC BITE/CONTRACT.md`) is the official copy. Changes are made through a pull request and logged in Section 15 (see Section 16).

## 2. Project Summary (from the approved proposal)

UBITE lets a customer view a menu, place an order (one menu item plus a quantity), and receive a unique order number. Each new order enters a First-In, First-Out (FIFO) queue, and the operator processes orders oldest first. Menu data and order records are saved in CSV files so they survive a restart.

What the project must demonstrate:

- **Array:** the menu is held in a `MenuItem[]`.
- **Queue:** our own array-based circular FIFO queue in `OrderQueue`. The `java.util` queue classes (`LinkedList`, `ArrayDeque`, `Queue`) must NOT be used inside it.
- **Object-oriented design:** separate classes, each with one clear responsibility.
- **File handling:** CSV reading and writing in `CsvManager`.
- **Validation and error handling:** bad input or bad files must never crash the program.

Out of scope (proposal Section 7): graphical, web, or mobile interface; accounts or authentication; ingredient inventory; digital payments or profit reports; delivery or pickup scheduling; multiple users or computers; databases.

## 3. Changes from the Proposal

| Item | Proposal | This contract | Reason |
|---|---|---|---|
| CLI / UI | Main coordinates the app and shows the CLI | New `ConsoleUI.java` does all console input and output. Main only coordinates | Keeps Main readable, and input-validation loops are written once. The proposal allows the class structure to be adjusted |
| Project name | UCC BITE | UBITE: UCC Basic Input and Transaction Engine (the repo folder stays UCC BITE; the Java project folder is named after the full name) | Group decision |
| Class name | CSVManager | CsvManager | File name must match class name exactly, and Java style prefers CsvManager |
| Order total | Calculated per order | Saved in orders.csv for readability but always recomputed on load | One source of truth, no inconsistent data |
| Order history | Not specified | Held in an ArrayList of Order in Main | Arrays remain for the menu and the queue as the proposal states |
| Order status | Text | Nested enum `Order.Status` (PENDING, COMPLETED) | Avoids typos |

## 4. Team and File Ownership

| File | Owner | Depends on | Used by |
|---|---|---|---|
| MenuItem.java | Ampater | nothing | Order, CsvManager, ConsoleUI, Main |
| Order.java | Jontilano | MenuItem | OrderQueue, CsvManager, ConsoleUI, Main |
| OrderQueue.java | Zaldivar | Order | Main |
| CsvManager.java (plus default menu.csv and orders.csv) | Dizon | MenuItem, Order | Main |
| ConsoleUI.java | Bolivar | MenuItem, Order | Main |
| Main.java and start.bat | Bolivar | all of the above | entry point |

## 5. Folder Structure and start.bat

```
UCC BITE/                                       (root of the GitHub repo)
  CONTRACT.md                                   (this file)
  .gitignore                                    (see "What .gitignore is" below)
  UCC Basic Input and Transaction Engine/       (the Java project folder, where start.bat lives)
    src/
      Main.java
      ConsoleUI.java
      MenuItem.java
      Order.java
      OrderQueue.java
      CsvManager.java
    data/
      menu.csv
      orders.csv                                (created at runtime, not committed to Git)
    bin/                                        (created by start.bat, holds compiled .class files, not committed)
    README.md                                   (added later, once the project is finished)
    start.bat
```

`start.bat` must, in this order:

1. Change to its own folder first, so relative paths like `data/menu.csv` work no matter where it is launched from.
2. Create `bin/` if it does not exist.
3. Compile every `.java` file in `src/` into `bin/`.
4. Stop with a readable message if compilation fails.
5. Run `Main` from `bin/`.
6. Pause at the end so the window does not close before errors can be read.

Requirements: a JDK (not just a JRE) with `javac` on the PATH. Write code that compiles on Java 8 and up: no `var`, no records, no text blocks, no switch expressions. Test `start.bat` on at least one other member's PC before the presentation.

The folder names contain spaces (`UCC BITE`, `UCC Basic Input and Transaction Engine`), so every path inside `start.bat` must be wrapped in double quotes. Otherwise the script breaks as soon as it is run from a path with spaces.

### What .gitignore is and why it is in the project

`.gitignore` is a plain text file in the repo root (`UCC BITE/`) that lists files and folders Git must not track. Ignored files are never committed, pushed, or shown in pull requests. It is in the project for three reasons:

1. `bin/` and `*.class` are generated by `start.bat` on every compile. Each member rebuilds them locally, so committing them only clutters the repo and causes pointless conflicts.
2. `data/orders.csv` changes every time someone runs the program. Committing it would push test orders to everyone and cause merge conflicts on every pull. `CsvManager` recreates it when it is missing. `data/menu.csv` is NOT ignored, because it is shared project data.
3. IDE and editor folders (`.idea/`, `.vscode/`, `*.iml`) are personal to each machine.

The `.gitignore` file itself is committed, so every member gets the same rules.

```
# Compiled output
bin/
*.class

# Runtime data (CsvManager recreates it)
**/data/orders.csv

# IDE and editor files
.idea/
*.iml
.vscode/
```

Notes:

- The Java project sits one folder below the repo root. `bin/` and `*.class` match at any depth, so they still work. `data/orders.csv` is written as `**/data/orders.csv` for the same reason: without the `**/`, the pattern would only match at the repo root and would miss `UCC Basic Input and Transaction Engine/data/orders.csv`.
- The name starts with a dot and has no extension. If it is created in Notepad, Windows may save it as `.gitignore.txt`, which Git ignores. The easiest way is to pick the Java `.gitignore` template when creating the repo on GitHub, then add the `orders.csv` line.
- `.gitignore` only affects files Git is not tracking yet. If `orders.csv` was already committed, run `git rm --cached` on it once to stop tracking it.

## 6. Global Conventions

- No `package` statement. Every file is in the default package, and each public class lives in a file with exactly the same name.
- Naming: PascalCase classes, camelCase methods and variables, UPPER_SNAKE_CASE constants. 4-space indentation.
- Every public method has a Javadoc-style comment: purpose, parameters, return value, and what happens on failure.
- All fields are private. Access is through getters.
- Money is a `double`, always shown and saved with exactly 2 decimals, always with a dot as the decimal separator regardless of the PC's regional settings.
- All CSV files are UTF-8.
- Invalid constructor or method arguments throw `IllegalArgumentException` with a clear message.
- No static mutable state (no global counters). State lives in objects created by Main.
- Methods that return arrays or lists never return `null` (return an empty one) unless the contract says null explicitly.

## 7. Data File Specification

### 7.1 menu.csv (one row = one MenuItem)

| Column | Type and rule |
|---|---|
| id | whole number greater than 0, unique |
| name | text, not blank, no commas |
| price | decimal greater than 0, dot as separator |
| available | true or false |

```
id,name,price,available
1,Siomai Rice,45.00,true
2,Chicken Adobo with Rice,65.00,true
```

Default seed menu written by `CsvManager` when menu.csv is missing: 1 Siomai Rice 45.00 true; 2 Chicken Adobo with Rice 65.00 true; 3 Pork Sisig 70.00 true; 4 Hotdog Sandwich 25.00 true; 5 Fried Chicken 60.00 true; 6 Spaghetti 40.00 false; 7 Iced Tea 20.00 true; 8 Bottled Water 15.00 true. Item 6 is intentionally unavailable so the sold-out path can be tested and demonstrated.

### 7.2 orders.csv (one row = one Order)

| Column | Type and rule |
|---|---|
| orderId | whole number greater than 0, unique |
| itemId | whole number greater than 0 |
| itemName | text copied from the menu at the time of ordering |
| unitPrice | decimal greater than 0, copied from the menu at the time of ordering |
| quantity | whole number greater than 0 |
| total | decimal, written for human readers. Ignored on load and recomputed as unitPrice x quantity |
| status | PENDING or COMPLETED (case-insensitive when loading, always written in uppercase) |

```
orderId,itemId,itemName,unitPrice,quantity,total,status
1,2,Chicken Adobo with Rice,65.00,2,130.00,COMPLETED
2,1,Siomai Rice,45.00,1,45.00,PENDING
```

Why the name and price are copied into the order: if the menu price changes later, old orders must still show what the customer actually paid.

### 7.3 Rules for both files

- The first non-blank line is the header and is skipped.
- Strip a UTF-8 BOM if present (Excel adds one when saving as CSV UTF-8).
- Skip blank lines. Trim spaces around every field. Quoted fields are not supported.
- A row with the wrong number of columns, an unparseable number, or a value rejected by the class constructor is skipped and recorded as a warning (file name, line number, reason). Loading never crashes.
- Orders are saved in ascending orderId order, which is also the order they were placed. Pending orders are restored to the queue in this order, which restores FIFO.
- Saving rewrites the whole file every time (header plus all rows). It is not appended.
- The next order number is not stored anywhere. After loading, it is the highest orderId plus 1, or 1 if there are no orders.
- `data/orders.csv` is runtime data and is not committed to Git. `CsvManager` recreates it when missing.

## 8. Class Contracts

### 8.1 MenuItem.java (Owner: Ampater)

**Purpose:** an immutable object representing one food item, i.e. one row of menu.csv.

**Private fields:** `int id`; `String name`; `double price`; `boolean available`.

```java
public MenuItem(int id, String name, double price, boolean available)
public int getId()
public String getName()
public double getPrice()
public boolean isAvailable()
public String toString()
```

**Behavior**

- The constructor trims the name. It throws `IllegalArgumentException` if id is 0 or less, name is null or blank, name contains a comma or a line break, or price is 0 or less.
- There are no setters. A MenuItem never changes after it is created.
- `toString` returns a short one-line debug string (id, name, price, availability). It is not used for screen layout.

**Must not:** read files, print anything, or know about Order or the queue.

**Owner self-test:** a valid item builds correctly; each invalid argument throws; the name ' Fried Chicken ' is stored as 'Fried Chicken'.

### 8.2 Order.java (Owner: Jontilano)

**Purpose:** one customer transaction, i.e. one row of orders.csv.

**Private fields:** `int orderId`; `int itemId`; `String itemName`; `double unitPrice`; `int quantity`; `double total`; `Status status` (the only field that can change).

```java
public enum Status { PENDING, COMPLETED }

public Order(int orderId, MenuItem item, int quantity)
public Order(int orderId, int itemId, String itemName, double unitPrice, int quantity, Status status)

public int getOrderId()
public int getItemId()
public String getItemName()
public double getUnitPrice()
public int getQuantity()
public double getTotal()
public Status getStatus()
public boolean isPending()
public void markCompleted()
public String toString()
```

**Behavior**

- **New-order constructor (used by Main):** copies the item's id, name, and price into the order, computes total as unitPrice x quantity rounded to 2 decimals, and sets status to PENDING. It does NOT check availability; Main does that. It throws `IllegalArgumentException` if orderId is 0 or less, item is null, or quantity is 0 or less.
- **Restore constructor (used only by CsvManager):** takes every stored field except total, computes the total the same way, and uses the given status. It applies the same kind of validation (ids above 0, name not blank and without commas, price above 0, quantity above 0, status not null).
- **markCompleted():** changes PENDING to COMPLETED. If the order is already COMPLETED it throws `IllegalStateException`.
- **isPending():** true when status is PENDING.
- `toString` returns a one-line debug string, not screen formatting.
- The maximum quantity (99) is a Main rule, not an Order rule. Order only requires quantity above 0.

**Must not:** read files, print anything, or hold a reference to the MenuItem after construction (it keeps copies of the values only).

**Owner self-test:** 65.00 x 2 = 130.00; 19.99 x 3 = 59.97 (rounding); new order is PENDING; markCompleted twice throws; quantity 0 and -1 throw; null item throws.

### 8.3 OrderQueue.java (Owner: Zaldivar)

**Purpose:** our own array-based circular queue holding pending orders in FIFO order.

**Private fields:** `Order[] items`; `int front` (index of the oldest order); `int size` (how many orders are stored).

```java
public static final int DEFAULT_CAPACITY = 100;

public OrderQueue()
public OrderQueue(int capacity)
public boolean enqueue(Order order)
public Order dequeue()
public Order peek()
public boolean isEmpty()
public boolean isFull()
public int size()
public int capacity()
public Order[] toArray()
public void clear()
```

**Behavior**

- The no-argument constructor uses `DEFAULT_CAPACITY`. A capacity of 0 or less throws `IllegalArgumentException`.
- **Circular logic:** the next free slot is (front + size) modulo capacity. Dequeuing moves front forward by one, modulo capacity. Track fullness and emptiness with the size counter, not by comparing front and rear. This avoids the classic 'full or empty?' confusion.
- **enqueue:** null throws `IllegalArgumentException`. If the queue is full it returns false and changes nothing. Otherwise it adds the order at the back and returns true. The queue never grows; the capacity is fixed.
- **dequeue:** removes and returns the oldest order, and clears that slot. Returns null if the queue is empty.
- **peek:** returns the oldest order without removing it. Returns null if empty.
- **toArray:** returns a new array of length `size()`, with index 0 being the oldest order. It must be correct even after the queue has wrapped around. Changing the returned array does not affect the queue. An empty queue returns a zero-length array, never null.
- **clear:** removes everything (set slots to null, reset front and size to 0).
- The queue stores references to the same Order objects that Main keeps in its history list. It never copies orders.

**Must not:** use `LinkedList`, `ArrayDeque`, `ArrayList`, or `java.util.Queue` inside; print anything; know about CSV or the menu.

**Owner self-test (most important):** enqueue 1, 2, 3 then dequeue returns 1, 2, 3 in that order. Fill to capacity: `isFull` is true and the next enqueue returns false. Wrap-around: capacity 3, enqueue A B C, dequeue A, enqueue D, then dequeues return B, C, D, and `toArray` gives the right order at every step. Dequeue and peek on an empty queue return null.

### 8.4 CsvManager.java (Owner: Dizon)

**Purpose:** the only class that knows how data is stored on disk. It loads and saves menu.csv and orders.csv and keeps a list of warnings about problems it met.

**Private fields:** `dataFolder`, `menuPath`, `ordersPath` (all text); `ArrayList<String>` of warnings.

```java
public static final String DEFAULT_DATA_FOLDER = "data";

public CsvManager()
public CsvManager(String dataFolder)
public boolean ensureDataFiles()
public MenuItem[] loadMenu()
public ArrayList<Order> loadOrders()
public boolean saveOrders(List<Order> orders)
public List<String> getWarnings()
public void clearWarnings()
```

**Behavior**

- The no-argument constructor uses the folder 'data'. Paths are relative to the working directory, which `start.bat` sets to the Java project folder (the folder that contains `start.bat`, `src/`, and `data/`).
- **ensureDataFiles:** creates the data folder if missing. Creates menu.csv (header plus the default seed menu from Section 7.1) if missing. Creates orders.csv (header only) if missing. It never overwrites an existing file. Returns true if both files exist and are usable afterwards, otherwise false with a warning explaining why.
- **loadMenu:** returns `MenuItem[]` in file order, following the rules in Section 7.3. It catches `IllegalArgumentException` and number-format errors per row, skips the bad row, and adds a warning. A repeated id is skipped with a warning. Never returns null: an unreadable or empty file gives a zero-length array plus a warning.
- **loadOrders:** returns an `ArrayList` of Order, sorted ascending by orderId, built with the restore constructor. The total column is ignored. Unknown status text is a skipped row. A repeated orderId is skipped with a warning. Never returns null.
- **saveOrders:** writes the header and one row per order, in the order given. It should write to a temporary file first and then replace orders.csv, so a crash mid-write cannot destroy the old data. Returns true on success. If it fails (for example the file is open in Excel and locked) it returns false and adds a warning; it never throws for I/O problems. A null list throws `IllegalArgumentException`.
- Numbers are written and parsed with a dot as the decimal separator (use an explicit US locale), so the files work on any PC.
- **getWarnings:** returns the accumulated warnings in order, as an unmodifiable list. **clearWarnings:** empties it. Warning text looks like: 'orders.csv line 7 skipped: quantity is not a whole number'.

**Must not:** print to the console, decide what to do about warnings (Main and ConsoleUI do that), call `System.exit`, or keep the orders in memory beyond a single call.

**Owner self-test:** delete the data folder and `ensureDataFiles` recreates it; a header-only orders.csv loads as empty; a row with letters in quantity is skipped and the warning names the line; a file saved with a BOM loads correctly; save followed by load gives identical orders; a locked file makes `saveOrders` return false.

### 8.5 ConsoleUI.java (Owner: Bolivar)

**Purpose:** everything the user sees and types. It formats output and collects validated input. It contains no business rules.

**Private fields:** one `Scanner` on `System.in` (the only Scanner in the whole program); constant `CURRENCY = "PHP "` (do not use the peso sign unless `start.bat` sets the console to UTF-8).

```java
public ConsoleUI()
public void showBanner()
public void showMainMenu()
public int readInt(String prompt, int min, int max)
public String readLine(String prompt)
public boolean confirm(String prompt)
public void pause()
public void printMenu(MenuItem[] menu)
public void printOrders(String title, Order[] orders)
public void printReceipt(Order order, int queuePosition)
public void showInfo(String message)
public void showSuccess(String message)
public void showWarning(String message)
public void showError(String message)
public void showGoodbye()
public void close()
```

**Behavior**

- **showBanner:** prints 'UBITE - UCC Basic Input and Transaction Engine' with a short subtitle.
- **showMainMenu:** prints the six options exactly as listed in Section 8.6 and reads nothing.
- **readInt:** prints the prompt, reads a whole line (always `nextLine`, never `nextInt`, to avoid the leftover-newline bug), and trims it. If it is not a whole number it shows 'Please enter a whole number.' and asks again. If it is outside min to max it shows 'Please enter a number from MIN to MAX.' and asks again. It returns only a valid value and never throws on bad input. If min is greater than max it throws `IllegalArgumentException`.
- **readLine:** prints the prompt and returns the trimmed line.
- **confirm:** asks 'prompt (y/n): ' and accepts y, yes, n, no in any letter case. Anything else asks again. Returns true for yes.
- **pause:** prints 'Press Enter to continue...' and waits.
- **printMenu:** a table with item number, name, and price (CURRENCY plus 2 decimals). Unavailable items are clearly marked SOLD OUT. An empty array shows 'The menu is empty.'
- **printOrders:** prints the title, then a table with Order No. (shown as #0007: four digits, zero-padded, display only), Item, Qty, Unit Price, Total, and Status. An empty array prints '(no orders to show)'. Long item names are shortened so the columns stay aligned.
- **printReceipt:** shown after an order is placed: order number, item, quantity, unit price, total, and 'Your position in the queue: N'.
- **showInfo, showSuccess, showWarning, showError:** print the message with the prefix [INFO], [OK], [WARNING], [ERROR].
- **showGoodbye:** closing message. **close:** closes the Scanner.
- **End of input:** if the input stream ends, `readInt`, `readLine`, `confirm`, and `pause` throw `IllegalStateException('Input closed')`. Main catches it at the top level and shuts down cleanly.

**Must not:** do price math, touch the queue, read or write files, decide program flow, or call `System.exit`.

**Owner self-test:** letters, blank input, negative numbers, and out-of-range numbers all re-prompt; mixing `readInt` and `readLine` does not skip input; table columns stay aligned for short and long item names.

### 8.6 Main.java (Owner: Bolivar)

**Purpose:** starts the program and coordinates the other classes. It contains the workflow and the business rules, but no file parsing, no queue logic, no `Scanner`, and no `System.out`.

**Private fields:** `ConsoleUI ui`; `CsvManager csv`; `OrderQueue queue`; `MenuItem[] menu`; `ArrayList<Order> orders` (the complete history, always in ascending orderId order); `int nextOrderId`.

**Constants:** `MAX_QUANTITY = 99`. Main menu numbers: 1 View Menu, 2 Place Order, 3 View Pending Orders, 4 Process Next Order, 5 View Order History, 6 Exit.

```java
public static void main(String[] args)
private void init()
private void run()
private void handleViewMenu()
private void handlePlaceOrder()
private void handleViewPending()
private void handleProcessNext()
private void handleViewHistory()
private void shutdown()
private MenuItem findMenuItemById(int id)
private int computeNextOrderId()
private void rebuildQueue()
private boolean saveAll()
```

**Behavior**

- **main:** creates the objects, calls `init`, then `run` inside a try/catch for `IllegalStateException` (input closed), then `shutdown`.
- **init:** see Section 9.1.
- **run:** loops: `showMainMenu`, `readInt(1 to 6)`, then calls the matching handler, until the user chooses Exit.
- **handleViewMenu:** `printMenu(menu)`, then `pause`.
- **handlePlaceOrder:** see Section 9.2.
- **handleViewPending:** `printOrders('Pending Orders (oldest first)', queue.toArray())`, then `pause`.
- **handleProcessNext:** see Section 9.3.
- **handleViewHistory:** collects the orders that are not pending (still in ascending order number) and calls `printOrders('Completed Orders', ...)`, then `pause`.
- **findMenuItemById:** searches the menu array and returns the matching MenuItem, or null if none.
- **computeNextOrderId:** highest orderId in orders plus 1, or 1 if the list is empty.
- **rebuildQueue:** clears the queue, then enqueues every pending order from orders in list order. If an enqueue returns false, shows a warning that the order could not be queued.
- **saveAll:** calls `csv.saveOrders(orders)`. If it returns false, shows an error that the data could not be saved (suggest closing orders.csv if it is open in another program) and returns false. The program keeps running with its in-memory data.
- **shutdown:** `saveAll`, `showGoodbye`, `ui.close`.

## 9. End-to-End Flows

### 9.1 Startup (init)

1. `showBanner`.
2. `csv.ensureDataFiles()`. If false, show an error with the warnings and stop the program.
3. `menu = csv.loadMenu()`; `orders = csv.loadOrders()`.
4. Show every warning from `csv.getWarnings()` with `showWarning`, then `clearWarnings`. Pause only if there were warnings.
5. `nextOrderId = computeNextOrderId()`.
6. `rebuildQueue()` (this is what restores pending orders after a restart).
7. If the menu is empty, show an error that orders cannot be placed until menu.csv is fixed. The program still runs.

### 9.2 Place an order

1. If the menu is empty, show an error and return.
2. `printMenu(menu)`.
3. Ask for the item number (0 to cancel) with `readInt`. If the item does not exist (`findMenuItemById` returns null) show an error and ask again. If it is not available show an error and ask again.
4. Ask for the quantity with `readInt(1 to MAX_QUANTITY)`.
5. If `queue.isFull()`, show an error and return (check this BEFORE creating the order, so order numbers are not wasted).
6. Show a summary (item, quantity, total) and `confirm()`. If the answer is no, show 'Order cancelled.' and return.
7. Create the order with `new Order(nextOrderId, item, quantity)`. Add it to orders and enqueue it in queue. Increase `nextOrderId` by 1.
8. `saveAll()`.
9. `printReceipt(order, queue.size())`.

### 9.3 Process the next order

1. If `queue.isEmpty()`, show info 'No pending orders.' and return.
2. Take the order with `queue.dequeue()`, then call `order.markCompleted()`.
3. `saveAll()`.
4. Show a success message with the order number and item that was processed.

### 9.4 Restart restoration

After closing and reopening: orders.csv contains every order with its status. `loadOrders` returns them in ascending order, `rebuildQueue` puts the PENDING ones back in the queue in the same order they were placed, completed ones appear in the history, and `nextOrderId` continues from the highest saved number.

## 10. Validation and Error Handling Matrix

| Situation | Detected by | Expected behavior |
|---|---|---|
| Menu choice not a number or out of range | ConsoleUI.readInt | Error message, asks again |
| Item number does not exist | Main | Error message, asks again |
| Item is sold out | Main | Error message, asks again |
| Quantity less than 1 or more than 99 | ConsoleUI.readInt (Order also rejects 0 or less) | Error message, asks again |
| Queue is full when ordering | Main (checks isFull first; enqueue returning false is the backup) | Error message, no order created |
| Process next with empty queue | Main | Info message, nothing changes |
| data folder or CSV files missing | CsvManager.ensureDataFiles | Recreated with defaults |
| Empty, malformed, or partly invalid CSV | CsvManager | Bad rows skipped, warnings collected, no crash |
| menu.csv has no valid rows | Main | Error shown, ordering blocked, program keeps running |
| Save fails (file locked, disk problem) | CsvManager returns false | Main shows an error and keeps running |
| Input stream closed | ConsoleUI throws IllegalStateException | Main saves and exits cleanly |

## 11. Working Without Waiting

Because every public signature is already defined, everyone can start today:

1. **Skeleton first:** create your file with the exact class name, constructors, and method signatures from Section 8, with placeholder bodies (return 0, false, null, or an empty array/list as the contract allows). Send it to the leader. This is Checkpoint 0.
2. **Compile together:** the leader puts all skeletons in `src/` and confirms everything compiles. From now on the whole team can compile even though most bodies are placeholders.
3. **Fill in your own logic** while the others do the same. Anything you need from another file already exists as a skeleton.
4. **Handy placeholders:** a CsvManager skeleton can return a hard-coded 3-item menu from `loadMenu`, an empty list from `loadOrders`, and true from `saveOrders`, so Main and ConsoleUI can already be tested. As soon as a real file is finished, it replaces its skeleton.
5. **Test your own class alone** with a small throwaway main method or test file (the self-test lists above). Remove it before integration.
6. **If you must change a signature,** do not just edit it. Tell the leader, agree on the change, update this contract, and record it in Section 15.

## 12. Schedule and Checkpoints (follows the proposal's 7-day plan)

| Day | Goal | Checkpoint |
|---|---|---|
| 1 | Contract review and sign-off, skeletons from everyone, project folder and sample CSV files set up | Checkpoint 0: everything compiles with placeholders |
| 2 | MenuItem and Order finished and self-tested. Leader starts ConsoleUI | Both pass their self-tests |
| 3 | OrderQueue finished and self-tested (including wrap-around) | FIFO confirmed |
| 4 | CsvManager finished and self-tested. Leader finishes ConsoleUI and Main handlers | Round-trip save and load works |
| 5 | Integration: replace all skeletons with real classes, run the full workflow including bad input and bad files | Complete workflow works |
| 6 | Debugging and stabilization: repeated orders, restarts, locked-file test. No new features | All acceptance tests in Section 13 pass |
| 7 | Presentation rehearsal, sample records prepared | Everyone can explain their file |

## 13. Acceptance Tests (from proposal Section 12)

| Proposal criterion | How to check it | Mainly depends on |
|---|---|---|
| Menu shows correct names and prices | View Menu against menu.csv | CsvManager, MenuItem, ConsoleUI |
| Invalid selections and zero or negative quantities rejected without crashing | Try letters, 0, -3, 999, a missing item, the sold-out item | ConsoleUI, Main, Order |
| Unique order ID and correct total | Place several orders and check numbers and arithmetic | Order, Main |
| Orders queued in the order placed | Place A, B, C and view pending | OrderQueue |
| Oldest pending order processed first | Process three times and check A, B, C | OrderQueue, Main |
| Completed orders stay in history | View Order History after processing | Main, Order |
| Records retained after restart | Close, reopen, check orders.csv and history | CsvManager |
| Pending orders restored on restart | Leave two pending, restart, process them in order | CsvManager, Main, OrderQueue |
| Empty queues and missing, empty, or malformed CSV handled | Process with empty queue; delete data folder; add a bad row; empty both files | Main, CsvManager |
| The group can explain how it all works together | Section 14 | Everyone |

## 14. Presentation Readiness and Sign-off

Each member should be ready to explain, in plain words:

- **Ampater (MenuItem):** how a menu row becomes an object, and how the MenuItem array is used to look up an item by its number.
- **Jontilano (Order):** why an order copies the item name and price, how the total is computed, and how the status changes.
- **Zaldivar (OrderQueue):** how a circular array works, how front and size move, and why FIFO is guaranteed.
- **Dizon (CsvManager):** how CSV rows are read and written, how bad rows are handled, and how restart restoration works.
- **Bolivar (Main and ConsoleUI):** the overall workflow, why the UI is separated from Main, and how the classes are connected.

Sign-off (each member confirms they have read the contract and accept the signatures for their file):

| Member | File | Agreed |
|---|---|---|
| Ampater, Railhie | MenuItem.java | [ ] |
| Jontilano, Dan Carlo | Order.java | [ ] |
| Zaldivar, Ivankaizer | OrderQueue.java | [ ] |
| Dizon, Lorraine | CsvManager.java and data files | [ ] |
| Bolivar, Greg Andrei | Main.java, ConsoleUI.java, start.bat | [ ] |

## 15. Change Log

| Date | Change | Approved by |
|---|---|---|
| Oct 21, 2026 | Version 1.0 created | Bolivar |
|  |  |  |
|  |  |  |

## 16. Git Workflow

- The repo root is the `UCC BITE` folder. It holds `CONTRACT.md`, `.gitignore`, and the Java project folder `UCC Basic Input and Transaction Engine`.
- The leader pushes the skeleton files first (Checkpoint 0) so everyone starts from the same base.
- The `main` branch must always compile. Nobody pushes broken code directly to `main`.
- Each member works on their own branch (for example `feature/order-queue`), then opens a pull request. The leader reviews and merges.
- Pull the latest `main` before every work session. Commit small and often.
- `.gitignore` excludes `bin/`, compiled `.class` files, and `data/orders.csv`. `data/menu.csv` is committed.
- Contract changes are made by pull request on this file, together with a new row in Section 15.
