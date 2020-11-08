package com.capgemini.addressbook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Driver;

public class AddressBookDBService {

	private synchronized Connection getConnection() throws AddressBookDBException {
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
		Map<Integer, Boolean> contactStatusMap = new HashMap<Integer, Boolean>();
		synchronized (this) {
			contactStatusMap.put(1, false);
			Runnable task = () -> {
				String sql = "select * from addressbook;";

				try (Connection connection = this.getConnection()) {
					Statement statement = connection.createStatement();
					ResultSet result = statement.executeQuery(sql);
					AddressBook.contactList = (LinkedList<Contacts>) getContactData(result);
				} catch (SQLException | AddressBookDBException e) {
					e.printStackTrace();
				}
				contactStatusMap.put(1, true);
			};
			Thread thread = new Thread(task);
			thread.start();
		}
		while (contactStatusMap.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return AddressBook.contactList;
	}

	private List<Contacts> getContactData(ResultSet result) throws AddressBookDBException {
		List<Contacts> tempContactList = new LinkedList<Contacts>();
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
				tempContactList.add(new Contacts(firstName, lastName, address, city, state, zip, phoneNo, email));
			}
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return tempContactList;
	}

	public int updatePersonAddress(String firstName, String column, String value) throws AddressBookDBException {
		Map<Integer, Boolean> contactStatusMap = new HashMap<Integer, Boolean>();
		int[] result = new int[1];
		synchronized (this) {
			contactStatusMap.put(1, false);
			Runnable task = () -> {
				String sql = String.format("UPDATE addressbook SET %s = '%s' WHERE first_name = '%s';", column, value,
						firstName);
				try (Connection connection = this.getConnection()) {
					Statement statement = connection.createStatement();
					result[0] = statement.executeUpdate(sql);
				} catch (SQLException | AddressBookDBException e) {
					e.printStackTrace();
				}
				contactStatusMap.put(1, true);
			};
			Thread thread = new Thread(task);
			thread.start();
		}
		while (contactStatusMap.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result[0];
	}

	public Contacts isAddressBookInSyncWithDB(String firstName) throws AddressBookDBException {
		List<Contacts> tempList = this.readContacts();
		return tempList.stream().filter(contact -> contact.getFirstName().contentEquals(firstName)).findFirst()
				.orElse(null);
	}

	public int getContactsOnDateRange(LocalDate startDate, LocalDate endDate) throws AddressBookDBException {
		String sql = String.format("SELECT Id FROM addressbook WHERE joining BETWEEN '%s' AND '%s';",
				Date.valueOf(startDate), Date.valueOf(endDate));
		int noOfContacts = 0;
		try (Connection connection = getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				noOfContacts++;
			}
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return noOfContacts;
	}

	public int retriveBasedOnField(String field, String value) throws AddressBookDBException {
		String sql = String.format("SELECT id FROM addressbook WHERE %s = '%s';", field, value);
		int noOfContacts = 0;
		try (Connection connection = getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				noOfContacts++;
			}
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return noOfContacts;
	}

	public void addContactToDB(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNo, String email, LocalDate start) throws AddressBookDBException {
		int id = -1;
		Contacts contact = null;
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO addressbook (first_name, last_name, address, city, state, zip, phone, email, joining)"
							+ "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
					firstName, lastName, address, city, state, zip, phoneNo, email, Date.valueOf(start));
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
			}
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.INCORRECT_INFO, e.getMessage());
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR,
							e.getMessage());
				}
		}
	}

	public void addMultipleContacts(List<Contacts> contactsToAddList) throws AddressBookDBException {
		Map<Integer, Boolean> contactStatusMap = new HashMap<Integer, Boolean>();
		contactsToAddList.forEach(contact -> {
			contactStatusMap.put(contact.hashCode(), false);
			Runnable task = () -> {
				try {
					this.addContactToDB(contact.getFirstName(), contact.getLastName(), contact.getAddress(),
							contact.getCity(), contact.getState(), contact.getZip(), contact.getPhoneNo(),
							contact.getEmail(), contact.getStartDate());
				} catch (AddressBookDBException e) {
					e.printStackTrace();
				}
				contactStatusMap.put(contact.hashCode(), true);
			};
			Thread thread = new Thread(task, contact.getFirstName());
			thread.start();
		});
		while (contactStatusMap.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new AddressBookDBException(AddressBookDBException.ExceptionType.CONNECTION_ERROR, e.getMessage());
			}
		}
	}
}