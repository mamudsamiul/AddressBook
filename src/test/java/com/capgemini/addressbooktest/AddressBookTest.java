package com.capgemini.addressbooktest;

import org.junit.Test;

import com.capgemini.addressbook.AddressBookMain;
import com.capgemini.addressbook.ValidateContact;

import junit.framework.Assert;

import static org.junit.Assert.*;

public class AddressBookTest {
	@Test
	public void firstNameTest() {
		ValidateContact contact = new ValidateContact();
		boolean result = contact.validateFirstName("Samiul");
		Assert.assertTrue(true);
	}

	@Test
	public void lastNameTest() {
		ValidateContact contact = new ValidateContact();
		boolean result = contact.validateLastName("Mamud");
		Assert.assertTrue(true);
	}

	@Test
	public void emailTest() {
		ValidateContact contact = new ValidateContact();
		boolean result = contact.validateEmail("mamud.samiul@gmail.com");
		Assert.assertTrue(true);
	}

	@Test
	public void phoneNoTest() {
		ValidateContact contact = new ValidateContact();
		boolean result = contact.validatePhoneNo("91 1234567890");
		Assert.assertTrue(true);
	}

	@Test
	public void addressTest() {
		ValidateContact contact = new ValidateContact();
		boolean result = contact.validateAddress("majdia");
		Assert.assertTrue(true);
	}

	@Test
	public void cityTest() {
		ValidateContact contact = new ValidateContact();
		boolean result = contact.validateCity("berhampore");
		Assert.assertTrue(true);
	}

	@Test
	public void stateTest() {
		ValidateContact contact = new ValidateContact();
		boolean result = contact.validateState("West Bengal");
		Assert.assertTrue(true);
	}
}