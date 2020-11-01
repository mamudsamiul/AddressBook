package com.capgemini.addressbook;

import com.opencsv.bean.CsvBindByName;

public class Contacts {

	@CsvBindByName
	private String firstName;

	@CsvBindByName
	private String lastName;

	@CsvBindByName
	private String address;

	@CsvBindByName
	private String city;

	@CsvBindByName
	private String state;

	@CsvBindByName
	private String zip;

	@CsvBindByName(column = "PhoneNumber")
	private String phoneNo;

	@CsvBindByName
	private String email;

	ValidateContact validateContact = new ValidateContact();

	public Contacts() {

	}

	public Contacts(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNo, String email) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setAddress(address);
		this.setCity(city);
		this.setState(state);
		this.setZip(zip);
		this.setPhoneNo(phoneNo);
		this.setEmail(email);
	}

	public String getFirstName() {
		return firstName;
	}

	public boolean setFirstName(String firstName) {
		boolean b = validateContact.validateFirstName(firstName);
		if (b)
			this.firstName = firstName;
		else
			System.out.println("Enter First Name again");
		return b;
	}

	public String getLastName() {
		return lastName;
	}

	public boolean setLastName(String lastName) {
		boolean b = validateContact.validateLastName(lastName);
		if (b)
			this.lastName = lastName;
		else
			System.out.println("Enter Last Name again");
		return b;
	}

	public String getAddress() {
		return address;
	}

	public boolean setAddress(String address) {
		boolean b = validateContact.validateAddress(address);
		if (b)
			this.address = address;
		else
			System.out.println("Enter Address again");
		return b;
	}

	public String getCity() {
		return city;
	}

	public boolean setCity(String city) {
		boolean b = validateContact.validateCity(city);
		if (b)
			this.city = city;
		else
			System.out.println("Enter City again");
		return b;
	}

	public String getState() {
		return state;
	}

	public boolean setState(String state) {
		boolean b = validateContact.validateState(state);
		if (b)
			this.state = state;
		else
			System.out.println("Enter State again");
		return b;
	}

	public String getZip() {
		return zip;
	}

	public boolean setZip(String zip) {
		boolean b = validateContact.validateZip(zip);
		if (b)
			this.zip = zip;
		else
			System.out.println("Enter Zip again");
		return b;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public boolean setPhoneNo(String phoneNo) {
		boolean b = validateContact.validatePhoneNo(phoneNo);
		if (b)
			this.phoneNo = phoneNo;
		else
			System.out.println("Enter Phone No again");
		return b;
	}

	public String getEmail() {
		return email;
	}

	public boolean setEmail(String email) {
		boolean b = validateContact.validateEmail(email);
		if (b)
			this.email = email;
		else
			System.out.println("Enter Email again");
		return b;
	}

	@Override
	public String toString() {
		return "FirstName : " + firstName + " LastName : " + lastName + " Address : " + address + " City : " + city
				+ " State : " + state + " Zip : " + zip + " Phone No : " + phoneNo + " Email : " + email;
	}
}