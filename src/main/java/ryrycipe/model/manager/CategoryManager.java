package ryrycipe.model.manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryManager.class.getName());

    /**
     * Connection to the ryrycipe database.
     * @see DBConnection
     */
    public Connection connection = DBConnection.getInstance();

    /**
     * Retrieve a {@link Category} by its id in the database.
     *
     * @param id {@link Category#id}
     * @return Return a filled {@link Category} if id is correct otherwise empty one.
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
            LOGGER.error(e.getMessage());
        }

        return category;
    }
}
