package com.capgemini.addressbooktest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.capgemini.addressbook.AddressBookFileIOService;
import com.capgemini.addressbook.Contacts;

import junit.framework.Assert;

public class AddressBookFileIOTest {
	@Test
	public void givenContactsInFileShouldRead() {
		AddressBookFileIOService addressBookFileIOService = new AddressBookFileIOService();
		List<Contacts> contactList = new ArrayList<>();
		contactList = addressBookFileIOService.readData();
		System.out.println(contactList);
		Assert.assertEquals(2, contactList.size());
	}

	@Test
	public void writeContactsToFile() {
		AddressBookFileIOService addressBookFileIOService = new AddressBookFileIOService();
		List<Contacts> contactList = new ArrayList<>();
		Contacts contact1 = new Contacts("Samiul", "Mamud", "Majdia", "Kolkata", "WB", "123456", "911234567890",
				"samiul@gmail.com");
		Contacts contact2 = new Contacts("Liton", "Kumar", "Patna", "Patna", "bihar", "123456", "919123456789",
				"liton@gmail.com");
		contactList.add(contact1);
		contactList.add(contact2);
		addressBookFileIOService.writeData(contactList);
		Assert.assertEquals(2, addressBookFileIOService.countEntries());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void givenContactsFromCSVFileShouldRead() {
		AddressBookFileIOService addressBookFileIOService = new AddressBookFileIOService();
		List<Contacts> contactList = new ArrayList<>();
		contactList = addressBookFileIOService.readCSVData();
		System.out.println(contactList);
		Assert.assertEquals(2, contactList.size());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void writeContactsToCSVFile() {
		AddressBookFileIOService addressBookFileIOService = new AddressBookFileIOService();
		List<Contacts> contactList = new ArrayList<>();
		Contacts contact1 = new Contacts("Samiul", "Mamud", "Majdia", "Kolkata", "WB", "123456", "911234567890",
				"samiul@gmail.com");
		Contacts contact2 = new Contacts("Liton", "Kumar", "Patna", "Patna", "bihar", "123456", "919123456789",
				"liton@gmail.com");
		contactList.add(contact1);
		contactList.add(contact2);
		boolean b = addressBookFileIOService.writeCSVData(contactList);
		Assert.assertTrue(b);
	}
}
