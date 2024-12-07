import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool extends LoggerConfiguration {
    private final List<Ticket> tickets;
    private final int maxCapacity; // Maximum number of tickets in the pool at one time
    private int totalTicketsProduced = 0; // Count of total tickets produced
    private int totalTicketsRetrieved = 0; // Count of total tickets retrieved

    /**
     * Constructs a TicketPool with a specified maximum capacity.
     *
     * @param maxCapacity The maximum number of tickets the pool can hold at any time.
     */
    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>()); // Thread-safe list
        logger.info("Ticket pool initialized with maxCapacity: " + maxCapacity);
    }


    /**
     * Adds a ticket to the pool, blocking if the pool is at maximum capacity.
     *
     * @param ticket   The {@link Ticket} to be added.
     * @param vendorId The ID of the vendor adding the ticket.
     */
    public synchronized void addTicket(Ticket ticket, int vendorId) {
        while (tickets.size() >= maxCapacity) {
            System.out.println("Ticket pool is at max capacity. Vendor " + vendorId + " is waiting to add tickets...");
            logger.info("Vendor " + vendorId + " is waiting to add tickets...");
            try {
                wait(); // Wait until space is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Vendor was interrupted while waiting to add tickets.");
                return;
            }
        }

        tickets.add(ticket);
        totalTicketsProduced++;
        System.out.println("Vendor " + vendorId + " released ticket ID " + ticket.getId());
        logger.info("Vendor " + vendorId + " added ticket ID " + ticket.getId());

        notifyAll(); // Notify any waiting consumers
    }

    /**
     * Retrieves a ticket from the pool for a customer, blocking if the pool is empty.
     *
     * @param customerId The ID of the customer retrieving the ticket.
     * @return The retrieved {@link Ticket}, or null if the thread is interrupted.
     */
    public synchronized Ticket retrieveTicket(int customerId) {
        while (tickets.isEmpty()) {
            System.out.println("No tickets available. Customer is waiting...");
            logger.info("Customer is waiting for tickets...");
            try {
                wait(); // Wait until a ticket is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Customer was interrupted while waiting for tickets.");
                return null;
            }
        }

        Ticket ticket = tickets.remove(0);
        totalTicketsRetrieved++;

        System.out.println("Customer " + customerId + " retrieved ticket ID " + ticket.getId());
        logger.info("Customer " + customerId + " retrieved ticket ID " + ticket.getId());

        notifyAll(); // Notify any waiting vendors
        return ticket;
    }

    // Gets the current size of the ticket pool
    public synchronized int getCurrentSize() {
        int size = tickets.size();
        logger.info("Current ticket pool size: " + size);
        return size;
    }

    /**
     * Updates the ticket status in the database based on the current state of the pool.
     * The status includes total tickets released, purchased, remaining, and the system's operational state.
     *
     * @param systemStatus The current status of the system (e.g., STOP or EXIT).
     */
    public void updateTicketStatusOnCommand(String systemStatus) {
        String query = "INSERT INTO TicketStatus (totalTicketsReleased, totalTicketsPurchased, totalTicketsRemaining, systemStatus) "
                + "VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "totalTicketsReleased = VALUES(totalTicketsReleased), "
                + "totalTicketsPurchased = VALUES(totalTicketsPurchased), "
                + "totalTicketsRemaining = VALUES(totalTicketsRemaining), "
                + "systemStatus = VALUES(systemStatus)";

        try (Connection connection = DbConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, totalTicketsProduced);
            preparedStatement.setInt(2, totalTicketsRetrieved);
            preparedStatement.setInt(3, getCurrentSize());
            preparedStatement.setString(4, systemStatus); // Add system status (STOP or EXIT)

            preparedStatement.executeUpdate();
            logger.info("TicketStatus table updated successfully with system status: " + systemStatus);
        } catch (SQLException e) {
            logger.severe("Failed to update TicketStatus table with system status: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
