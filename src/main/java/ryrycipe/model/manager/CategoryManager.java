package ryrycipe.model.manager;


import ryrycipe.model.Category;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manage {@link Category} object from ryrycipe database.
 */
public class CategoryManager {

    /**
     * Connection to the ryrycipe database.
     * @see DBConnection
     */
    public Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a {@link Category} by its id in the database.
     *
     * @param id {@link Category#id}
     * @return  {@link Category}
     */
    public Category find(int id) {
        Category category = new Category();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM recipe_category WHERE id = ?"
            );
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                category = new Category(
                    id, resultSet.getString("category"), resultSet.getString("subcategory"), resultSet.getInt("hand")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }
}
