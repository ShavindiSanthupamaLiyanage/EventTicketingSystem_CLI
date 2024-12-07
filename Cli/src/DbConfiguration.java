import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfiguration {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ticketingsystemcli";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Santhu2002";

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A {@link Connection} object to interact with the database.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
