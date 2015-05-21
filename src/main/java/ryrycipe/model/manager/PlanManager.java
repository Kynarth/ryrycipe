package ryrycipe.model.manager;

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
 * Manage Plan object from ryrycipe database.
 */
public class PlanManager {

    public Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a Plan object by its name and quality.
     * @param name Plan's name
     * @param quality plan's quality
     * @return Plan
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
                List<Component> components = componentManager.getPlanComponents(resultSet.getInt("plan_id"));

                plan = new Plan(
                    resultSet.getInt("plan_id"), name, quality,
                    resultSet.getString("icon"), category, components
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plan;
    }

    /**
     * Retrieve all plan with the given quality.
     *
     * @param quality Plan's quality.
     * @return An ArrayList of Plan object corresponding to the given quality.
     */
    public ArrayList<Plan> getAll(String quality) {
        ArrayList<Plan> plans = new ArrayList<Plan>();
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
                    componentManager.getPlanComponents(resultSet.getInt("id"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plans;
    }
}
