package ryrycipe.model.manager;


import ryrycipe.model.Component;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Manage Component object from ryrycipe database.
 */
public class ComponentManager {

    public Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a Component by its id in the database.
     *
     * @param id
     * @return Component
     */
    public Component find(String id) {
        Component component = new Component();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM component as c " +
                "INNER JOIN recipe_component as rc ON rc.component_id = c.id " +
                "WHERE c.id = ?"
            );
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.first()) {
                component = new Component(
                    id, resultSet.getString("name"), resultSet.getString("icon"), resultSet.getInt("amount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return component;
    }
}
