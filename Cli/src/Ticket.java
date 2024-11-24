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

    public int getId() {
        return id;
    }

    public int getVendorId() {
        return vendorId; // Getter for vendorId
    }

    @Override
    public String toString() {
        return "Ticket{id=" + id + ", vendorId=" + vendorId + "}";
    }
}