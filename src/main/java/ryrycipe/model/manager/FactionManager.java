package ryrycipe.model.manager;

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

    /**
     * Connection to the ryrycipe database.
     * @see DBConnection
     */
    private Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a {@link Faction} by its name in the database.
     *
     * @param name Name of {@link Faction#name}
     * @return {@link Faction}
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
            e.printStackTrace();
        }

        return faction;
    }
}
