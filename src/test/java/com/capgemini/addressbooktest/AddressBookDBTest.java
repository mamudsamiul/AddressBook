package com.capgemini.addressbooktest;

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
}
