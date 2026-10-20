/**
 * OrderQueue - our own array-based circular FIFO queue of pending orders.
 *
 * OWNER      : Zaldivar
 * CONTRACT   : Section 8.3 (class contract), Section 2 (why we build our own queue)
 * DEPENDS ON : Order
 * USED BY    : Main
 *
 * PRIVATE FIELDS (Section 8.3)
 *   Order[] items; int front (index of the oldest order); int size (how many stored)
 *
 * PUBLIC API (frozen signatures, Section 8.3)
 *   public static final int DEFAULT_CAPACITY = 100;
 *
 *   public OrderQueue()
 *   public OrderQueue(int capacity)
 *   public boolean enqueue(Order order)
 *   public Order dequeue()
 *   public Order peek()
 *   public boolean isEmpty()
 *   public boolean isFull()
 *   public int size()
 *   public int capacity()
 *   public Order[] toArray()
 *   public void clear()
 *
 * BEHAVIOR (Section 8.3 "Behavior")
 *   - No-arg constructor uses DEFAULT_CAPACITY. Capacity <= 0 throws
 *     IllegalArgumentException.
 *   - Circular logic: next free slot = (front + size) % capacity;
 *     dequeue moves front forward by one, modulo capacity. Fullness and
 *     emptiness are tracked with the size counter, not front vs rear.
 *   - enqueue: null throws IllegalArgumentException; full returns false and
 *     changes nothing; otherwise adds at the back and returns true.
 *     Capacity is fixed, the queue never grows.
 *   - dequeue: removes and returns the oldest order and clears that slot;
 *     null if empty.
 *   - peek: oldest order without removing it; null if empty.
 *   - toArray: NEW array of length size(), index 0 = oldest, correct even after
 *     wrap-around; changing it does not affect the queue; empty queue gives a
 *     zero-length array, never null.
 *   - clear: set slots to null, reset front and size to 0.
 *   - Stores references to the SAME Order objects Main keeps in its history list.
 *     Never copies orders.
 *
 * MUST NOT (Section 8.3): use LinkedList, ArrayDeque, ArrayList or
 *   java.util.Queue inside; print anything; know about CSV or the menu.
 *
 * SELF-TEST (Section 8.3, most important): enqueue 1,2,3 -> dequeue 1,2,3;
 *   fill to capacity: isFull true and next enqueue returns false; wrap-around
 *   (capacity 3: enqueue A B C, dequeue A, enqueue D, dequeues give B C D,
 *   toArray right at every step); dequeue/peek on empty return null.
 *
 * ALSO SEE: Section 13 (FIFO acceptance tests), Section 14 (explain how front
 *   and size move, and why FIFO is guaranteed).
 */
public class OrderQueue {

    // Capacity used by the no-argument constructor.
    public static final int DEFAULT_CAPACITY = 100;

    // ------------------------------------------------------------------
    // PRIVATE FIELDS (Section 8.3)
    // ------------------------------------------------------------------
    private Order[] items;   // the circular array (fixed length = capacity)
    private int front;       // index of the OLDEST order
    private int size;        // how many orders are stored right now

    /**
     * Creates an empty queue with DEFAULT_CAPACITY (100) slots.
     * Same as calling OrderQueue(DEFAULT_CAPACITY).
     */
    public OrderQueue() {
        // TODO (Zaldivar): call this(DEFAULT_CAPACITY).
    }

    /**
     * Creates an empty queue with a fixed number of slots.
     *
     * HOW IT MUST BEHAVE
     *   - capacity <= 0 throws IllegalArgumentException.
     *   - items = new Order[capacity]; front = 0; size = 0.
     *   - The capacity never changes afterwards (the queue never grows).
     *
     * @param capacity maximum number of orders the queue can hold (greater than 0)
     * @throws IllegalArgumentException if capacity is 0 or less
     */
    public OrderQueue(int capacity) {
        // TODO (Zaldivar): validate, create the array, set front and size to 0.
    }

    /**
     * Adds an order at the BACK of the queue.
     *
     * HOW IT MUST BEHAVE
     *   1. order == null throws IllegalArgumentException.
     *   2. If the queue is full, return false and change NOTHING.
     *   3. The next free slot is (front + size) % capacity. Put the order
     *      there (store the same reference, never a copy), then size++.
     *   4. Return true.
     *
     * @param order the order to add (not null)
     * @return true if added, false if the queue was full
     * @throws IllegalArgumentException if order is null
     */
    public boolean enqueue(Order order) {
        // TODO (Zaldivar): implement the steps above.
        return false;
    }

    /**
     * Removes and returns the OLDEST order (FIFO).
     *
     * HOW IT MUST BEHAVE
     *   1. If empty, return null.
     *   2. Take items[front], set that slot to null (clear it).
     *   3. front = (front + 1) % capacity; size--.
     *   4. Return the order you took.
     *
     * @return the oldest order, or null if the queue is empty
     */
    public Order dequeue() {
        // TODO (Zaldivar): implement the steps above.
        return null;
    }

    /**
     * Returns the OLDEST order WITHOUT removing it.
     *
     * @return items[front], or null if the queue is empty
     */
    public Order peek() {
        // TODO (Zaldivar): return the oldest order or null; change nothing.
        return null;
    }

    /**
     * @return true when no orders are stored (size == 0)
     */
    public boolean isEmpty() {
        // TODO (Zaldivar): use the size counter (not front/rear comparison).
        return true;
    }

    /**
     * @return true when every slot is used (size == capacity)
     */
    public boolean isFull() {
        // TODO (Zaldivar): use the size counter.
        return false;
    }

    /**
     * @return how many orders are stored right now
     */
    public int size() {
        // TODO (Zaldivar): return the size field.
        return 0;
    }

    /**
     * @return the fixed maximum number of orders (the array length)
     */
    public int capacity() {
        // TODO (Zaldivar): return items.length.
        return 0;
    }

    /**
     * Returns the stored orders as a NEW array, oldest first.
     *
     * HOW IT MUST BEHAVE
     *   - New array of length size(). Index 0 = oldest order.
     *   - Must be correct even after the queue has wrapped around:
     *     copy items[(front + i) % capacity] into result[i] for i = 0..size-1.
     *   - Changing the returned array must NOT affect the queue.
     *   - An empty queue returns a zero-length array, NEVER null.
     *
     * @return a new array of the pending orders in FIFO order
     */
    public Order[] toArray() {
        // TODO (Zaldivar): build and return the new array.
        return new Order[0];
    }

    /**
     * Removes everything: set every slot to null, front = 0, size = 0.
     * The capacity stays the same.
     */
    public void clear() {
        // TODO (Zaldivar): empty the queue as described.
    }
}
