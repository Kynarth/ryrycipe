package ryrycipe.model.manager;


import ryrycipe.model.Category;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manage Category object from ryrycipe database.
 */
public class CategoryManager {

    public Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a Category by its id in the database.
     *
     * @param id
     * @return Category
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
