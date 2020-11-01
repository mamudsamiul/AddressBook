package com.capgemini.addressbooktest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.addressbook.AddressBookDBException;
import com.capgemini.addressbook.AddressBookDBService;
import com.capgemini.addressbook.Contacts;

import junit.framework.Assert;

public class AddressBookDBTest {

	private AddressBookDBService addressBookDBService;

	@Before
	public void initialize() {
		addressBookDBService = new AddressBookDBService();
	}

	@Test
	public void givenAddressBookDB_ShouldMatchCount() throws AddressBookDBException {
		List<Contacts> contactList = addressBookDBService.readContacts();
		Assert.assertEquals(7, contactList.size());
	}

	@Test
	public void givenAddressBookDB_WhenCityUpdated_ShouldSync() throws AddressBookDBException {
		addressBookDBService.updatePersonAddress("Tom", "city", "kolkata");
		Contacts contact = addressBookDBService.isAddressBookInSyncWithDB("Tom");
		Assert.assertEquals("kolkata", contact.getCity());
	}

	@Test
	public void givenAddressBookDB_WhenStateUpdated_ShouldSync() throws AddressBookDBException {
		addressBookDBService.updatePersonAddress("Rahul", "state", "kerala");
		Contacts contact = addressBookDBService.isAddressBookInSyncWithDB("Rahul");
		Assert.assertEquals("kerala", contact.getState());
	}

	@Test
	public void givenAddressBookDB_WhenRetrivedBasedOnDate_ShouldReturnCount() throws AddressBookDBException {
		LocalDate startDate = LocalDate.of(2017, 01, 01);
		LocalDate endDate = LocalDate.now();
		int noOfContacts = addressBookDBService.getContactsOnDateRange(startDate, endDate);
		Assert.assertEquals(5, noOfContacts);
	}

	@Test
	public void givenAddressBookDB_WhenRetrivedBasedOnCity_ShouldReturnCount() throws AddressBookDBException {
		int noOfContacts = addressBookDBService.retriveBasedOnField("city", "kolkata");
		Assert.assertEquals(4, noOfContacts);
	}

	@Test
	public void givenAddressBookDB_WhenRetrivedBasedOnState_ShouldReturnCount() throws AddressBookDBException {
		int noOfContacts = addressBookDBService.retriveBasedOnField("state", "Bihar");
		Assert.assertEquals(1, noOfContacts);
	}

	@Test
	public void whenContactAddedToDB_ShouldMatchCount() throws AddressBookDBException {
		addressBookDBService.addContactToDB("Supriyo", "Gain", "malda", "malda", "wb", "752101", "987",
				"gain@yahoo.co.in", LocalDate.now());
		List<Contacts> contactList = addressBookDBService.readContacts();
		Assert.assertEquals(8, contactList.size());
	}

	@Test
	public void whenAddedMultipleContacts_ShouldMatchCount() throws AddressBookDBException {
		Contacts[] arrOfContacts = {
				new Contacts("Abc", "Bcd", "c", "d", "e", "123", "1234567890", "abc@gmail.com", LocalDate.now()),
				new Contacts("Fgh", "Ghi", "h", "i", "j", "234", "1234567890", "fgh@gmail.com", LocalDate.now()),
				new Contacts("Klm", "Lmn", "m", "n", "o", "432", "1234567890", "klm@gmail.com", LocalDate.now()) };
		addressBookDBService.addMultipleContacts(Arrays.asList(arrOfContacts));
		List<Contacts> contactList = addressBookDBService.readContacts();
		Assert.assertEquals(10, contactList.size());
	}
}
