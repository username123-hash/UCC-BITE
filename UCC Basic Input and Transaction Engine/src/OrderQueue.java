public class OrderQueue {

    // Default capacity
    public static final int DEFAULT_CAPACITY = 100;

    // Private fields
    private Order[] items;
    private int front;
    private int size;

    // Creates an empty queue with DEFAULT_CAPACITY.
    public OrderQueue() {
        this(DEFAULT_CAPACITY);
    }

    // Creates an empty queue with a specified capacity.
    public OrderQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be greater than zero."
            );
        }

        items = new Order[capacity];
        front = 0;
        size = 0;
    }

    // Adds an order at the back of the queue.
    public boolean enqueue(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                "Order cannot be null."
            );
        }

        if (isFull()) {
            return false;
        }

        // Calculate the next free position.
        int rear = (front + size) % items.length;

        // Store the same Order object reference.
        items[rear] = order;

        size++;

        return true;
    }


    // Removes and returns the oldest order.
    public Order dequeue() {
        if (isEmpty()) {
            return null;
        }

        // Get the oldest order.
        Order order = items[front];

        // Clear the removed array slot.
        items[front] = null;

        // Move front to the next position.
        front = (front + 1) % items.length;

        // Decrease the number of stored orders.
        size--;

        return order;
    }


    // Returns the oldest order without removing it.
    public Order peek() {
        if (isEmpty()) {
            return null;
        }

        return items[front];
    }
    
    // Checks whether the queue is empty.
    public boolean isEmpty() {
        return size == 0;
    }

    // Checks whether the queue is full.
    public boolean isFull() {
        return size == items.length;
    }


    // Returns the current number of stored orders.
    public int size() {
        return size;
    }

    // Returns the fixed capacity of the queue.
    public int capacity() {
        return items.length;
    }

    /**
     * Returns a new array of orders in FIFO order.
     *
     * Index 0 contains the oldest order.
     * The returned array does not affect the queue when modified.
     *
     */
    public Order[] toArray() {
        Order[] result = new Order[size];

        for (int i = 0; i < size; i++) {
            int index = (front + i) % items.length;
            result[i] = items[index];
        }

        return result;
    }

    /**
     * Removes all orders from the queue.
     * The capacity remains unchanged.
     */
    public void clear() {
        // Clear every slot.
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }

        // Reset queue state.
        front = 0;
        size = 0;
    }
}
