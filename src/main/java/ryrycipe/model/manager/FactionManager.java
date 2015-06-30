package ryrycipe.model.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.Faction;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manage {@link Faction} object from ryrycipe database.
 */
public class FactionManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(FactionManager.class.getName());

    /**
     * Connection to the ryrycipe database.
     * @see DBConnection
     */
    private Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a {@link Faction} by its name in the database.
     *
     * @param name Name of {@link Faction#name}
     * @return Return a filled {@link Faction} if provided name is correct otherwise, return an empty one.
     */
    public Faction find(String name) {
        Faction faction = new Faction();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT name, icon FROM faction WHERE name = ?"
            );
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                faction = new Faction(
                    name, resultSet.getString("icon")
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return faction;
    }
}
