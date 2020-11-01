package com.capgemini.addressbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Driver;

public class AddressBookDBService {

	private Connection getConnection() throws AddressBookDBException {
		String jdbcURL = "jdbc:mysql://localhost:3306/addressbook_service?useSSL=false";
		String userName = "root";
		String password = "admin";
		try {
			return DriverManager.getConnection(jdbcURL, userName, password);
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
	}

	public List<Contacts> readContacts() throws AddressBookDBException {
		String sql = "SELECT * from addressbook ;";
		List<Contacts> contactList = new ArrayList<Contacts>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			contactList = getContactData(result);
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return contactList;
	}

	private List<Contacts> getContactData(ResultSet result) throws AddressBookDBException {
		List<Contacts> contactList = new ArrayList<Contacts>();
		try {
			while (result.next()) {
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String address = result.getString("address");
				String city = result.getString("city");
				String state = result.getString("state");
				String zip = result.getString("zip");
				String phoneNo = result.getString("phone");
				String email = result.getString("email");
				contactList.add(new Contacts(firstName, lastName, address, city, state, zip, phoneNo, email));
			}
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return contactList;
	}
}