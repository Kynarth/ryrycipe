package ryrycipe.model.manager;


import ryrycipe.model.Component;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


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

            if (resultSet.isBeforeFirst()) {
                component = new Component(
                    id, resultSet.getString("name"), resultSet.getString("icon"), resultSet.getInt("amount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return component;
    }

    /**
     * Retrieve all components from a plan.
     *
     * @param planId
     * @return The list of components composing the plan.
     */
    public ArrayList<Component> getPlanComponents(int planId) {
        ArrayList<Component> components = new ArrayList<Component>();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT c.id as component_id, c.name, c.icon, rc.amount" +
                    " FROM component as c " +
                    "INNER JOIN recipe_component as rc ON rc.component_id = c.id " +
                    "WHERE rc.recipe_id = ?"
            );
            statement.setInt(1, planId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                components.add(new Component(
                    resultSet.getString("component_id"), resultSet.getString("name"),
                    resultSet.getString("icon"), resultSet.getInt("amount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return components;

    }
}
