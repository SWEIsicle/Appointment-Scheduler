package DBClientApplication.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/***
 * JDBC is a class that handles the connection between this program and the MySQL database.
 */
public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /***
     * a method to maintain an open connection.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /***
     * a method for ending the current connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /***
     * a method for getting the connection.
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(jdbcUrl, userName, password);
        return connection;
    }
}
