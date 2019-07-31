package com.vincent.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.demo.entity.Book;
import com.vincent.demo.entity.Publisher;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTest {

    private ObjectMapper mapper = new ObjectMapper();

//    @Test
    public void testSerializeBookToJSON() throws Exception {
        Book book = new Book();
        book.setId("B0001");
        book.setName("Operation Management");
        book.setPrice(250);
        book.setIsbn("978-986-123-456-7");

        Publisher publisher = new Publisher();
        publisher.setCompanyName("Taipei Company");
        publisher.setAddress("Taipei");
        publisher.setTel("02-1234-5678");
        book.setPublisher(publisher);

        String bookJSONStr = mapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(bookJSONStr);

        assertEquals(book.getId(), bookJSON.getString("id"));
        assertEquals(book.getName(), bookJSON.getString("name"));
        assertEquals(book.getPrice(), bookJSON.getInt("price"));
        assertEquals(book.getIsbn(), bookJSON.getString("isbn"));

        JSONObject publisherJSON = bookJSON.getJSONObject("publisher");
        assertEquals(publisher.getCompanyName(),
                publisherJSON.getString("companyName"));
        assertEquals(publisher.getAddress(),
                publisherJSON.getString("address"));
        assertEquals(publisher.getTel(),
                publisherJSON.getString("tel"));
    }

//    @Test
    public void testDeserializeJSONToBook() throws Exception {
        JSONObject bookJSON = new JSONObject();
        bookJSON.put("id", "B0001")
                .put("name", "Operation Management")
                .put("price", 250)
                .put("isbn", "978-986-123-456-7");

        JSONObject publisherJSON = new JSONObject();
        publisherJSON.put("companyName", "Taipei Company")
                .put("address", "Taipei")
                .put("tel", "02-1234-5678")
                .put("publisher", publisherJSON);

        Book book =
                mapper.readValue(bookJSON.toString(), Book.class);

        assertEquals(bookJSON.getString("id"), book.getId());
        assertEquals(bookJSON.getString("name"), book.getName());
        assertEquals(bookJSON.getInt("price"), book.getPrice());
        assertEquals(bookJSON.getString("isbn"), book.getIsbn());

        Publisher publisher = book.getPublisher();
        assertEquals(publisherJSON.getString("companyName"),
                publisher.getCompanyName());
        assertEquals(publisherJSON.getString("address"),
                publisher.getAddress());
        assertEquals(publisherJSON.getString("tel"),
                publisher.getTel());
    }

//    @Test
    public void testChangeFieldNameWhenSerialize() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setTel("02-1234-5678");

        String publisherJSONStr = mapper.writeValueAsString(publisher);
        JSONObject publisherJSON = new JSONObject(publisherJSONStr);
        assertEquals(publisher.getTel(),
                publisherJSON.getString("telephone"));
    }

//    @Test
    public void testIgnoreFieldWhenSerialize() throws Exception {
        Book book = new Book();
        book.setId("B0001");
        book.setName("Operation Management");
        book.setPrice(250);
        book.setIsbn("978-986-123-456-7");

        String bookJSONStr = mapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(bookJSONStr);

        assertFalse(bookJSON.has("isbn"));
    }

//    @Test
    public void testIncludeNonNullField() throws Exception {
        Book book = new Book();
        book.setId("B0001");
        book.setName("Operation Management");
        book.setPrice(250);
        book.setIsbn(null);

        String bookJSONStr = mapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(bookJSONStr);

        assertFalse(bookJSON.has("isbn"));
    }

}
