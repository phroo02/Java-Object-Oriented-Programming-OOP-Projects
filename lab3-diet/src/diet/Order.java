package diet;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
/**
 * Represents and order issued by an {@link Customer} for a {@link Restaurant}.
 *
 * When an order is printed to a string is should look like:
 * <pre>
 *  RESTAURANT_NAME, USER_FIRST_NAME USER_LAST_NAME : DELIVERY(HH:MM):
 *  	MENU_NAME_1->MENU_QUANTITY_1
 *  	...
 *  	MENU_NAME_k->MENU_QUANTITY_k
 * </pre>
 */
public class Order {
	Customer customer;
	Restaurant restaurant;
	String time;
	OrderStatus status;
	PaymentMethod paymentMethod;
	Takeaway tk;
	private Map<String, Integer> oMenus = new TreeMap<>();


	public Order(Customer customer, String restaurantName, String time, Takeaway tk){
		this.customer = customer;
		this.restaurant = tk.getRestaurant(restaurantName);

		if (time.length() == 4) {      // e.g. "9:00"
			time = "0" + time;        // becomes "09:00"
		}
		
		LocalTime t = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
		LocalTime start = t;
		
		do {
			// check open at the LocalTime directly (no extra parsing)
			if (restaurant.isOpenAt(t.toString())) {
				break;
			}
			// move forward one minute—this wraps at midnight automatically
			t = t.plusMinutes(1);
			
		} while (!t.equals(start));  // if we've wrapped all the way back, give up
		this.time = t.format(DateTimeFormatter.ofPattern("HH:mm"));


		status = OrderStatus.ORDERED;
		paymentMethod = PaymentMethod.CASH;
		this.tk = tk;
	}
	/**
	 * Possible order statuses
	 */
	public enum OrderStatus {
		ORDERED, READY, DELIVERED
	}

	/**
	 * Accepted payment methods
	 */
	public enum PaymentMethod {
		PAID, CASH, CARD
	}

	/**
	 * Set payment method
	 * @param pm the payment method
	 */
	public void setPaymentMethod(PaymentMethod pm) {
		paymentMethod = pm;
	}

	/**
	 * Retrieves current payment method
	 * @return the current method
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Set the new status for the order
	 * @param os new status
	 */
	public void setStatus(OrderStatus os) {
		status = os;
	}

	/**
	 * Retrieves the current status of the order
	 *
	 * @return current status
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * Add a new menu to the order with a given quantity
	 *
	 * @param menu	menu to be added
	 * @param quantity quantity
	 * @return the order itself (allows method chaining)
	 */
	public Order addMenus(String menu, int quantity) {
		oMenus.put(menu, quantity);
		return this;
	}

	@Override
	public String toString(){

		StringBuilder sb = new StringBuilder();
		sb.append(restaurant.getName())
			.append(", ")
			.append(customer.getFirstName()+ " " + customer.getLastName()+" : (")
			
			.append(time + "):")
			.append("\n");
			for(String m : oMenus.keySet()){
				sb.append("\t").append(m).append("->").append(oMenus.get(m)).append("\n");
			}
		return sb.toString();
	}
	public String getDeliveryTime(){
		return time;
	}
	public Customer getCustomer(){
		return customer;
	}
	public Restaurant getRestaurant(){
		return restaurant;
	}
	public Map<String, Integer> getOmenus(){
		return oMenus;
	}

}
