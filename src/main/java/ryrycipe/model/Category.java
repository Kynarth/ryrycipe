package ryrycipe.model;

import com.google.common.base.Objects;

/**
 * {@link Plan}'s category
 *
 * @see Plan#category
 */
public class Category {

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

    /**
     * Each craft plan in Ryzom can be categorized in categories (weapon, jewel...),
     * themselves divided in subcategories (One-handed, boots ...)
     */
    public Category() {
        this.id = -1;
    }

    /**
     * Jewel category.
     *
     * @param id Category's id in the database.
     * @param category One of 4 great category: Weapon, Jewel, Armor, Ammo: here Jewel.
     */
    public Category(int id, String category) {
        this.id = id;
        this.category = category;
    }

    /**
     * Ammo and Armor category.
     * @param id Category's id in the database.
     * @param category One of 4 great category: Weapon, Jewel, Armor, Ammo: here Ammo or Armor.
     * @param subCategory one or two handed ranged weapon for ammo or type of armor (light, medium, shield ...).
     */
    public Category(int id, String category, String subCategory) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
    }

    /**
     * Weapon category.
     *
     * @param id Category's id in the database.
     * @param category One of 4 great category: Weapon, Jewel, Armor, Ammo: here Weapon.
     * @param subCategory Define if the weapon is melee or ranged.
     * @param hand Number of hand to handle the weapon.
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category1 = (Category) o;
        return Objects.equal(id, category1.id) &&
            Objects.equal(hand, category1.hand) &&
            Objects.equal(category, category1.category) &&
            Objects.equal(subCategory, category1.subCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, category, subCategory, hand);
    }
}
