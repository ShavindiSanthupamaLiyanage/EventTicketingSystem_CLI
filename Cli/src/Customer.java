public class Customer extends LoggerConfiguration implements Runnable {
    private final int customerId; // Unique identifier for the customer
    private final int ticketsPerRetrieval; // Number of tickets to retrieve per attempt
    private final int retrievalInterval; // Interval for ticket purchase attempts
    private final TicketPool ticketPool; // Reference to the shared TicketPool

    public Customer(int customerId, TicketPool ticketPool, int retrievalInterval, int ticketsPerRetrieval) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.ticketsPerRetrieval = ticketsPerRetrieval; // Set tickets per retrieval
    }

    @Override
    public void run() {
        while (true) {
            try {
                boolean ticketsRetrieved = false;

                // Attempt to retrieve the specified number of tickets
                for (int i = 0; i < ticketsPerRetrieval; i++) {
                    Ticket ticket = ticketPool.retrieveTicket(customerId);
                    if (ticket != null) {
                        ticketsRetrieved = true;
                        //System.out.printf("Customer %d successfully purchased ticket ID %d%n", customerId, ticket.getId());
                        logger.info("Customer " + customerId + " successfully purchased ticket ID " + ticket.getId());
                    } else {
                        // No tickets available or all tickets have been produced
                        logger.info("Customer " + customerId + " found no tickets available.");
                        break;
                    }
                }

                if (!ticketsRetrieved) {
                    System.out.printf("Customer %d: No tickets retrieved. Exiting.%n", customerId);
                    break; // Exit loop if no tickets were retrieved in this cycle
                }

                // Wait for the specified interval before the next purchase attempt
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                logger.warning("Customer " + customerId + " was interrupted.");
                break;
            }
        }

        System.out.printf("Customer %d: Finished attempting ticket purchases.%n", customerId);
        logger.info("Customer " + customerId + " has finished all attempts.");
    }
}

