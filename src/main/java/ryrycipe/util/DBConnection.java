package ryrycipe.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Create a connection with the SQLite database.
 */
public class DBConnection {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBConnection.class.getName());

    /**
     * SQL {@link Connection}.
     */
    private static Connection connection;

    private DBConnection() {}

    /**
     * Retrieve the singleton instance of {@link Connection}
     *
     * @return {@link Connection}
     */
    public static Connection getInstance() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(
                    "jdbc:sqlite::resource:databases/ryrycipe_" + LanguageUtil.getLanguage() + ".db"
                );
            } catch (ClassNotFoundException | SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return connection;
    }

    /**
     * Update the path to the database in function of chosen language.
     */
    public static void changeLanguage() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(
                "jdbc:sqlite::resource:databases/ryrycipe_" + LanguageUtil.getLanguage() + ".db"
            );
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
