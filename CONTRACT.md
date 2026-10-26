# UBITE Development Contract

**UBITE (UCC BITE): UCC Basic Input and Transaction Engine**

A Java command-line canteen ordering and queue management system | BSCS 2A, University of Caloocan City

Version 1.2 | Oct 25, 2026 | Prepared by Greg Andrei Bolivar, Project Leader and Integrator

---

## 1. Purpose and Rules

This document is the single agreement every member codes against. It fixes what each file is responsible for and the exact public methods other files may call, so everyone can write their own file without waiting for the others.

1. The public signatures in Section 8 are frozen once everyone signs off (Section 14). Do not rename methods, reorder parameters, or change return types. Private helper methods are free. A new public method needs the leader's approval and a row in the Change Log (Section 15).
2. Only `Main.java` reads from or prints to the console (`Scanner`, `System.out`). Temporary test code is fine but must be removed before integration.
3. Every owner must be able to explain their own file during the presentation.
4. If something here is unclear or wrong, tell the leader. We fix the contract first, then the code.

## 2. Project Summary

UBITE lets a customer view a menu, place an order (one menu item plus a quantity), and receive a unique order number. Each new order enters a First-In, First-Out (FIFO) queue, and the operator processes orders oldest first. Menu data and orders are saved in CSV files so they survive a restart.

What the project must demonstrate:

- **Array:** the menu is held in a `MenuItem[]`.
- **Queue:** our own array-based circular FIFO queue in `OrderQueue`. `java.util` queue and list classes must NOT be used inside it.
- **Object-oriented design:** separate classes, each with one clear responsibility.
- **File handling:** CSV reading and writing in `CsvManager`.
- **Basic validation:** bad input or bad CSV rows must not crash the program.

Out of scope: graphical, web, or mobile interface; accounts; ingredient inventory; payments or profit reports; delivery scheduling; multiple users; databases.

## 3. Changes from the Proposal

| Item | Proposal | This contract |
|---|---|---|
| Console UI | Main coordinates the app and shows the CLI | Same: all console input and output is inside `Main.java`. (Version 1.0 planned a separate `ConsoleUI.java`; it was merged into Main in version 1.1) |
| Class name | CSVManager | CsvManager (file name must match the class name, and Java style prefers CsvManager) |
| Order total | Calculated per order | Saved in orders.csv for readability but always recomputed on load |
| Order history | Not specified | Kept in an `ArrayList<Order>` in Main. Arrays remain for the menu and the queue |
| Order status | Text | Nested enum `Order.Status` (PENDING, COMPLETED) |

## 4. Team and File Ownership

| File | Owner | Depends on | Used by |
|---|---|---|---|
| MenuItem.java | Ampater | nothing | Order, CsvManager, Main |
| Order.java | Jontilano | MenuItem | OrderQueue, CsvManager, Main |
| OrderQueue.java | Zaldivar | Order | Main |
| CsvManager.java (plus default menu.csv and orders.csv) | Dizon | MenuItem, Order | Main |
| Main.java (including all console input and output) and start.bat | Bolivar | all of the above | entry point |

## 5. Folder Structure and start.bat

```
UCC BITE/                                       (root of the GitHub repo)
  CONTRACT.md
  .gitignore
  UCC Basic Input and Transaction Engine/       (the Java project folder, where start.bat lives)
    src/
      Main.java
      MenuItem.java
      Order.java
      OrderQueue.java
      CsvManager.java
    data/
      menu.csv
      orders.csv                                (created at runtime, not committed)
    bin/                                        (created by start.bat, not committed)
    start.bat
```

`start.bat` must, in this order: change to its own folder, create `bin/` if missing, compile every `.java` file in `src/` into `bin/`, stop with a readable message if compilation fails, run `Main` from `bin/`, and pause at the end so the window does not close.

Requirements: a JDK with `javac` on the PATH, and code that compiles on Java 8 and up (no `var`, records, text blocks, or switch expressions). The folder names contain spaces, so every path inside `start.bat` must be in double quotes. Test `start.bat` on at least one other member's PC before the presentation, and check that the screen clears properly there (if raw characters appear instead, tell the leader).

`.gitignore` (in the repo root, committed) keeps generated and personal files out of Git:

```
bin/
*.class
**/data/orders.csv
.idea/
*.iml
.vscode/
```

