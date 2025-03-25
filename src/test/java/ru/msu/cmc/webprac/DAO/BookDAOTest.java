package ru.msu.cmc.webprac.DAO;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;

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
        List<Book> readerListAll = (List<Book>) bookDAO.getAll();
        assertEquals(7, readerListAll.size());

        List<Author> authorList = bookDAO.getAllAuthorByBookId(bookDAO.getSingleBookByTitle("Двенадцать стульев").getId());
        assertEquals(2, authorList.size());
    }
}