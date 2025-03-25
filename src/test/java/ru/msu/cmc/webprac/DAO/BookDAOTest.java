package ru.msu.cmc.webprac.DAO;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;
import ru.msu.cmc.webprac.entities.BookStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class BookDAOTest {

    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {
        List<Book> bookListAll = (List<Book>)bookDAO.getAll();
        assertEquals(7, bookListAll.size());

        List<Author> authorList = bookDAO.getAllAuthorByBookId(bookDAO.getSingleBookByTitle("Двенадцать стульев").getId());
        assertEquals(2, authorList.size());
    }

    @Test
    void testFilter() {
        BookDAO.Filter filter;
        List<Book> bookListFilter;

        // Getting all the books from author with id=1
        filter = BookDAO.Filter.builder().authorId(1L).build();
        bookListFilter = bookDAO.getAllBookByFilter(filter);
        assertEquals(1, bookListFilter.size());

        // Getting all the books with available copies
        filter = BookDAO.Filter.builder().status(BookStatus.available).build();
        bookListFilter = bookDAO.getAllBookByFilter(filter);
        assertEquals(5, bookListFilter.size());
    }
}