`data/menu.csv` is NOT ignored because it is shared project data. `orders.csv` changes every run and `CsvManager` recreates it when missing.

## 6. Global Conventions

- No `package` statement. Each public class lives in a file with exactly the same name.
- PascalCase classes, camelCase methods and variables, UPPER_SNAKE_CASE constants.
- All fields are private. Access is through getters.
- Money is a `double`, shown and saved with exactly 2 decimals and a dot as the decimal separator.
- All CSV files are UTF-8.
- Invalid constructor arguments throw `IllegalArgumentException`.
- Methods that return arrays or lists never return `null`.

## 7. Data File Specification

### 7.1 menu.csv (one row = one MenuItem)

| Column | Rule |
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

Default menu written by `CsvManager` when menu.csv is missing: 1 Siomai Rice 45.00 true; 2 Chicken Adobo with Rice 65.00 true; 3 Pork Sisig 70.00 true; 4 Hotdog Sandwich 25.00 true; 5 Fried Chicken 60.00 true; 6 Spaghetti 40.00 false; 7 Iced Tea 20.00 true; 8 Bottled Water 15.00 true. Item 6 is intentionally unavailable so the sold-out path can be tested.

### 7.2 orders.csv (one row = one Order)

| Column | Rule |
|---|---|
| orderId | whole number greater than 0, unique |
| itemId | whole number greater than 0 |
| itemName | text copied from the menu at the time of ordering |
| unitPrice | decimal greater than 0, copied from the menu at the time of ordering |
| quantity | whole number greater than 0 |
| total | decimal, for human readers only. Ignored on load and recomputed as unitPrice x quantity |
| status | PENDING or COMPLETED (case-insensitive when loading, written in uppercase) |

```
orderId,itemId,itemName,unitPrice,quantity,total,status
1,2,Chicken Adobo with Rice,65.00,2,130.00,COMPLETED
2,1,Siomai Rice,45.00,1,45.00,PENDING
```

The name and price are copied into the order so old orders still show what the customer actually paid if the menu price changes later.

### 7.3 Rules for both files

- The first line is a header and is skipped. Blank lines are skipped. Fields are trimmed. Quoted fields are not supported.
- A bad row (wrong number of columns, an unparseable number, or a value the class constructor rejects) is skipped and recorded as a warning with the file name, line number, and reason. Loading never crashes.
- Orders are saved in ascending orderId order, which is also the order they were placed. Pending orders are restored to the queue in this order, which restores FIFO.
- Saving rewrites the whole file (header plus all rows).
- The next order number is not stored. After loading, it is the highest orderId plus 1, or 1 if there are no orders.

## 8. Class Contracts

### 8.1 MenuItem.java (Owner: Ampater)

**Purpose:** an immutable object for one food item, i.e. one row of menu.csv.

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

- The constructor trims the name. It throws `IllegalArgumentException` if id is 0 or less, the name is null, blank, or contains a comma, or the price is 0 or less.
- There are no setters.
- `toString` returns a short one-line debug string.

**Must not:** read files or print anything.

**Self-test:** a valid item builds correctly; each invalid argument throws; ' Fried Chicken ' is stored as 'Fried Chicken'.

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

- **New-order constructor (used by Main):** copies the item's id, name, and price, computes total as unitPrice x quantity rounded to 2 decimals, and sets status to PENDING. It does not check availability (Main does). It throws `IllegalArgumentException` if orderId is 0 or less, item is null, or quantity is 0 or less.
- **Restore constructor (used only by CsvManager):** takes every stored field except total, computes the total the same way, and uses the given status. It applies the same kind of validation (ids above 0, name not blank, price above 0, quantity above 0, status not null).
- **markCompleted:** sets the status to COMPLETED. **isPending:** true when the status is PENDING.
- The maximum quantity (99) is a Main rule, not an Order rule.

**Must not:** read files, print anything, or keep a reference to the MenuItem (it keeps copies of the values only).

