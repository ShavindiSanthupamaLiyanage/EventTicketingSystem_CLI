import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class SystemConfiguration extends LoggerConfiguration{

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
        System.out.print(" \n" +
                         "System configuration initialized successfully.");


        saveToDatabase();
        logger.info("Configuration saved to the database.");


        // Save to JSON file
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

    /**
     * Serializes the system configuration to a JSON file using Gson.
     * The file is named based on the provided fileName parameter.
     *
     * @param fileName The name of the file to save the configuration.
     */
    public void saveToFile(String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
            logger.info("Configuration saved to JSON file: " + fileName);
        } catch (IOException e) {
            logger.severe("Error saving configuration to JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the system configuration from a JSON file using Gson.
     *
     * @param fileName The name of the file to load the configuration from.
     * @return The loaded SystemConfiguration object, or null if an error occurs.
     */
    public static SystemConfiguration loadFromFile(String fileName) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            logger.info("Configuration loaded from file: " + fileName);
            return gson.fromJson(reader, SystemConfiguration.class);
        } catch (IOException e) {
            logger.severe("Error loading configuration from JSON file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

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

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void setNumVendors(int numVendors) {
        this.numVendors = numVendors;
    }

    public void setNumCustomers(int numCustomers) {
        this.numCustomers = numCustomers;
    }
}
