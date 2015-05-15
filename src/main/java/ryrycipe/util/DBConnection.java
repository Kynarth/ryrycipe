package ryrycipe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Create a connection with the SQLite database.
 */
public class DBConnection {

    private static Connection connection;

    private DBConnection() {}

    /**
     * Method to get the database connection.
     *
     * @return Connection
     */
    public static Connection getInstance() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(
                    "jdbc:sqlite://"+ DBConnection.class.getResource(
                        "/databases/ryrycipe_" + LocaleUtil.getLanguage() + ".db"
                    ).getPath()
                );
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return connection;
    }
}
