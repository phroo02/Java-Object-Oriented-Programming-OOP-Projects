package diet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToDoubleFunction;

/**
 * Represents a complete menu.
 * 
 * It can be made up of both packaged products and servings of given recipes.
 *
 */
public class Menu implements NutritionalElement {
	private Map<String, Double> recpies = new TreeMap<>();
	private List<String> products = new ArrayList<>();
	/**
	 * Adds a given serving size of a recipe.
	 * The recipe is a name of a recipe defined in the {@code food}
	 * argument of the constructor.
	 * 
	 * @param recipe the name of the recipe to be used as ingredient
	 * @param quantity the amount in grams of the recipe to be used
	 * @return the same Menu to allow method chaining
	 */
	private String name;
	private Food food;
	public Menu(String name, Food food){
		this.name = name;
		this.food = food;
	}
	public Menu addRecipe(String recipe, double quantity) {
		recpies.put(recipe, quantity);
		return this;
	}

	/**
	 * Adds a unit of a packaged product.
	 * The product is a name of a product defined in the {@code food}
	 * argument of the constructor.
	 * 
	 * @param product the name of the product to be used as ingredient
	 * @return the same Menu to allow method chaining
	 */
    	public Menu addProduct(String product) {
		products.add(product);
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	private Double computePer100Grams(ToDoubleFunction<NutritionalElement> extractor){
		Double totalNut = 0.0;
		Double totalWeight = 0.0;
		for(Map.Entry<String, Double> entry : recpies.entrySet()){
			NutritionalElement r = food.getRecipe(entry.getKey());
			if(r==null) continue;
			Double weight = entry.getValue();
			totalNut += extractor.applyAsDouble(r) * (weight/100);
			totalWeight += weight;
		}
		for(String s : products){
			NutritionalElement p = food.getProduct(s);
			if(p==null) continue;
			totalNut += extractor.applyAsDouble(p);
		}
		return totalNut ;

	}

	/**
	 * Total KCal in the menu
	 */
	@Override
	public double getCalories() {
		return computePer100Grams(NutritionalElement::getCalories);
	}

	/**
	 * Total proteins in the menu
	 */
	@Override
	public double getProteins() {
		return computePer100Grams(NutritionalElement::getProteins);
	}

	/**
	 * Total carbs in the menu
	 */
	@Override
	public double getCarbs() {
		return computePer100Grams(NutritionalElement::getCarbs);
	}

	/**
	 * Total fats in the menu
	 */
	@Override
	public double getFat() {
		return computePer100Grams(NutritionalElement::getFat);
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Menu} class it must always return {@code false}:
	 * nutritional values are provided for the whole menu.
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
		return false;
	}
}