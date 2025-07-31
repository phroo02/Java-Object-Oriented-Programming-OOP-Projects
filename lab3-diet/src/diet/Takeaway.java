package diet;

import java.util.*;
import java.util.stream.Stream;


/**
 * Represents a takeaway restaurant chain.
 * It allows managing restaurants, customers, and orders.
 */
public class Takeaway {

	/**
	 * Constructor
	 * @param food the reference {@link Food} object with materials and products info.
	 */
	private Map<String, Restaurant> restaurants = new TreeMap<>();
	private List<Customer> customers = new ArrayList<>();
	private List<Order> orders = new ArrayList<>();

	private Food food;

	public Takeaway(Food food){
		this.food = food;
	}

	/**
	 * Creates a new restaurant with a given name
	 *
	 * @param restaurantName name of the restaurant
	 * @return the new restaurant
	 */
	public Restaurant addRestaurant(String restaurantName) {
		Restaurant r = new Restaurant(restaurantName, food, this);
		restaurants.put(restaurantName, r);
		return r;
	}
	public Restaurant getRestaurant(String name){
		return restaurants.get(name);
	}

	/**
	 * Retrieves the names of all restaurants
	 *
	 * @return collection of restaurant names
	 */
	public Collection<String> restaurants() {
		return restaurants.keySet();
	}

	/**
	 * Creates a new customer for the takeaway
	 * @param firstName first name of the customer
	 * @param lastName	last name of the customer
	 * @param email		email of the customer
	 * @param phoneNumber mobile phone number
	 *
	 * @return the object representing the newly created customer
	 */
	public Customer registerCustomer(String firstName, String lastName, String email, String phoneNumber) {
		Customer c = new Customer(firstName, lastName, email, phoneNumber);
		customers.add(c);
		return c;
	}

	/**
	 * Retrieves all registered customers
	 *
	 * @return sorted collection of customers
	 */
	public Collection<Customer> customers(){
		List<Customer> sortedCustomers = new ArrayList<>(customers);
		sortedCustomers.sort(Comparator.comparing(Customer::getLastName).thenComparing(Customer::getFirstName));
		return sortedCustomers;
	}


	/**
	 * Creates a new order for the chain.
	 *
	 * @param customer		 customer issuing the order
	 * @param restaurantName name of the restaurant that will take the order
	 * @param time	time of desired delivery
	 * @return order object
	 */
	public Order createOrder(Customer customer, String restaurantName, String time) {
		Order o = new Order(customer, restaurantName, time, this);
		orders.add(o);
		return o;
	}

	/**
	 * Find all restaurants that are open at a given time.
	 *
	 * @param time the time with format {@code "HH:MM"}
	 * @return the sorted collection of restaurants
	 */
	public Collection<Restaurant> openRestaurants(String time){
		Stream or = restaurants.values().stream()
				.filter(r -> r.isOpenAt(time))
				.sorted(Comparator.comparing(Restaurant::getName));
		return or.toList();
	}
	public List<Order> getOrders(){
		return orders;
	}
}
