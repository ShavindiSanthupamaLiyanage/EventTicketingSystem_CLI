//import java.util.Scanner;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class TicketingSystem extends LoggerConfiguration {
//    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
//    private static ExecutorService vendorService;
//    private static ExecutorService customerService;
//    private static volatile boolean isMonitoring = true; // Flag to control monitoring thread
//
//    public static void main(String[] args) {
//
//        String message = "Welcome to the Ticketing System CLI";
//        Logo.printBoxedMessage(message);
//
//        logger.info("Starting the Ticketing System CLI");
//
//        // Initialize system configuration
//        SystemConfiguration config = new SystemConfiguration();
//        config.initializeConfiguration();
//
//        // Use configuration to create the ticket pool
//        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());
//
//        // Main loop for controlling the ticketing system
//        controlSystem(config, ticketPool);
//    }
//
//    private static void controlSystem(SystemConfiguration config, TicketPool ticketPool) {
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            if (!isRunning.get()) {
//                System.out.println("Enter '1' to START ticket handling, '2' to PAUSE, or '0' to EXIT:");
//            }
//
//            String command = scanner.nextLine().trim().toLowerCase();
//
//            if ("1".equals(command)) {
//                if (isRunning.compareAndSet(false, true)) {
//                    System.out.println("Starting ticket handling operations...");
//                    vendorService = Executors.newFixedThreadPool(config.getNumVendors());
//                    customerService = Executors.newFixedThreadPool(config.getNumCustomers());
//                    startTicketingOperations(
//                            vendorService,
//                            customerService,
//                            ticketPool,
//                            config.getTicketReleaseRate(),
//                            config.getTotalTickets(),
//                            config.getCustomerRetrievalRate(),
//                            config.getNumVendors(),
//                            config.getNumCustomers()
//                    );
//                } else {
//                    System.out.println("Ticket handling is already running.");
//                }
//            } else if ("2".equals(command)) { // PAUSE
//                if (isRunning.compareAndSet(true, false)) {
//                    System.out.println("Stopping ticket handling operations...");
//                    stopTicketingOperations(vendorService, customerService);
//
//                    // Update the TicketStatus table with 'STOP'
//                    ticketPool.updateTicketStatusOnCommand("STOP");
//                    System.out.println("System paused. Enter '1' to resume or '0' to exit.");
//                } else {
//                    System.out.println("Ticket handling is not currently running.");
//                }
//            } else if ("0".equals(command)) { // EXIT
//                System.out.println("Exiting the system...");
//                logger.info("Exiting the Ticketing System CLI");
//                stopTicketingOperations(vendorService, customerService);
//
//                // Update the TicketStatus table with 'EXIT'
//                ticketPool.updateTicketStatusOnCommand("EXIT");
//                isMonitoring = false;
//                break;
//            } else {
//                System.out.println("Invalid command! Please enter '1', '2', or '0'.");
//            }
//        }
//        scanner.close();
//    }
//
//
//
//    private static void startTicketingOperations(
//            ExecutorService vendorService,
//            ExecutorService customerService,
//            TicketPool ticketPool,
//            int ticketReleaseRate,
//            int totalTickets,
//            int customerRetrievalRate,
//            int numVendors,
//            int numCustomers
//    ) {
//        for (int i = 1; i <= numVendors; i++) {
//            Vendor vendor = new Vendor(i, ticketReleaseRate, 1000, ticketPool, totalTickets);
//            vendorService.execute(vendor);
//        }
//
//        //REMOVE ticketsPerRetrieval FROM MAIN AND THE CUSTOMER CLASS AND INITATE THE customerRetrievalRate
//
//
//        for (int i = 1; i <= numCustomers; i++) {
//            int ticketsPerRetrieval = 2; // Example value
//            Customer customer = new Customer(i, ticketPool, 2000, ticketsPerRetrieval);
//            customerService.execute(customer);
//        }
//
//        new Thread(() -> monitorTicketingOperations(ticketPool)).start();
//    }
//
//    private static void stopTicketingOperations(ExecutorService vendorService, ExecutorService customerService) {
//        if (vendorService != null && !vendorService.isShutdown()) {
//            vendorService.shutdownNow();
//        }
//        if (customerService != null && !customerService.isShutdown()) {
//            customerService.shutdownNow();
//        }
//
//        try {
//            if (vendorService != null && !vendorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
//                vendorService.shutdownNow();
//            }
//            if (customerService != null && !customerService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
//                customerService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            if (vendorService != null) vendorService.shutdownNow();
//            if (customerService != null) customerService.shutdownNow();
//            logger.severe("Error during shutdown of services: " + e.getMessage());
//        }
//    }
//
//    private static void monitorTicketingOperations(TicketPool ticketPool) {
//        while (isMonitoring) {
//            try {
//                int currentSize = ticketPool.getCurrentSize();
//                logger.info("Monitoring Ticket Pool: Current Size = " + currentSize);
//                System.out.println("Monitoring Ticket Pool: Current Size = " + currentSize);
//
//                if (currentSize == 0) {
//                    logger.warning("Warning: Ticket pool is empty!");
//                    System.out.println("Warning: Ticket pool is empty!");
//                }
//
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                logger.warning("Monitoring thread interrupted.");
//                break;
//            }
//        }
//    }
//}



import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketingSystem extends LoggerConfiguration {
    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static ExecutorService vendorService;
    private static ExecutorService customerService;
    private static volatile boolean isMonitoring = true; // Flag to control monitoring thread

    public static void main(String[] args) {
        String message = "Welcome to the Ticketing System CLI";
        Logo.printBoxedMessage(message);

        logger.info("Starting the Ticketing System CLI");

        // Initialize system configuration
        SystemConfiguration config = null;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Present the choices
            System.out.println("Do you want to (1) Load existing configuration or (2) Reconfigure the system? (0 to exit)");

            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                // Attempt to load the serialized configuration
                config = SystemConfiguration.loadFromFile("system_configuration.json");
                if (config != null) {
                    logger.info("Loaded configuration from file successfully.");
                    System.out.println("Loaded configuration from file successfully.");
                    break;
                } else {
                    logger.warning("No existing configuration found or failed to load. Redirecting to reconfiguration...");
                    System.out.println("No existing configuration found. Proceeding to reconfigure...");
                    config = new SystemConfiguration();
                    config.initializeConfiguration();
                    break;
                }
            } else if ("2".equals(choice)) {
                // Reconfigure the system
                config = new SystemConfiguration();
                config.initializeConfiguration();
                break;
            } else if ("0".equals(choice)) {
                // Exit the system
                System.out.println("Exiting the system...");
                logger.info("User exited the system.");
                return; // Exits the program
            } else {
                // Invalid choice
                System.out.println("Invalid choice.");
            }
        }

        // Use configuration to create the ticket pool
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());

        // Main loop for controlling the ticketing system
        controlSystem(config, ticketPool);
    }

    private static void controlSystem(SystemConfiguration config, TicketPool ticketPool) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!isRunning.get()) {
                System.out.println("Enter '1' to START ticket handling, '2' to PAUSE, or '0' to EXIT:");
            }

            String command = scanner.nextLine().trim().toLowerCase();

            if ("1".equals(command)) {
                if (isRunning.compareAndSet(false, true)) {
                    System.out.println("Starting ticket handling operations...");
                    vendorService = Executors.newFixedThreadPool(config.getNumVendors());
                    customerService = Executors.newFixedThreadPool(config.getNumCustomers());
                    startTicketingOperations(
                            vendorService,
                            customerService,
                            ticketPool,
                            config.getTicketReleaseRate(),
                            config.getTotalTickets(),
                            config.getCustomerRetrievalRate(),
                            config.getNumVendors(),
                            config.getNumCustomers()
                    );
                } else {
                    System.out.println("Ticket handling is already running.");
                }
            } else if ("2".equals(command)) { // PAUSE
                if (isRunning.compareAndSet(true, false)) {
                    System.out.println("Stopping ticket handling operations...");
                    stopTicketingOperations(vendorService, customerService);

                    // Update the TicketStatus table with 'STOP'
                    ticketPool.updateTicketStatusOnCommand("STOP");
                    System.out.println("System paused. Enter '1' to resume or '0' to exit.");
                } else {
                    System.out.println("Ticket handling is not currently running.");
                }
            } else if ("0".equals(command)) { // EXIT
                System.out.println("Exiting the system...");
                logger.info("Exiting the Ticketing System CLI");
                stopTicketingOperations(vendorService, customerService);

                // Update the TicketStatus table with 'EXIT'
                ticketPool.updateTicketStatusOnCommand("EXIT");
                isMonitoring = false;
                break;
            } else {
                System.out.println("Invalid command! Please enter '1', '2', or '0'.");
            }
        }
        scanner.close();
    }

    private static void startTicketingOperations(
            ExecutorService vendorService,
            ExecutorService customerService,
            TicketPool ticketPool,
            int ticketReleaseRate,
            int totalTickets,
            int customerRetrievalRate,
            int numVendors,
            int numCustomers
    ) {
        for (int i = 1; i <= numVendors; i++) {
            Vendor vendor = new Vendor(i, ticketReleaseRate, 1000, ticketPool, totalTickets);
            vendorService.execute(vendor);
        }

        for (int i = 1; i <= numCustomers; i++) {
            int ticketsPerRetrieval = 2; // Example value
            Customer customer = new Customer(i, ticketPool, 2000, ticketsPerRetrieval);
            customerService.execute(customer);
        }

        new Thread(() -> monitorTicketingOperations(ticketPool)).start();
    }

    private static void stopTicketingOperations(ExecutorService vendorService, ExecutorService customerService) {
        if (vendorService != null && !vendorService.isShutdown()) {
            vendorService.shutdownNow();
        }
        if (customerService != null && !customerService.isShutdown()) {
            customerService.shutdownNow();
        }

        try {
            if (vendorService != null && !vendorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                vendorService.shutdownNow();
            }
            if (customerService != null && !customerService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                customerService.shutdownNow();
            }
        } catch (InterruptedException e) {
            if (vendorService != null) vendorService.shutdownNow();
            if (customerService != null) customerService.shutdownNow();
            logger.severe("Error during shutdown of services: " + e.getMessage());
        }
    }

    private static void monitorTicketingOperations(TicketPool ticketPool) {
        while (isMonitoring) {
            try {
                int currentSize = ticketPool.getCurrentSize();
                logger.info("Monitoring Ticket Pool: Current Size = " + currentSize);
                System.out.println("Monitoring Ticket Pool: Current Size = " + currentSize);

                if (currentSize == 0) {
                    logger.warning("Warning: Ticket pool is empty!");
                    System.out.println("Warning: Ticket pool is empty!");
                }

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warning("Monitoring thread interrupted.");
                break;
            }
        }
    }
}

