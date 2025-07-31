package diet;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import diet.Order.OrderStatus;

/**
 * Represents a restaurant class with given opening times and a set of menus.
 */
public class Restaurant {
	
	/**
	 * retrieves the name of the restaurant.
	 *
	 * @return name of the restaurant
	 */
	private String name;
	private Food food;
	private Takeaway tk;
	private final List<LocalTime> opens = new ArrayList<>();
	private final List<LocalTime> closes = new ArrayList<>();
	private final Map<String,Menu> menus = new TreeMap<>();

	public Restaurant(String name, Food food, Takeaway tk){
		this.name = name;
		this.food=food;
		this.tk=tk;
	}
	public String getName() {
		return name;
	}

	/**
	 * Define opening times.
	 * Accepts an array of strings (even number of elements) in the format {@code "HH:MM"},
	 * so that the closing hours follow the opening hours
	 * (e.g., for a restaurant opened from 8:15 until 14:00 and from 19:00 until 00:00,
	 * arguments would be {@code "08:15", "14:00", "19:00", "00:00"}).
	 *
	 * @param hm sequence of opening and closing times
	 */
	public void setHours(String ... hm) {
		opens.clear();
		closes.clear();
		for(int i=0; i<hm.length; i+=2){
			opens.add(LocalTime.parse(hm[i]));
			closes.add(LocalTime.parse(hm[i+1]));
		}
	}
	

	/**
	 * Checks whether the restaurant is open at the given time.
	 *
	 * @param time time to check
	 * @return {@code true} is the restaurant is open at that time
	 */
	public boolean isOpenAt(String time){
		LocalTime now = LocalTime.parse(time);
		for(int i=0; i<opens.size(); i++){
			LocalTime open = opens.get(i);
			LocalTime close = closes.get(i);

			if(open.isBefore(close)){
				//simple day interval
				if(!now.isBefore(open) && now.isBefore(close)){
					return true;
				}
				
			}else{
				if (!now.isBefore(open) || now.isBefore(close)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds a menu to the list of menus offered by the restaurant
	 *
	 * @param menu	the menu
	 */
	public void addMenu(Menu menu) {
		menus.put(menu.getName(),menu);
	}

	/**
	 * Gets the restaurant menu with the given name
	 *
	 * @param name	name of the required menu
	 * @return menu with the given name
	 */
	public Menu getMenu(String name) {
		return menus.get(name);
	}

	/**
	 * Retrieve all order with a given status with all the relative details in text format.
	 *
	 * @param status the status to be matched
	 * @return textual representation of orders
	 */
	public String ordersWithStatus(OrderStatus status) {
		StringBuilder sb = new StringBuilder();
		tk.getOrders().stream().filter(e-> e.status == status)
				.sorted(Comparator.comparing((Order o) -> o.getRestaurant().getName())
						.thenComparing(o -> o.getCustomer().getFirstName())
						.thenComparing(Order::getDeliveryTime)
				)
				.forEach(e -> {
					sb.append(this.getName())
						.append(", ")
						.append(e.getCustomer().getFirstName()+ " " + e.getCustomer().getLastName()+" : (")
						.append(e.time + "):")
						.append("\n");
						for(String m : e.getOmenus().keySet()){
							sb.append("\t").append(m).append("->").append(e.getOmenus().get(m)).append("\n");
						}
					}
				);
		return sb.toString();
	}
}
