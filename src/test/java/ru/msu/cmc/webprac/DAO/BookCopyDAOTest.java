package ru.msu.cmc.webprac.DAO;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.BookCopy;
import ru.msu.cmc.webprac.entities.BookStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class BookCopyDAOTest {

    @Autowired
    private BookCopyDAO bookCopyDAO;

    @Test
    void getAllBookCopyByBookIdTest() {
        List<BookCopy> bookCopyList;

        bookCopyList = bookCopyDAO.getAllBookCopyByBookId(1L);
        assertEquals(2, bookCopyList.size());

        bookCopyList = bookCopyDAO.getAllBookCopyByBookId(0L);
        assertEquals(0, bookCopyList.size());
    }

    @Test
    void getAllBookCopyByStatusTest() {
        List<BookCopy> bookCopyList;

        bookCopyList = bookCopyDAO.getAllBookCopyByStatus(BookStatus.borrowed);
        assertEquals(4, bookCopyList.size());

        bookCopyList = bookCopyDAO.getAllBookCopyByStatus(null);
        assertEquals(0, bookCopyList.size());
    }
}