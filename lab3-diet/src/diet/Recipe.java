package diet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Represents a recipe of the diet.
 * 
 * A recipe consists of a a set of ingredients that are given amounts of raw materials.
 * The overall nutritional values of a recipe can be computed
 * on the basis of the ingredients' values and are expressed per 100g
 * 
 *
 */
public class Recipe implements NutritionalElement {
	
	/**
	 * Adds the given quantity of an ingredient to the recipe.
	 * The ingredient is a raw material.
	 * 
	 * @param material the name of the raw material to be used as ingredient
	 * @param quantity the amount in grams of the raw material to be used
	 * @return the same Recipe object, it allows method chaining.
	 */
	private Map<String, Double> ingredients = new LinkedHashMap<>();
	private String name;
	private Food food;

	public Recipe(String name, Food food) {
		this.name = name;
		this.food = food;
	}
	public Recipe addIngredient(String material, double quantity) {
		ingredients.put(material, quantity);
		return this;
	}

	@Override
	public String getName() {
		return this.name;
	}
	private double computePer100g(java.util.function.ToDoubleFunction<NutritionalElement> extractor) {
		double totalNutrient = 0.0;
		double totalWeight = 0.0;
		for (Map.Entry<String, Double> entry : ingredients.entrySet()) {
		NutritionalElement ne = food.getRawMaterial(entry.getKey());
		if (ne == null) {
			continue; // skip undefined raw materials
		}
		double weight = entry.getValue(); // weight in grams (double)
		totalNutrient += extractor.applyAsDouble(ne) * (weight / 100.0);
		totalWeight += weight;
		}
		if (totalWeight == 0.0) {
		return 0.0;
		}
		// Scale summed nutrient to per 100g of recipe
		return totalNutrient * (100.0 / totalWeight);
   	}

	
	@Override
	public double getCalories() {
		return computePer100g(NutritionalElement::getCalories);
	}
	

	@Override
	public double getProteins() {
		return computePer100g(NutritionalElement::getProteins);
	}

	@Override
	public double getCarbs() {
		return computePer100g(NutritionalElement::getCarbs);
	}

	@Override
	public double getFat() {
		return computePer100g(NutritionalElement::getFat);
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Recipe} class it must always return {@code true}:
	 * a recipe expresses nutritional values per 100g
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
		return true;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		Stream<Map.Entry<String, Double>> s = ingredients.entrySet().stream();
		s.forEach(entry -> {
			NutritionalElement ne = food.getRawMaterial(entry.getKey());
			sb.append(ne.getName())
			.append('\n');

			

		});
		return sb.toString();
	}
	
}
