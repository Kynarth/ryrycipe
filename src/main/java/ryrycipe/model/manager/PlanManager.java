package ryrycipe.model.manager;

import ryrycipe.model.Plan;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM recipe as r" +
                    "INNER JOIN recipe_category as rc ON r.category_id = rc.id" +
                    "INNER JOIN recipe_component as rcmp ON rcmp.recipe_id = r.id" +
                    "WHERE r.name= ? AND r.quality= ?"
            );
            statement.setString(1, name);
            statement.setString(2, quality);

            ResultSet resultSet = statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plan;
    }
}
