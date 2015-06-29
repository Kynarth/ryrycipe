package ryrycipe.model.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.Category;
import ryrycipe.model.Component;
import ryrycipe.model.Plan;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage {@link Plan} object from ryrycipe database.
 */
public class PlanManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlanManager.class.getName());

    /**
     * Connection to the ryrycipe database.
     * @see DBConnection
     */
    public Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a {@link Plan} by its name and quality in the database.
     *
     * @param name {@link Plan#name}
     * @param quality {@link Plan#quality}
     * @return {@link Plan}
     */
    public Plan find(String name, String quality) {
        Plan plan = new Plan();
        CategoryManager categoryManager = new CategoryManager();
        ComponentManager componentManager = new ComponentManager();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT r.id AS plan_id, r.icon, r.category_id FROM recipe AS r " +
                    "INNER JOIN recipe_category AS rc ON r.category_id = rc.id " +
                    "INNER JOIN recipe_component AS rcmp ON rcmp.recipe_id = r.id " +
                    "WHERE r.name= ? AND r.quality= ?",
                ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY
            );
            statement.setString(1, name);
            statement.setString(2, quality);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.first()) {

                // Retrieve plan's category and the list of its components
                Category category = categoryManager.find(resultSet.getInt("category_id"));
                List<Component> components = componentManager.findPlanComponents(resultSet.getInt("plan_id"));

                plan = new Plan(
                    resultSet.getInt("plan_id"), name, quality,
                    resultSet.getString("icon"), category, components
                );
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return plan;
    }

    /**
     * Retrieve all {@link Plan}s with the given quality.
     *
     * @param quality {@link Plan#quality}
     * @return {@link List} of {@link Plan} corresponding to the given quality.
     */
    public List<Plan> getAll(String quality) {
        List<Plan> plans = new ArrayList<>();
        CategoryManager categoryManager = new CategoryManager();
        ComponentManager componentManager = new ComponentManager();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM recipe WHERE quality = ?"
            );
            statement.setString(1, quality);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                plans.add(new Plan(
                    resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("quality"),
                    resultSet.getString("icon"), categoryManager.find(resultSet.getInt("category_id")),
                    componentManager.findPlanComponents(resultSet.getInt("id"))
                ));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return plans;
    }
}
