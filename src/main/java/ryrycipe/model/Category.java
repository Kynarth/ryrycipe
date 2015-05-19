package ryrycipe.model;

/**
 * Plan's category
 */
public class Category {

    private int id;
    private String category;
    private String subCategory;
    private int hand;

    public Category() {
        this.id = -1;
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
        return String.valueOf(id);
    }

}