**Self-test:** 65.00 x 2 = 130.00; 19.99 x 3 = 59.97; a new order is PENDING; `markCompleted` makes it COMPLETED; quantity 0 and -1 throw; a null item throws.

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
- **Circular logic:** the next free slot is (front + size) modulo capacity. Dequeuing moves front forward by one, modulo capacity. Fullness and emptiness are tracked with the size counter.
- **enqueue:** if the queue is full, returns false and changes nothing. Otherwise adds the order at the back and returns true. The capacity is fixed; the queue never grows.
- **dequeue:** removes and returns the oldest order, or null if empty. **peek:** returns the oldest order without removing it, or null if empty.
- **toArray:** returns a new array of length `size()`, oldest order first, correct even after the queue has wrapped around. An empty queue returns a zero-length array.
- **clear:** removes everything.
- The queue stores references to the same Order objects that Main keeps in its history list. It never copies orders.

**Must not:** use `LinkedList`, `ArrayDeque`, `ArrayList`, or `java.util.Queue`, or print anything.

**Self-test (most important):** enqueue 1, 2, 3 then dequeue returns 1, 2, 3. Fill to capacity: `isFull` is true and the next enqueue returns false. Wrap-around: capacity 3, enqueue A B C, dequeue A, enqueue D, then dequeues return B, C, D, and `toArray` gives the right order at every step. Dequeue and peek on an empty queue return null.

### 8.4 CsvManager.java (Owner: Dizon)

**Purpose:** the only class that knows how data is stored on disk. It loads and saves menu.csv and orders.csv and keeps a list of warnings about rows it skipped.

**Private fields:** `dataFolder`, `menuPath`, `ordersPath` (text); an `ArrayList<String>` of warnings.

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

- The no-argument constructor uses the folder 'data'. Paths are relative to the working directory, which `start.bat` sets to the Java project folder.
- **ensureDataFiles:** creates the data folder if missing, creates menu.csv (header plus the default menu from Section 7.1) if missing, and creates orders.csv (header only) if missing. It never overwrites an existing file. Returns true if both files exist afterwards, otherwise false with a warning.
- **loadMenu:** returns a `MenuItem[]` in file order, following Section 7.3. A bad row or a repeated id is skipped with a warning. Never returns null: an unreadable or empty file gives a zero-length array.
- **loadOrders:** returns an `ArrayList<Order>` sorted ascending by orderId, built with the restore constructor. The total column is ignored. An unknown status or a repeated orderId is skipped with a warning. Never returns null.
- **saveOrders:** writes the header and one row per order, in the order given. Returns true on success. If the file cannot be written (for example it is open in Excel) it returns false and adds a warning; it does not throw for file problems.
- Numbers are written and parsed with a dot as the decimal separator.
- **getWarnings:** returns the warnings collected so far, for example 'orders.csv line 7 skipped: quantity is not a whole number'. **clearWarnings:** empties the list.

**Must not:** print to the console or call `System.exit`. Showing warnings is Main's job.

**Self-test:** deleting the data folder and calling `ensureDataFiles` recreates it; a header-only orders.csv loads as empty; a row with letters in quantity is skipped and the warning names the line; save followed by load gives identical orders; a locked file makes `saveOrders` return false.

### 8.5 Console input and output

Part of `Main.java` (see 8.6). The separate `ConsoleUI.java` from version 1.0 no longer exists.

### 8.6 Main.java (Owner: Bolivar)

**Purpose:** starts the program, coordinates the other classes, and does all console input and output. It contains the workflow and the business rules. It does no file parsing and no queue logic.

**Fields:** `CsvManager csv`; `OrderQueue queue`; `MenuItem[] menu`; `ArrayList<Order> orders` (the complete history, always in ascending orderId order); `int nextOrderId`; one `Scanner`.

**Constants:** `MAX_QUANTITY = 99`; currency text `"PHP "`.

**Public method:** `public static void main(String[] args)` only. Everything else in Main is private and is the owner's own business: how it is organized or split into methods is free, as long as the screens and flows below behave as written.

**Main menu** (exactly these numbers): 1 View Menu, 2 Place Order, 3 View Pending Orders, 4 Process Next Order, 5 View Order History, 6 Exit. The menu repeats until the user chooses Exit. Choosing a number that is not a whole number from 1 to 6 shows an error and asks again.

**What each choice does**

