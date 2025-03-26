package ru.msu.cmc.webprac.DAO;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.Borrowing;
import ru.msu.cmc.webprac.entities.Reader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class ReaderDAOTest {

    @Autowired
    private ReaderDAO readerDAO;

    @Test
    void getAllReaderByNameTest() {
        List<Reader> readerList;

        readerList = readerDAO.getAllReaderByName("Иван Иванов");
        assertEquals(1, readerList.size());

        readerList = readerDAO.getAllReaderByName("testtesttest");
        assertEquals(0, readerList.size());
    }

    @Test
    void getSingleReaderByLibraryCardNumTest() {
        Reader reader;

        reader = readerDAO.getSingleReaderByLibraryCardNum("L12345");
        assertNotNull(reader);

        reader = readerDAO.getSingleReaderByLibraryCardNum("testtesttest");
        assertNull(reader);
    }

    @Test
    void getSingleReaderByAddressTest() {
        Reader reader;

        reader = readerDAO.getSingleReaderByAddress("ivan_ivanov@example.com");
        assertNotNull(reader);

        reader = readerDAO.getSingleReaderByAddress("testtesttest");
        assertNull(reader);
    }

    @Test
    void getSingleReaderByPhone() {
        Reader reader;

        reader = readerDAO.getSingleReaderByPhone("89031234567");
        assertNotNull(reader);

        reader = readerDAO.getSingleReaderByPhone("testtesttest");
        assertNull(reader);
    }

    @Test
    void getAllBorrowingByReaderFullName() {
        List<Borrowing> borrowingList;

        borrowingList = readerDAO.getAllBorrowingByReaderFullName("Иван Иванов");
        assertEquals(1, borrowingList.size());

        borrowingList = readerDAO.getAllBorrowingByReaderFullName("testtesttest");
        assertEquals(0, borrowingList.size());
    }
}