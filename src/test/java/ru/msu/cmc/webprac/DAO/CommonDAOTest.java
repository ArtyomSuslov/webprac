package ru.msu.cmc.webprac.DAO;

import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.*;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class CommonDAOTest {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private ReaderDAO readerDAO;

    @Autowired
    private AuthorDAO authorDAO;

    @Test
    void getByIdTest() {
        Book book;

        book = bookDAO.getById(1L);
        assertNotNull(book);
    }

    @Test
    void getAllTest() {
        List<Book> bookList;

        bookList = (List<Book>) bookDAO.getAll();
        assertEquals(7, bookList.size());
    }

    @Test
    void saveDeleteTest() {
        Book book = new Book(
                10L,
                "save_title",
                "test_publisher",
                2025L,
                "1111111111111",
                null,
                null);

        bookDAO.save(book);

        book = bookDAO.getSingleBookByTitle("save_title");
        assertNotNull(book);

        bookDAO.delete(book);
        book = bookDAO.getSingleBookByTitle("save_title");
        assertNull(book);
    }

    @Test
    void saveCollectionTest() {
        List<Book> bookList = new ArrayList<>();

        bookList.add(new Book(
                10L,
                "save_title1",
                "test_publisher1",
                2025L,
                "1111111111111",
                null,
                null));

        bookList.add(new Book(
                11L,
                "save_title2",
                "test_publisher1",
                2025L,
                "1111111111112",
                null,
                null));

        int prevSize = bookDAO.getAll().size();

        bookDAO.saveCollection(bookList);

        assertEquals(2, bookDAO.getAll().size() - prevSize);

        bookDAO.delete(bookDAO.getSingleBookByTitle("save_title1"));
        bookDAO.delete(bookDAO.getSingleBookByTitle("save_title2"));
    }

    @Test
    void updateTest() {
        Book book = new Book(
                10L,
                "save_title",
                "test_publisher",
                2025L,
                "1111111111111",
                null,
                null);

        bookDAO.save(book);

        assertNotNull(bookDAO.getSingleBookByTitle("save_title"));

        book = bookDAO.getSingleBookByTitle("save_title");
        book.setTitle("update_title");

        bookDAO.update(book);
        assertNull(bookDAO.getSingleBookByTitle("save_title"));
        assertEquals("update_title", bookDAO.getSingleBookByTitle("update_title").getTitle());

        bookDAO.delete(bookDAO.getSingleBookByTitle("update_title"));
    }

    @Test
    public void deleteByIdTest() {
        Book book = new Book(
                10L,
                "save_title",
                "test_publisher",
                2025L,
                "1111111111111",
                null,
                null);

        bookDAO.save(book);

        assertNotNull(bookDAO.getSingleBookByTitle("save_title"));

        bookDAO.deleteById(bookDAO.getSingleBookByTitle("save_title").getId());

        assertNull(bookDAO.getSingleBookByTitle("save_title"));
    }
}