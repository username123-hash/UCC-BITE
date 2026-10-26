import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

// CsvManager - the ONLY class that knows how data is stored on disk.

public class CsvManager {

    public static final String DEFAULT_DATA_FOLDER = "data";

    private static final String MENU_HEADER = "id,name,price,available";
    private static final String ORDERS_HEADER =
            "orderId,itemId,itemName,unitPrice,quantity,total,status";

    private String dataFolder;
    private String menuPath;
    private String ordersPath;
    private ArrayList<String> warnings = new ArrayList<String>();

    // CONSTRUCTORS: just remember where the files are. No disk access.
    public CsvManager() {
        this(DEFAULT_DATA_FOLDER);
    }

    public CsvManager(String dataFolder) {
        this.dataFolder = dataFolder;
        this.menuPath = dataFolder + "/menu.csv";
        this.ordersPath = dataFolder + "/orders.csv";
    }

    // ensureDataFiles: make the folder + files if they are missing.
    public boolean ensureDataFiles() {
        try {
            // 1. folder (does nothing if it already exists)
            Files.createDirectories(Paths.get(dataFolder));

            // 2. menu.csv, only if missing (NEVER overwrite)
            Path menu = Paths.get(menuPath);
            if (!Files.exists(menu)) {
                List<String> lines = new ArrayList<String>();
                lines.add(MENU_HEADER);
                lines.add("1,Siomai Rice,45.00,true");
                lines.add("2,Chicken Adobo with Rice,65.00,true");
                lines.add("3,Pork Sisig,70.00,true");
                lines.add("4,Hotdog Sandwich,25.00,true");
                lines.add("5,Fried Chicken,60.00,true");
                lines.add("6,Spaghetti,40.00,false");
                lines.add("7,Iced Tea,20.00,true");
                lines.add("8,Bottled Water,15.00,true");
                Files.write(menu, lines, StandardCharsets.UTF_8);
            }

            // 3. orders.csv, only if missing (header only)
            Path orders = Paths.get(ordersPath);
            if (!Files.exists(orders)) {
                List<String> lines = new ArrayList<String>();
                lines.add(ORDERS_HEADER);
                Files.write(orders, lines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            warnings.add("Could not create data files: " + e.getMessage());
            return false;
        }
        return Files.exists(Paths.get(menuPath)) && Files.exists(Paths.get(ordersPath));
    }

    // loadMenu: menu.csv -> MenuItem[]
    public MenuItem[] loadMenu() {
        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        HashSet<Integer> seenIds = new HashSet<Integer>();

        List<String> lines = readLines(menuPath, "menu.csv");
        boolean headerSkipped = false;

        for (int i = 0; i < lines.size(); i++) {
            int lineNo = i + 1;                 // humans count from 1
            String line = lines.get(i);

            if (line.trim().isEmpty()) continue;            // blank line
            if (!headerSkipped) { headerSkipped = true; continue; } // header

            String[] f = line.split(",", -1);   // -1 keeps empty trailing fields
            if (f.length != 4) {
                warnings.add("menu.csv line " + lineNo + " skipped: expected 4 columns");
                continue;
            }
            for (int k = 0; k < f.length; k++) f[k] = f[k].trim();

            int id;
            double price;
            try {
                id = Integer.parseInt(f[0]);
            } catch (NumberFormatException e) {
                warnings.add("menu.csv line " + lineNo + " skipped: id is not a whole number");
                continue;
            }
            try {
                price = Double.parseDouble(f[2]);
            } catch (NumberFormatException e) {
                warnings.add("menu.csv line " + lineNo + " skipped: price is not a number");
                continue;
            }
            if (!f[3].equalsIgnoreCase("true") && !f[3].equalsIgnoreCase("false")) {
                warnings.add("menu.csv line " + lineNo + " skipped: available must be true or false");
                continue;
            }
            boolean available = f[3].equalsIgnoreCase("true");

            if (seenIds.contains(id)) {
                warnings.add("menu.csv line " + lineNo + " skipped: id " + id + " is repeated");
                continue;
            }

            try {
                items.add(new MenuItem(id, f[1], price, available));
                seenIds.add(id);
            } catch (IllegalArgumentException e) {
                warnings.add("menu.csv line " + lineNo + " skipped: " + e.getMessage());
            }
        }

        if (items.isEmpty()) {
            warnings.add("menu.csv has no valid menu items");
        }
        return items.toArray(new MenuItem[0]);
    }

    // loadOrders: orders.csv -> ArrayList<Order>, sorted by orderId
    public ArrayList<Order> loadOrders() {
        ArrayList<Order> result = new ArrayList<Order>();
        HashSet<Integer> seenIds = new HashSet<Integer>();

        List<String> lines = readLines(ordersPath, "orders.csv");
        boolean headerSkipped = false;

        for (int i = 0; i < lines.size(); i++) {
            int lineNo = i + 1;
            String line = lines.get(i);

            if (line.trim().isEmpty()) continue;
            if (!headerSkipped) { headerSkipped = true; continue; }

            String[] f = line.split(",", -1);
            if (f.length != 7) {
                warnings.add("orders.csv line " + lineNo + " skipped: expected 7 columns");
                continue;
            }
            for (int k = 0; k < f.length; k++) f[k] = f[k].trim();

            int orderId, itemId, quantity;
            double unitPrice;
            Order.Status status;

            try {
                orderId = Integer.parseInt(f[0]);
            } catch (NumberFormatException e) {
                warnings.add("orders.csv line " + lineNo + " skipped: orderId is not a whole number");
                continue;
            }
            try {
                itemId = Integer.parseInt(f[1]);
            } catch (NumberFormatException e) {
                warnings.add("orders.csv line " + lineNo + " skipped: itemId is not a whole number");
                continue;
            }
            // f[2] = itemName, used as text
            try {
                unitPrice = Double.parseDouble(f[3]);
            } catch (NumberFormatException e) {
                warnings.add("orders.csv line " + lineNo + " skipped: unitPrice is not a number");
                continue;
            }
            try {
                quantity = Integer.parseInt(f[4]);
            } catch (NumberFormatException e) {
                warnings.add("orders.csv line " + lineNo + " skipped: quantity is not a whole number");
                continue;
            }
            // f[5] = total -> IGNORED (Order recomputes it)
            try {
                status = Order.Status.valueOf(f[6].toUpperCase(Locale.US));
            } catch (IllegalArgumentException e) {
                warnings.add("orders.csv line " + lineNo + " skipped: unknown status '" + f[6] + "'");
                continue;
            }

            if (seenIds.contains(orderId)) {
                warnings.add("orders.csv line " + lineNo + " skipped: orderId " + orderId + " is repeated");
                continue;
            }

            try {
                result.add(new Order(orderId, itemId, f[2], unitPrice, quantity, status));
                seenIds.add(orderId);
            } catch (IllegalArgumentException e) {
                warnings.add("orders.csv line " + lineNo + " skipped: " + e.getMessage());
            }
        }

        // sort ascending by orderId (small list, so a simple comparator is fine)
        Collections.sort(result, new java.util.Comparator<Order>() {
            public int compare(Order a, Order b) {
                return Integer.compare(a.getOrderId(), b.getOrderId());
            }
        });
        return result;
    }

    // saveOrders: write ALL orders to orders.csv (safely)
    public boolean saveOrders(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders must not be null");
        }

        List<String> lines = new ArrayList<String>();
        lines.add(ORDERS_HEADER);
        for (Order o : orders) {
            lines.add(o.getOrderId() + ","
                    + o.getItemId() + ","
                    + o.getItemName() + ","
                    + String.format(Locale.US, "%.2f", o.getUnitPrice()) + ","
                    + o.getQuantity() + ","
                    + String.format(Locale.US, "%.2f", o.getTotal()) + ","
                    + o.getStatus().name());   // name() is already UPPERCASE
        }

        Path target = Paths.get(ordersPath);
        Path temp = Paths.get(ordersPath + ".tmp");
        try {
            // 1. write everything to a temp file first
            Files.write(temp, lines, StandardCharsets.UTF_8);
            // 2. then swap it in. If we crashed in step 1, the old file is untouched.
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            warnings.add("Could not save orders.csv: " + e.getMessage());
            try { Files.deleteIfExists(temp); } catch (IOException ignored) { }
            return false;
        }
    }

    // warnings
    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public void clearWarnings() {
        warnings.clear();
    }

    // PRIVATE HELPER: read a whole file as UTF-8 lines, strip the BOM.
    // Never returns null: if the file can't be read, returns an empty list
    // and adds a warning.
    private List<String> readLines(String path, String fileName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            if (!lines.isEmpty() && lines.get(0).startsWith("\uFEFF")) {
                lines.set(0, lines.get(0).substring(1));   // remove the BOM
            }
            return lines;
        } catch (IOException e) {
            warnings.add(fileName + " could not be read: " + e.getMessage());
            return new ArrayList<String>();
        }
    }
}