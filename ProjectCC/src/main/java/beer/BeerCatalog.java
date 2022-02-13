package beer;

import java.util.Comparator;
import java.util.List;

public class BeerCatalog {
    private String id;
    private String name;
    private String brand;
    private String type;
    private int price;
    private double alcohol;
    private List<Ingredient> ingredients;
    private double waterIngredient;

    public BeerCatalog(String id, String name, String brand, String type, int price, double alcohol, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.price = price;
        this.alcohol = alcohol;
        this.ingredients = ingredients;
        setWaterIngredient();
    }

    public boolean checkIfIngredientNotInclude(String ingredient) {
        for (Ingredient actual : ingredients) {
            if (actual.getName().equals(ingredient) && actual.getRatio() == 0) {
                return true;
            }
        }
        return false;
    }

    public void setWaterIngredient() {
        for (Ingredient ingredient : ingredients) {
            waterIngredient += ingredient.getRatio();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getAlcohol() {
        return alcohol;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getBrand() {
        return brand;
    }

    public int getPrice() {
        return price;
    }

    public double getWaterIngredient() {
        return waterIngredient;
    }
}
