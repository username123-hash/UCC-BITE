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

    // TODO (Zaldivar): add DEFAULT_CAPACITY, the private fields, both
    //                  constructors, and the methods listed above.

}
