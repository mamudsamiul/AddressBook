package com.capgemini.addressbook;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.CSVWriter;

public class AddressBookFileIOService {
	public static String CONTACT_FILE_NAME = "contactRead.txt";
	public static String CONTACT_SECOND_FILE_NAME = "contactWrite.txt";

	public static final String SAMPLE_CSV_FILE_PATH = "./read.csv";
	public static final String SAMPLE_CSV_FILE_PATH2 = "./write.csv";

	public List<Contacts> readData() {
		List<Contacts> contactsList = new ArrayList<>();
		try {
			Files.lines(new File(CONTACT_FILE_NAME).toPath()).map(line -> line.trim()).forEach(line -> {
				String[] words = line.split("[\\s,:]+");

				Contacts contact = new Contacts();
				contact.setFirstName(words[1]);
				contact.setLastName(words[3]);
				contact.setAddress(words[5]);
				contact.setCity(words[7]);
				contact.setState(words[9]);
				contact.setZip(words[11]);
				contact.setPhoneNo(words[13]);
				contact.setEmail(words[15]);

				contactsList.add(contact);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contactsList;
	}

	public void writeData(List<Contacts> contactList) {
		StringBuffer empBuffer = new StringBuffer();
		contactList.forEach(contact -> {
			String employeeDataString = contact.toString().concat("\n");
			empBuffer.append(employeeDataString);
		});
		try {
			Files.write(Paths.get(CONTACT_SECOND_FILE_NAME), empBuffer.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long countEntries() {
		long entries = 0;
		try {
			entries = Files.lines(new File(CONTACT_SECOND_FILE_NAME).toPath()).count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}

	public List<Contacts> readCSVData() {
		List<Contacts> contactsList = new ArrayList<>();
		try {
			Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
			CsvToBean<Contacts> csvToBean = new CsvToBeanBuilder<Contacts>(reader).withType(Contacts.class)
					.withIgnoreLeadingWhiteSpace(true).build();

			contactsList = csvToBean.parse();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return contactsList;
	}

	public boolean writeCSVData(List<Contacts> contactList) {
		try (Writer writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE_PATH2))) {
			StatefulBeanToCsv<Contacts> beanToCsv = new StatefulBeanToCsvBuilder<Contacts>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			beanToCsv.write(contactList);
		} catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
