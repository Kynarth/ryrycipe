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
import java.util.Map;

/**
 * Manage Material object from ryrycipe database.
 */
public class MaterialManager {

    public Connection connection = DBConnection.getInstance();

    /**
     * Gives the list of materials corresponding to the parameters from the materials filter.
     *
     * @param parameters A Map containing each value of filter's controls.
     * @return A list of materials fitting the materials filter parameters.
     */
    public ArrayList<Material> filter(Map<String ,String> parameters) {
        ArrayList<Material> materials = new ArrayList<Material>();
        ComponentManager componentManager = new ComponentManager();

        try {
            PreparedStatement statement = this.connection.prepareStatement(
                "SELECT m.id AS material_id, m.description, mc.category, mc.name AS material_name, " +
                    "mct.name AS type_name, mct.icon, ms.faction, ms.quality " +
                    "FROM material AS m " +
                    "JOIN material_category AS mc ON m.category_id = mc.id " +
                    "JOIN material_category_type AS mct ON mc.type_id = mct.id " +
                    "JOIN material_spec AS ms ON ms.id = m.spec_id " +
                    "JOIN material_component AS mcmp ON mcmp.id = material_component_id " +
                    "WHERE (mc.category = ? OR mc.category = ?) " +
                    "AND ms.quality = ? AND ms.Faction = ? " +
                    "AND (mcmp.component_id_1 = ? OR mcmp.component_id_2 = ?)"
            );
            statement.setString(1, parameters.get("foraged"));
            statement.setString(2, parameters.get("quartered"));
            statement.setString(3, parameters.get("quality"));
            statement.setString(4, parameters.get("faction"));
            statement.setString(5, parameters.get("component"));
            statement.setString(6, parameters.get("component"));

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