- **1 View Menu:** shows a table of item number (the item's id), name, and price (`PHP ` plus 2 decimals). Unavailable items are marked SOLD OUT. An empty menu shows 'The menu is empty.'
- **2 Place Order:** see Section 9.2.
- **3 View Pending Orders:** shows `queue.toArray()` (oldest first) as an orders table.
- **4 Process Next Order:** see Section 9.3.
- **5 View Order History:** shows the orders that are not pending, in ascending order number, as an orders table.
- **6 Exit:** saves the orders with `csv.saveOrders(orders)`, shows a goodbye message, and ends.

**Orders table:** one row per order with order number, item, quantity, unit price, total, and status. All values come from the Order getters. An empty list shows '(no orders to show)'.

**Screens:** Main clears the console before each new screen (the main menu and the result of each menu choice), so every screen looks like a separate page. Because the clear wipes the old text, each result ends with a pause ('Press Enter to continue...') so it can be read first. Clearing uses the ANSI escape code `"\033[H\033[2J"`, kept in one helper method (`ClearCMDScreen` in the current code) so the rest of Main just calls it.

**Saving:** after every order placed or processed, Main calls `csv.saveOrders(orders)`. If it returns false, Main shows an error (suggest closing orders.csv if it is open in another program) and keeps running with its in-memory data.

**Must not:** parse CSV, implement queue logic, or use `java.util` queue classes.

**Self-test:** letters, blank input, negative numbers, and out-of-range numbers all re-prompt without crashing; mixing number input and text input does not skip a line; every screen can be read before it is replaced.

## 9. End-to-End Flows

### 9.1 Startup

1. Show the program banner.
2. `csv.ensureDataFiles()`. If false, show an error with the warnings and stop the program.
3. `menu = csv.loadMenu()`; `orders = csv.loadOrders()`.
4. Show every warning from `csv.getWarnings()`, then `csv.clearWarnings()`.
5. `nextOrderId` = highest orderId in orders plus 1, or 1 if there are none.
6. Rebuild the queue: clear it, then enqueue every pending order from orders in list order. This is what restores pending orders after a restart.
7. If the menu is empty, show an error that orders cannot be placed until menu.csv is fixed. The program still runs.

### 9.2 Place an order

1. If the menu is empty, show an error and return.
2. Show the menu.
3. Ask for the item number (0 to cancel). If the item does not exist or is sold out, show an error and ask again.
4. Ask for the quantity, from 1 to `MAX_QUANTITY`.
5. If `queue.isFull()`, show an error and return. Check this BEFORE creating the order so order numbers are not wasted.
6. Show a summary (item, quantity, total) and ask the customer to confirm (y/n). If no, show 'Order cancelled.' and return. The total must come from an Order or MenuItem value, so Main does no price math.
7. Create the order with `new Order(nextOrderId, item, quantity)`, add it to orders, enqueue it in queue, and increase `nextOrderId` by 1.
8. Save the orders.
9. Show a receipt: order number, item, quantity, unit price, total, and 'Your position in the queue: N' (use `queue.size()`).

### 9.3 Process the next order

1. If `queue.isEmpty()`, show 'No pending orders.' and return.
2. `order = queue.dequeue()`, then `order.markCompleted()`. The queue and the orders list hold the same Order object, so the history updates too.
3. Save the orders.
4. Show a message with the order number and item that was processed.

### 9.4 Restart restoration

After closing and reopening: orders.csv contains every order with its status. `loadOrders` returns them in ascending order, the startup step in 9.1 puts the PENDING ones back in the queue in the order they were placed, completed ones appear in the history, and `nextOrderId` continues from the highest saved number.

## 10. Validation and Error Handling

Keep it simple: the program must not crash on the situations below.

| Situation | Handled by | Expected behavior |
|---|---|---|
| Menu choice or quantity not a number, or out of range | Main | Error message, asks again |
| Item number does not exist, or item is sold out | Main | Error message, asks again |
| Queue is full when ordering | Main | Error message, no order created |
| Process next with an empty queue | Main | Info message, nothing changes |
| data folder or CSV files missing | CsvManager | Recreated with defaults |
| Empty or partly invalid CSV | CsvManager | Bad rows skipped, warnings collected, no crash |
| menu.csv has no valid rows | Main | Error shown, ordering blocked, program keeps running |
| Save fails (for example file locked) | CsvManager returns false | Main shows an error and keeps running |

## 11. Working Without Waiting

Because every public signature is already defined, everyone can start right away:

1. **Skeleton first:** create your file with the exact class name, constructors, and method signatures from Section 8, with placeholder bodies (return 0, false, null, or an empty array or list). Send it to the leader. This is Checkpoint 0.
2. **Compile together:** the leader puts all skeletons in `src/` and confirms everything compiles. After that, fill in your own logic while the others do the same.
3. **Test your class alone** with a small throwaway main method (the self-tests in Section 8). Remove it before integration.
4. **If you must change a signature,** tell the leader first, update this contract, and record it in Section 15.

## 12. Schedule and Checkpoints

| Day | Goal | Checkpoint |
|---|---|---|
| 1 | Contract sign-off, skeletons from everyone, project folder and sample CSV files set up | Checkpoint 0: everything compiles with placeholders |
| 2 | MenuItem and Order finished and self-tested. Leader starts Main | Both pass their self-tests |
| 3 | OrderQueue finished and self-tested (including wrap-around) | FIFO confirmed |
| 4 | CsvManager finished and self-tested. Leader finishes Main | Round-trip save and load works |
| 5 | Integration: replace all skeletons with real classes, run the full workflow including bad input and bad files | Complete workflow works |
| 6 | Debugging and stabilization. No new features | All acceptance tests in Section 13 pass |
| 7 | Presentation rehearsal | Everyone can explain their file |

## 13. Acceptance Tests

| Criterion | How to check it | Mainly depends on |
|---|---|---|
| Menu shows correct names and prices | View Menu against menu.csv | CsvManager, MenuItem, Main |
| Invalid selections and zero or negative quantities rejected without crashing | Try letters, 0, -3, 999, a missing item, the sold-out item | Main, Order |
| Unique order ID and correct total | Place several orders and check numbers and arithmetic | Order, Main |
| Orders queued in the order placed | Place A, B, C and view pending | OrderQueue |
| Oldest pending order processed first | Process three times and check A, B, C | OrderQueue, Main |
| Completed orders stay in history | View Order History after processing | Main, Order |
| Records retained after restart | Close, reopen, check orders.csv and history | CsvManager |
| Pending orders restored on restart | Leave two pending, restart, process them in order | CsvManager, Main, OrderQueue |
| Empty queue and missing, empty, or malformed CSV handled | Process with an empty queue; delete the data folder; add a bad row; empty both files | Main, CsvManager |
| The group can explain how it all works together | Section 14 | Everyone |

## 14. Presentation Readiness and Sign-off

Each member should be ready to explain, in plain words:

- **Ampater (MenuItem):** how a menu row becomes an object, and how the MenuItem array is used to look up an item by its number.
- **Jontilano (Order):** why an order copies the item name and price, how the total is computed, and how the status changes.
- **Zaldivar (OrderQueue):** how a circular array works, how front and size move, and why FIFO is guaranteed.
- **Dizon (CsvManager):** how CSV rows are read and written, how bad rows are handled, and how restart restoration works.
- **Bolivar (Main):** the overall workflow and how the classes are connected.

Sign-off (each member confirms they have read the contract and accept the signatures for their file):

| Member | File | Agreed |
|---|---|---|
| Ampater, Railhie | MenuItem.java | [ ] |
| Jontilano, Dan Carlo | Order.java | [ ] |
| Zaldivar, Ivankaizer | OrderQueue.java | [ ] |
| Dizon, Lorraine | CsvManager.java and data files | [ ] |
| Bolivar, Greg Andrei | Main.java, start.bat | [ ] |

## 15. Change Log

| Date | Change | Approved by |
|---|---|---|
| Oct 21, 2026 | Version 1.0 created | Bolivar |
| Oct 25, 2026 | Version 1.1: `ConsoleUI.java` merged into `Main.java` | Bolivar |
| Oct 25, 2026 | Version 1.2: contract simplified. No public signature changed. Removed Main's private method list and UI formatting details, closed-input handling, BOM handling, temporary-file saving, and several extra validation rules | Bolivar |
|  |  |  |

## 16. Git Workflow

- The repo root is the `UCC BITE` folder. It holds `CONTRACT.md`, `.gitignore`, and the Java project folder.
- The leader pushes the skeleton files first (Checkpoint 0) so everyone starts from the same base.
- The `main` branch must always compile. Each member works on their own branch (for example `feature/order-queue`) and opens a pull request. The leader reviews and merges.
- Pull the latest `main` before every work session. Commit small and often.
- Contract changes are made by pull request on this file, together with a new row in Section 15.
