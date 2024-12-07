import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfiguration {

    // Logger instance that used across the application
    protected static final Logger logger = Logger.getLogger(LoggerConfiguration.class.getName());

    // Folder to store log files
    private static final String LOG_DIR = "logs";

    static {
        // Create the 'logs' folder if it doesn't exist
        createDirectory(LOG_DIR);

        try {
            // Generate a timestamped log file name
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String logFileName = LOG_DIR + "/ticketing_system_cli_logs_" + timestamp + ".log";

            // Set up the file handler to write logs
            FileHandler fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new SimpleFormatter()); // Optional: set a formatter
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console logging if desired
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a directory if it does not already exist.
     *
     * @param directoryName The name of the directory to create.
     */
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
