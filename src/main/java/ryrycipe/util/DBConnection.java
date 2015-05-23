package ryrycipe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Create a connection with the SQLite database.
 */
public class DBConnection {

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
