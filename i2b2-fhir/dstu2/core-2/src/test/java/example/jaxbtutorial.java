/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import org.hl7.fhir.Medication;
import org.junit.Test;

public class jaxbtutorial {

	//@Test
	public void test() throws JAXBException, IOException{
		 final String BOOKSTORE_XML = "./bookstore-jaxb.xml";

		   

		    ArrayList<Book> bookList = new ArrayList<Book>();

		    // create books
		    Book book1 = new Book();
		    book1.setIsbn("978-0060554736");
		    book1.setName("The Game");
		    book1.setAuthor("Neil Strauss");
		    book1.setPublisher("Harpercollins");
		    bookList.add(book1);

		    Book book2 = new Book();
		    book2.setIsbn("978-3832180577");
		    book2.setName("Feuchtgebiete");
		    book2.setAuthor("Charlotte Roche");
		    book2.setPublisher("Dumont Buchverlag");
		    bookList.add(book2);

		    // create bookstore, assigning book
		    Bookstore bookstore = new Bookstore();
		    bookstore.setName("Fraport Bookstore");
		    bookstore.setLocation("Frankfurt Airport");
		    bookstore.setBookList(bookList);

		    // create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance(Bookstore.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
		    m.marshal(bookstore, System.out);

		    // Write to File
		    m.marshal(bookstore, new File(BOOKSTORE_XML));

		    // get variables from our xml file, created before
		    System.out.println();
		    System.out.println("Output from our XML File: ");
		    Unmarshaller um = context.createUnmarshaller();
		    Bookstore bookstore2 = (Bookstore) um.unmarshal(new FileReader(BOOKSTORE_XML));
		    ArrayList<Book> list = bookstore2.getBooksList();
		    for (Book book : list) {
		      System.out.println("Book: " + book.getName() + " from "
		          + book.getAuthor());
		    }
		  }	
/*	
	//@Test
	public void test1() throws JAXBException, IOException{
		 final String BOOKSTORE_XML = "./tmp.xml";

		   

		    ArrayList<FhirResourceWithMetaData> list = new ArrayList<FhirResourceWithMetaData>();

		    // create books
		    FhirResourceWithMetaDataSet s1= new FhirResourceWithMetaDataSet();
		    FhirResourceWithMetaData r1= new FhirResourceWithMetaData();
		    MetaData md1= new MetaData();
		    md1.setId("medication/1");
		    md1.setLastUpdated("2015-04-22");
		    
		    Medication m1= new Medication();
		    org.hl7.fhir.String str1=new org.hl7.fhir.String();
		    str1.setValue("med1");
		    m1.setName(str1);
		   r1.setRmd(md1);
		    r1.setR(m1);
		    list.add(r1);
		    s1.setLists(list);
		    
		 
		    // create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance( FhirResourceWithMetaDataSet.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
		    m.marshal(s1, System.out);

		    // Write to File
		    m.marshal(s1, new File(BOOKSTORE_XML));

		    
		    // get variables from our xml file, created before
		    System.out.println();
		    System.out.println("Output from our XML File: ");
		    Unmarshaller um = context.createUnmarshaller();
		    FhirResourceWithMetaDataSet s2= (FhirResourceWithMetaDataSet) um.unmarshal(new FileReader(BOOKSTORE_XML));
		    ArrayList<FhirResourceWithMetaData> list1 = (ArrayList<FhirResourceWithMetaData>) s2.getLists();
		    for (FhirResourceWithMetaData rm : list1) {
		      System.out.println("FhirResourceWithMetaData: " +rm.getRmd().getId() );
		    }
		  }	
*/
	
	
}
