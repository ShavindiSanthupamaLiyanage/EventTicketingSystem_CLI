import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileConfiguration {

    // Folder for configuration files
    private static final String CONFIG_DIR = "configurations";

    static {
        // Create the 'configurations' folder if it doesn't exist
        createDirectory(CONFIG_DIR);
    }

    // Method to save configuration to the 'configurations' folder
    public static void saveConfigurationToFile(SystemConfiguration config) {
        // Generate file name with current date and time
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = CONFIG_DIR + "/System_Configuration_" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("System Configuration Details:");
            writer.newLine();
            writer.write("-----------------------------");
            writer.newLine();
            writer.write("Total Tickets: " + config.getTotalTickets());
            writer.newLine();
            writer.write("Ticket Release Rate: " + config.getTicketReleaseRate());
            writer.newLine();
            writer.write("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());
            writer.newLine();
            writer.write("Max Ticket Capacity: " + config.getMaxTicketCapacity());
            writer.newLine();
            writer.write("Number of Vendors: " + config.getNumVendors());
            writer.newLine();
            writer.write("Number of Customers: " + config.getNumCustomers());
            writer.newLine();

            System.out.println("Configuration saved to file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving configuration to file: " + e.getMessage());
        }
    }

    // Utility method to create directories if they do not exist
    private static void createDirectory(String directoryName) {
        Path path = Paths.get(directoryName);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directory created: " + directoryName);
            } catch (IOException e) {
                System.err.println("Error creating directory: " + directoryName + ". " + e.getMessage());
            }
        }
    }
}
