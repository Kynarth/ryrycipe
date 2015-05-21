package ryrycipe.model.manager;

import ryrycipe.model.Component;
import ryrycipe.model.Material;
import ryrycipe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage Material object from ryrycipe database.
 */
public class MaterialManager {

    public Connection connection = DBConnection.getInstance();

    /**
     * Gives the list of materials corresponding to the parameters from the materials filter.
     *
     * @param categories Contains the categories of materials. Ex: Foragaed, Quartered or both
     * @param component Define on which components the user search for materials.
     * @param quality Material's quality
     * @param faction Material's faction
     * @return A list of materials fitting the materials filter parameters
     */
    public ArrayList<Material> filter(String[] categories, String component, String quality, String faction) {
        ArrayList<Material> materials = new ArrayList<Material>();
        ComponentManager componentManager = new ComponentManager();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT m.id as material_id, m.description mc.category, mc.name as material_name" +
                    "mct.name as type_name, mct.icon, ms.faction, ms.quality " +
                    "FROM material as m " +
                    "JOIN material_category as mc " +
                    "ON m.category_id = mc.id " +
                    "JOIN material_category_type as mct " +
                    "ON mc.type_id = mct.id " +
                    "JOIN material_spec as ms " +
                    "ON ms.id = m.spec_id " +
                    "JOIN material_component as mcmp " +
                    "ON mcp.id = material_component_id " +
                    "WHERE (mc.category = ? OR mc.category = ?) " +
                    "AND ms.quality = ? AND Mmq.Faction = ? " +
                    "AND (mcp.component_id_1 = ? OR mcp.component_id_2 = ?)",
                ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY
            );
            statement.setString(1, categories[0]);
            statement.setString(2, categories[1]);
            statement.setString(3, component);
            statement.setString(4, quality);
            statement.setString(5, faction);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                // Retrieve components affiliated to the current material
                List<Component> components = componentManager.getMaterialComponents(resultSet.getString("material_id"));

                materials.add(new Material(
                    resultSet.getString("material_id"), resultSet.getString("description"),
                    resultSet.getString("category"), resultSet.getString("type_name"), resultSet.getString("quality"),
                    resultSet.getString("faction"), resultSet.getString("icon"), resultSet.getString("material_name"),
                    components
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materials;
    }
}
