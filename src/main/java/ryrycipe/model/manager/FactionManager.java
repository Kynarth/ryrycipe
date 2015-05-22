package ryrycipe.model.manager;

import ryrycipe.model.Faction;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manage Faction object from ryrycipe database.
 */
public class FactionManager {

    private Connection connection = DBConnection.getInstance();

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
