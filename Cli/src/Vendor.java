public class Vendor extends LoggerConfiguration implements Runnable {
    private final int vendorId;
    private final int ticketsPerRelease;
    private final int releaseInterval;
    private final TicketPool ticketPool;
    private final int totalTickets; // Total tickets that can be produced

    private int totalTicketsProduced = 0; // Count of total tickets produced by this vendor

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
            while (totalTicketsProduced < totalTickets) { // Limit ticket production by totalTickets
                for (int i = 0; i < ticketsPerRelease; i++) {
                    Ticket ticket = new Ticket(vendorId);
                    ticketPool.addTicket(ticket, vendorId); // Add ticket to the pool
                    totalTicketsProduced++; // Track the number of tickets this vendor has produced
                    //System.out.println("Vendor " + vendorId + " added ticket " + ticket);
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

