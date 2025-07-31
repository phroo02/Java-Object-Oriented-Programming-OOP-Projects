package diet;


public class Customer {
	private String name, lastName, email, phone;
	public Customer(String name, String lastName, String email, String phone){
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}
	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void SetEmail(String email) {
		this.email = email;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString(){
		return name + " " + lastName;
	}

	
}
