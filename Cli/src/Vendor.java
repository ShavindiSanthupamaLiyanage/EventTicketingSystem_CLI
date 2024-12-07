/**
 * Represents a Vendor in the ticketing system responsible for producing and adding tickets to the shared ticket pool.
 * Implements {@link Runnable} to allow concurrent ticket production by multiple vendors.
 */
public class Vendor extends LoggerConfiguration implements Runnable {
    private final int vendorId;
    private final int ticketsPerRelease;
    private final int releaseInterval;
    private final TicketPool ticketPool;
    private final int totalTickets;

    private int totalTicketsProduced = 0;

    /**
     * Constructs a Vendor with the specified parameters.
     *
     * @param vendorId          Unique ID of the vendor.
     * @param ticketsPerRelease Number of tickets released per cycle.
     * @param releaseInterval   Time interval between ticket release cycles (in milliseconds).
     * @param ticketPool        Shared {@link TicketPool} to which tickets will be added.
     * @param totalTickets      Maximum number of tickets the vendor is allowed to produce.
     */
    public Vendor(int vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool, int totalTickets) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        try {
            // Loop until the vendor reaches the maximum ticket production limit
            while (totalTicketsProduced < totalTickets) {
                // Produce and add tickets in batches defined by ticketsPerRelease
                for (int i = 0; i < ticketsPerRelease; i++) {

                    // Add ticket to the pool
                    Ticket ticket = new Ticket(vendorId);
                    ticketPool.addTicket(ticket, vendorId);

                    // Track the number of tickets this vendor has produced
                    totalTicketsProduced++;

                    logger.info("Vendor " + vendorId + " added ticket: " + ticket.getId()); // Log ticket addition
                }
                Thread.sleep(releaseInterval); // Wait for the specified release interval
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Vendor " + vendorId + " was unable to release tickets due to high demand. Please wait for the next ticket release cycle."); // Log warning
            System.out.println("Vendor " + vendorId + " was unable to release tickets due to high demand. Please wait for the next ticket release cycle.");
        }
    }
}

