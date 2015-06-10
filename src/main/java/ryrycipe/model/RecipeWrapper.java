package ryrycipe.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Helper class to wrap a created recipe by user. This class is used to load or save a recipe.
 */
@XmlRootElement(name = "recipe")
public class RecipeWrapper {

    private String recipeName;
    private String recipeAuthor;
    private String recipeComment;
    private String planName;

    private List<ComponentWrapper> components;

    public RecipeWrapper() {}

    public RecipeWrapper(String authorName, String recipeName, String comment, String planName) {
        this.recipeName = recipeName;
        this.recipeAuthor = authorName;
        this.recipeComment = comment;
        this.planName = planName;
    }

    @XmlAttribute(name = "name")
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @XmlAttribute(name = "author")
    public String getRecipeAuthor() {
        return recipeAuthor;
    }

    public void setRecipeAuthor(String recipeAuthor) {
        this.recipeAuthor = recipeAuthor;
    }

    @XmlElement(name = "comment")
    public String getRecipeComment() {
        return recipeComment;
    }

    public void setRecipeComment(String recipeComment) {
        this.recipeComment = recipeComment;
    }

    @XmlElement(name = "components")
    public List<ComponentWrapper> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentWrapper> components) {
        this.components = components;
    }

    @XmlElement(name = "plan_name")
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String fileName() {
        return toString() + ".xml";
    }

    public String toString() {
        return this.recipeAuthor + "_" + this.planName + "_" + this.recipeName;
    }

}
