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
    private static Connection connection = DBConnection.getInstance();

    private PlanManager() {}

    /**
     * Retrieve a {@link Plan} by its name and quality in the database.
     *
     * @param name {@link Plan#name}
     * @param quality {@link Plan#quality}
     * @return Return a filled {@link Plan} if founded otherwise empty one.
     */
    public static Plan find(String name, String quality) {
        Plan plan = new Plan();

        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT r.id AS plan_id, r.icon, r.category_id FROM recipe AS r " +
                    "INNER JOIN recipe_category AS rc ON r.category_id = rc.id " +
                    "INNER JOIN recipe_component AS rcmp ON rcmp.recipe_id = r.id " +
                    "WHERE r.name= ? AND r.quality= ?"
            );
            statement.setString(1, name);
            statement.setString(2, quality);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                // Retrieve plan's category and the list of its components
                Category category = CategoryManager.find(resultSet.getInt("category_id"));
                List<Component> components = ComponentManager.findPlanComponents(resultSet.getInt("plan_id"));

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
     * @return {@link List} of {@link Plan} corresponding to the given quality or empty one if incorrect quality.
     */
    public static List<Plan> findAllPlans(String quality) {
        List<Plan> plans = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM recipe WHERE quality = ?"
            );
            statement.setString(1, quality);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                plans.add(new Plan(
                    resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("quality"),
                    resultSet.getString("icon"), CategoryManager.find(resultSet.getInt("category_id")),
                    ComponentManager.findPlanComponents(resultSet.getInt("id"))
                ));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return plans;
    }
}
