//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Scanner;
//
//public class SystemConfiguration extends LoggerConfiguration {
//    private int totalTickets;
//    private int ticketReleaseRate;
//    private int customerRetrievalRate;
//    private int maxTicketCapacity;
//    private int numVendors;
//    private int numCustomers;
//
//    public SystemConfiguration() {
//        // Initialize with default values, if needed
//    }
//
//    public void initializeConfiguration() {
//        Scanner scanner = new Scanner(System.in);
//        logger.info("Initializing system configuration...");
//
//        this.totalTickets = getValidInput(scanner, "Enter total number of tickets per vendor :", 1, Integer.MAX_VALUE);
//        this.ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate :", 1, Integer.MAX_VALUE);
//        this.customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate :", 1, Integer.MAX_VALUE);
//        this.maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity :", 1, Integer.MAX_VALUE);
//        this.numVendors = getValidInput(scanner, "Enter number of vendors :", 1, Integer.MAX_VALUE);
//        this.numCustomers = getValidInput(scanner, "Enter number of customers :", 1, Integer.MAX_VALUE);
//
//        logger.info("System configuration initialized successfully.");
//        System.out.println("System configuration initialized successfully.");
//
//        saveToDatabase();
//        logger.info("Configuration saved to the database.");
//
//        // Call the FileConfigurationSaver class
//        FileConfiguration.saveConfigurationToFile(this);
//        logger.info("System configuration saved to file successfully.");
//    }
//
//    private int getValidInput(Scanner scanner, String prompt, int min, int max) {
//        int input;
//        while (true) {
//            System.out.print(prompt + " ");
//            try {
//                input = Integer.parseInt(scanner.nextLine().trim());
//                if (input < min || input > max) {
//                    System.out.println("Invalid input! Please enter a positive number.");
//                    logger.warning("Invalid input. Expected range: " + min + " - " + max);
//                } else {
//                    return input;
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input! Please enter a valid integer.");
//                logger.warning("Invalid Number format. Expected range: " + min + " - " + max);
//            }
//        }
//    }
//
//    public void saveToDatabase() {
//        String query = "INSERT INTO SystemConfiguration (totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, numVendors, numCustomers) VALUES (?, ?, ?, ?, ?, ?)";
//
//        try (Connection connection = DbConfiguration.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, totalTickets);
//            preparedStatement.setInt(2, ticketReleaseRate);
//            preparedStatement.setInt(3, customerRetrievalRate);
//            preparedStatement.setInt(4, maxTicketCapacity);
//            preparedStatement.setInt(5, numVendors);
//            preparedStatement.setInt(6, numCustomers);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
////    public static SystemConfiguration loadFromDatabase(int configId) {
////        String query = "SELECT * FROM SystemConfiguration WHERE id = ?";
////        SystemConfiguration config = null;
////
////        try (Connection connection = DbConfiguration.getConnection();
////             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
////            preparedStatement.setInt(1, configId);
////
////            ResultSet resultSet = preparedStatement.executeQuery();
////            if (resultSet.next()) {
////                config = new SystemConfiguration();
////                config.totalTickets = resultSet.getInt("totalTickets");
////                config.ticketReleaseRate = resultSet.getInt("ticketReleaseRate");
////                config.customerRetrievalRate = resultSet.getInt("customerRetrievalRate");
////                config.maxTicketCapacity = resultSet.getInt("maxTicketCapacity");
////                config.numVendors = resultSet.getInt("numVendors");
////                config.numCustomers = resultSet.getInt("numCustomers");
////            }
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////
////        return config;
////    }
//
//    public int getTotalTickets() {
//        return totalTickets;
//    }
//
//    public int getTicketReleaseRate() {
//        return ticketReleaseRate;
//    }
//
//    public int getCustomerRetrievalRate() {
//        return customerRetrievalRate;
//    }
//
//    public int getMaxTicketCapacity() {
//        return maxTicketCapacity;
//    }
//
//    public int getNumVendors() {
//        return numVendors;
//    }
//
//    public int getNumCustomers() {
//        return numCustomers;
//    }
//}





import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class SystemConfiguration extends LoggerConfiguration implements Serializable {
    private static final long serialVersionUID = 1L; // For versioning the serialized object

    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int numVendors;
    private int numCustomers;

    public SystemConfiguration() {
        // Initialize with default values, if needed
    }

    public void initializeConfiguration() {
        Scanner scanner = new Scanner(System.in);
        logger.info("Initializing system configuration...");

        this.totalTickets = getValidInput(scanner, "Enter total number of tickets per vendor :", 1, Integer.MAX_VALUE);
        this.ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate :", 1, Integer.MAX_VALUE);
        this.customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate :", 1, Integer.MAX_VALUE);
        this.maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity :", 1, Integer.MAX_VALUE);
        this.numVendors = getValidInput(scanner, "Enter number of vendors :", 1, Integer.MAX_VALUE);
        this.numCustomers = getValidInput(scanner, "Enter number of customers :", 1, Integer.MAX_VALUE);

        logger.info("System configuration initialized successfully.");
        System.out.println("System configuration initialized successfully.");

        saveToDatabase();
        logger.info("Configuration saved to the database.");

        // Save to file using serialization
        saveToFile("system_configuration.json");
        logger.info("System configuration serialized to file successfully.");

        FileConfiguration.saveConfigurationToFile(this);
        logger.info("System configuration saved to file successfully.");
    }

    private int getValidInput(Scanner scanner, String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt + " ");
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input < min || input > max) {
                    System.out.println("Invalid input! Please enter a positive number.");
                    logger.warning("Invalid input. Expected range: " + min + " - " + max);
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
                logger.warning("Invalid Number format. Expected range: " + min + " - " + max);
            }
        }
    }

    public void saveToDatabase() {
        String query = "INSERT INTO SystemConfiguration (totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, numVendors, numCustomers) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DbConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, totalTickets);
            preparedStatement.setInt(2, ticketReleaseRate);
            preparedStatement.setInt(3, customerRetrievalRate);
            preparedStatement.setInt(4, maxTicketCapacity);
            preparedStatement.setInt(5, numVendors);
            preparedStatement.setInt(6, numCustomers);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            logger.info("Configuration saved to file: " + fileName);
        } catch (IOException e) {
            logger.severe("Error saving configuration to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    public static SystemConfiguration loadFromFile(String fileName) {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
//            return (SystemConfiguration) ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            System.err.println("Error loading configuration from file: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Save the configuration object to a file
//    public void saveToFile(String fileName) {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
//            oos.writeObject(this);
//            logger.info("Configuration saved to file: " + fileName);
//        } catch (IOException e) {
//            logger.severe("Error saving configuration to file: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    // Load the configuration object from a file
    public static SystemConfiguration loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            SystemConfiguration config = (SystemConfiguration) ois.readObject();
            logger.info("Configuration loaded from file: " + fileName);
            return config;
        } catch (FileNotFoundException e) {
            logger.warning("Configuration file not found: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Error loading configuration from file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    // Getter methods
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public int getNumVendors() {
        return numVendors;
    }

    public int getNumCustomers() {
        return numCustomers;
    }
}
