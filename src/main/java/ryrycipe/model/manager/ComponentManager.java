package ryrycipe.model.manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.Component;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Manage {@link Component} object from ryrycipe database.
 */
public class ComponentManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(ComponentManager.class.getName());

    /**
     * Connection to the ryrycipe database.
     * @see DBConnection
     */
    public Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a {@link Component} not affiliated to a {@link Plan} by its id in the database.
     *
     * @param id {@link Component#id}
     * @return {@link Component}
     */
    public Component find(String id) {
        Component component = new Component();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM component AS c " +
                    "INNER JOIN recipe_component AS rc ON rc.component_id = c.id " +
                    "WHERE c.id = ?"
            );
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                component = new Component(
                    id, resultSet.getString("name"), resultSet.getString("icon")
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return component;
    }

    /**
     * Retrieve all {@link Component}s from a {@link ryrycipe.model.Plan}.
     *
     * @param planId {@link ryrycipe.model.Plan#id}
     * @return A {@link List} of {@link Component}s composing the {@link ryrycipe.model.Plan}.
     */
    public List<Component> findPlanComponents(int planId) {
        List<Component> components = new ArrayList<>();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT c.id AS component_id, c.name, c.icon, rc.amount " +
                    "FROM component AS c " +
                    "INNER JOIN recipe_component AS rc ON rc.component_id = c.id " +
                    "WHERE rc.recipe_id = ?"
            );
            statement.setInt(1, planId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                components.add(new Component(
                    resultSet.getString("component_id"), planId, resultSet.getString("name"),
                    resultSet.getString("icon"), resultSet.getInt("amount")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return components;
    }

    /**
     * Retrieve which pair of {@link Component}s a {@link ryrycipe.model.Material} can be used there.
     *
     * @param materialId {@link ryrycipe.model.Material#id}
     * @return {@link List} of {@link Component}s affiliated to the given {@link ryrycipe.model.Material}.
     */
    public List<Component> findMaterialComponents(String materialId) {
        List<Component> components = new ArrayList<>();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT c.id as cmp_id, c.name as cmp_name, c.icon as cmp_icon " +
                    "FROM component as c " +
                    "INNER JOIN material_component as mcmp " +
                    "ON c.id = mcmp.component_id_1 OR c.id = mcmp.component_id_2 " +
                    "INNER JOIN material_category as mc ON mcmp.id = mc.material_component_id " +
                    "INNER JOIN material as m ON m.category_id = mc.id " +
                    "WHERE m.id = ?"
            );
            statement.setString(1, materialId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                components.add(new Component(
                    resultSet.getString("cmp_id"), resultSet.getString("cmp_name"), resultSet.getString("cmp_icon")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return components;
    }
}
