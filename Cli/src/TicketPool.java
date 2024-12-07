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

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>()); // Thread-safe list
        logger.info("Ticket pool initialized with maxCapacity: " + maxCapacity);
    }

    // Adds a ticket to the pool, respecting capacity constraints
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

//        updateTicketStatus(); // Update the status table
        notifyAll(); // Notify any waiting consumers
    }

    // Retrieves a ticket from the pool for a customer
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
//        System.out.println("Customer retrieved ticket ID " + ticket.getId());
//        logger.info("Customer retrieved ticket ID " + ticket.getId());
        System.out.println("Customer " + customerId + " retrieved ticket ID " + ticket.getId());
        logger.info("Customer " + customerId + " retrieved ticket ID " + ticket.getId());

//        updateTicketStatus(); // Update the status table
        notifyAll(); // Notify any waiting vendors
        return ticket;
    }

    // Gets the current size of the ticket pool
    public synchronized int getCurrentSize() {
        int size = tickets.size();
        logger.info("Current ticket pool size: " + size);
        return size;
    }

//    // Updates the TicketStatus table with real-time data
//    public void updateTicketStatus() {
//        String query = "INSERT INTO TicketStatus (totalTicketsReleased, totalTicketsPurchased, totalTicketsToBeReleased, totalTicketsRemaining) "
//                + "VALUES (?, ?, ?, ?) "
//                + "ON DUPLICATE KEY UPDATE "
//                + "totalTicketsReleased = VALUES(totalTicketsReleased), "
//                + "totalTicketsPurchased = VALUES(totalTicketsPurchased), "
//                + "totalTicketsToBeReleased = VALUES(totalTicketsToBeReleased), "
//                + "totalTicketsRemaining = VALUES(totalTicketsRemaining)";
//
//        try (Connection connection = DbConfiguration.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            int totalTicketsToBeReleased = maxCapacity - getCurrentSize();
//            preparedStatement.setInt(1, totalTicketsProduced);
//            preparedStatement.setInt(2, totalTicketsRetrieved);
//            preparedStatement.setInt(3, totalTicketsToBeReleased);
//            preparedStatement.setInt(4, getCurrentSize());
//
//            preparedStatement.executeUpdate();
//            logger.info("TicketStatus table updated successfully.");
//        } catch (SQLException e) {
//            logger.severe("Failed to update TicketStatus table: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

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
