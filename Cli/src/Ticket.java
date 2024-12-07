import java.util.concurrent.atomic.AtomicInteger;

public class Ticket extends LoggerConfiguration {
    private static final AtomicInteger idCounter = new AtomicInteger();
    private final int id;
    private final int vendorId; // Add vendorId attribute


    // Constructor that accepts vendorId
    public Ticket(int vendorId) {
        this.id = idCounter.incrementAndGet();
        this.vendorId = vendorId; // Assign vendorId
        logger.info("Ticket created: " + this); // Log ticket creation
    }

    /**
     * Retrieves the unique ID of this ticket.
     *
     * @return The unique ticket ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the ID of the vendor who created this ticket.
     *
     * @return The vendor ID associated with this ticket.
     */
    public int getVendorId() {
        return vendorId;
    }

    /**
     * Returns a string representation of the Ticket object.
     * Includes the ticket ID and associated vendor ID.
     *
     * @return A string describing this ticket.
     */
    @Override
    public String toString() {
        return "Ticket{id=" + id + ", vendorId=" + vendorId + "}";
    }
}