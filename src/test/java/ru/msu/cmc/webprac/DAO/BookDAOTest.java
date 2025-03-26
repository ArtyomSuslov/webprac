package ru.msu.cmc.webprac.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class BookDAOTest {

    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void getSingleBookByTitleTest() {
        Book book;

        book = bookDAO.getSingleBookByTitle("Анна Каренина");
        assertNotNull(book);

        book = bookDAO.getSingleBookByTitle("testtesttest");
        assertNull(book);
    }

    @Test
    void getAllBookFromYearTest() {
        List<Book> bookList;

        bookList = bookDAO.getAllBookFromYear(1877L);
        assertEquals(1, bookList.size());

        bookList = bookDAO.getAllBookFromYear(0L);
        assertEquals(0, bookList.size());
    }

    @Test
    void getSingleBookByIsbnTest() {
        Book book;

        book = bookDAO.getSingleBookByIsbn("9781234567897");
        assertNotNull(book);

        book = bookDAO.getSingleBookByIsbn("0000000000000");
        assertNull(book);
    }

    @Test
    void getAllAuthorByBookIdTest() {
        List<Author> authorList;

        authorList = bookDAO.getAllAuthorByBookId(6L);
        assertEquals(2, authorList.size());

        authorList = bookDAO.getAllAuthorByBookId(0L);
        assertEquals(0, authorList.size());
    }

    @Test
    void getAllAvailableBookCopyByBookIdTest() {
        List<BookCopy> bookCopyList;

        bookCopyList = bookDAO.getAllAvailableBookCopyByBookId(1L);
        assertEquals(1, bookCopyList.size());

        bookCopyList = bookDAO.getAllAvailableBookCopyByBookId(3L);
        assertEquals(0, bookCopyList.size());
    }

    @Test
    void getAllBookByFilterTest() {
        BookDAO.Filter filter;
        List<Book> bookList;

        filter = BookDAO.Filter.builder()
                .authorId(1L)
                .publisher("Издательство 1")
                .year(1877L)
                .status(BookStatus.available)
                .build();
        bookList = bookDAO.getAllBookByFilter(filter);
        assertEquals(1, bookList.size());

        filter = BookDAO.Filter.builder()
                .authorId(1L)
                .build();
        bookList = bookDAO.getAllBookByFilter(filter);
        assertEquals(1, bookList.size());

        filter = BookDAO.Filter.builder()
                .publisher("Издательство 1")
                .build();
        bookList = bookDAO.getAllBookByFilter(filter);
        assertEquals(1, bookList.size());

        filter = BookDAO.Filter.builder()
                .year(1877L)
                .build();
        bookList = bookDAO.getAllBookByFilter(filter);
        assertEquals(1, bookList.size());

        filter = BookDAO.Filter.builder()
                .status(BookStatus.available)
                .build();
        bookList = bookDAO.getAllBookByFilter(filter);
        assertEquals(5, bookList.size());
    }
}