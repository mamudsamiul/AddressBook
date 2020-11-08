package com.capgemini.addressbooktest;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.addressbook.AddressBook;
import com.capgemini.addressbook.Contacts;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookRestAPITest {

	@Before
	public void initialize() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	private Contacts[] getContactList() {
		Response response = RestAssured.get("/addressbook");
		Contacts[] arrOfContact = new Gson().fromJson(response.asString(), Contacts[].class);
		return arrOfContact;
	}

	private Response addContactToJSONServer(Contacts contact) {
		String contactJson = new Gson().toJson(contact);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(contactJson);
		return requestSpecification.post("/addressbook");
	}

	@Test
	public void givenAddressBookDataInJsonServer_WhenRetrived_ShouldMatchCount() {
		Contacts[] arrOfContact = getContactList();
		AddressBook addressBook;
		addressBook = new AddressBook(Arrays.asList(arrOfContact));
		long entries = AddressBook.contactList.size();
		Assert.assertEquals(4, entries);
	}

	@Test
	public void givenContacts_WhenAdded_ShouldMatchStatusAndCount() {
		Contacts[] arrOfContact = getContactList();
		AddressBook addressBook;
		addressBook = new AddressBook(Arrays.asList(arrOfContact));

		Contacts contact = new Contacts(0, "Samiul", "Mamud", "berhampore", "Murshidabad", "wb", "742101", "9876543210",
				"mamud@gmail.com");
		Response response = addContactToJSONServer(contact);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);

		contact = new Gson().fromJson(response.asString(), Contacts.class);
		addressBook.addNewContact(contact);
		long entries = AddressBook.contactList.size();
		Assert.assertEquals(5, entries);
	}

	@Test
	public void givenListOfContact_WhenAdded_ShouldMatch201ResponseAndCount() {
		Contacts[] arrOfContact = getContactList();
		AddressBook addressBook;
		addressBook = new AddressBook(Arrays.asList(arrOfContact));

		Contacts[] arrOfContacts = {
				new Contacts(0, "Rahul", "Ghosh", "town", "Burdwan", "wb", "712032", "9112345678", "rahul@gmail.com"),
				new Contacts(0, "Samiul", "Mamud", "berhampore", "Murshidabad", "wb", "742101", "9876543210",
						"mamud@gmail.com") };
		for (Contacts contact : arrOfContacts) {
			Response response = addContactToJSONServer(contact);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);

			contact = new Gson().fromJson(response.asString(), Contacts.class);
			addressBook.addNewContact(contact);
		}
		long entries = AddressBook.contactList.size();
		Assert.assertEquals(7, entries);
	}
}