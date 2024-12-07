/**
 * Represents a customer in the ticketing system who attempts to purchase tickets.
 * Implements the {@link Runnable} interface to enable multi-threaded ticket retrieval.
 *
 * Each customer repeatedly attempts to retrieve a specified number of tickets at fixed intervals
 * until no tickets are available or the thread is interrupted.
 */
public class Customer extends LoggerConfiguration implements Runnable {
    private final int customerId;
    private final int ticketsPerRetrieval;
    private final int retrievalInterval;
    private final TicketPool ticketPool;

    /**
     * Constructs a Customer object with specified parameters.
     *
     * @param customerId          Unique ID for the customer.
     * @param ticketPool          The shared {@link TicketPool} object for ticket management.
     * @param retrievalInterval   Interval (in milliseconds) between retrieval attempts.
     * @param ticketsPerRetrieval Number of tickets to attempt to retrieve in each cycle.
     */
    public Customer(int customerId, TicketPool ticketPool, int retrievalInterval, int ticketsPerRetrieval) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.ticketsPerRetrieval = ticketsPerRetrieval;
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
                        logger.info("Customer " + customerId + " successfully purchased ticket ID " + ticket.getId());
                    } else {
                        logger.info("Customer " + customerId + " found no tickets available.");
                        break;
                    }
                }

                // Exit loop if no tickets were retrieved in this cycle
                if (!ticketsRetrieved) {
                    System.out.printf("Customer %d: No tickets retrieved. Exiting.%n", customerId);
                    break;
                }

                // Wait for the specified interval before the next purchase attempt
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                //Handle thread interruption
                Thread.currentThread().interrupt(); // Restore interrupted status
                logger.warning("Customer " + customerId + " was interrupted.");
                break;
            }
        }
        System.out.printf("Customer %d: Finished attempting ticket purchases.%n", customerId);
        logger.info("Customer " + customerId + " has finished all attempts.");
    }
}

