package ryrycipe.model;

import org.jetbrains.annotations.NotNull;

/**
 * {@link Plan}'s category
 *
 * @see Plan#category
 */
public class Category implements Comparable<Category> {

    /**
     * Category's database id.
     */
    private int id;

    /**
     * First level of classification.
     */
    private String category;

    /**
     * Second level of classification.
     */
    private String subCategory;

    /**
     * NUmber of hand needed to use the crafted item from the {@link Plan}
     */
    private int hand;

    public Category() {
        this.id = -1;
    }

    public Category(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public Category(int id, String category, String subCategory) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
    }

    public Category(int id, String category, String subCategory, int hand ) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
        this.hand = hand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getHand() {
        return hand;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    public String toString() {
        return "Id: " + String.valueOf(id) + "\n" +
            "Category: " + this.category + "\n " +
            "Subcategory: " + this.subCategory + "\n" +
            "hand :" + this.hand;
    }

    @Override
    public int compareTo(@NotNull Category o) {
        return this.toString().compareTo(o.toString());
    }
}
