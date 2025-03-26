package ru.msu.cmc.webprac.DAO;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class AuthorDAOTest {

    @Autowired
    private AuthorDAO authorDAO;

    @Test
    void getAllAuthorByNameTest() {
        List<Author> authorList;

        authorList = authorDAO.getAllAuthorByName("Лев Толстой");
        assertEquals(1, authorList.size());

        authorList = authorDAO.getAllAuthorByName("testtesttest");
        assertEquals(0, authorList.size());
    }

    @Test
    void getAllBookByAuthorIdTest() {
        List<Book> bookList;

        bookList = authorDAO.getAllBookByAuthorId(7L);
        assertEquals(2, bookList.size());

        bookList = authorDAO.getAllBookByAuthorId(0L);
        assertEquals(0, bookList.size());
    }
}