package diet;

public class RawMaterial implements NutritionalElement{
       private String name;
       private double calories, proteins, carbs, fat;
       private Boolean per100;
       public RawMaterial(String name, Double cal, Double proteins, Double carbs, Double fat, Boolean per100){
              this.name = name;
              this.calories = cal;
              this.proteins = proteins;
              this.carbs = carbs;
              this.fat = fat;
              this.per100 = per100;
       }

       @Override
       public String getName() {
              return this.name;
       }

       @Override
       public double getCalories() {
              return this.calories;
       }

       @Override
       public double getProteins() {
              return this.proteins;
       }

       @Override
       public double getCarbs() {
              return this.carbs;   
       }

       @Override
       public double getFat() {
              return this.fat;
       }

       @Override
       public boolean per100g() {
              return this.per100;
       }

}
