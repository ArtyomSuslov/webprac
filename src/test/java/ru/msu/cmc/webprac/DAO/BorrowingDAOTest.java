package ru.msu.cmc.webprac.DAO;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.Borrowing;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class BorrowingDAOTest {

    @Autowired
    private BorrowingDAO borrowingDAO;

    @Test
    void getAllBorrowingByReaderIdTest() {
        List<Borrowing> borrowingList;

        borrowingList = borrowingDAO.getAllBorrowingByReaderId(1L);
        assertEquals(1, borrowingList.size());

        borrowingList = borrowingDAO.getAllBorrowingByReaderId(0L);
        assertEquals(0, borrowingList.size());
    }

    @Test
    void getAllBorrowingByBookCopyId() {
        List<Borrowing> borrowingList;

        borrowingList = borrowingDAO.getAllBorrowingByBookCopyId(2L);
        assertEquals(1, borrowingList.size());

        borrowingList = borrowingDAO.getAllBorrowingByBookCopyId(1L);
        assertEquals(0, borrowingList.size());
    }

    @Test
    void getAllBorrowingWithoutReturnDate() {
        List<Borrowing> borrowingList;

        borrowingList = borrowingDAO.getAllBorrowingWithoutReturnDate();
        assertEquals(3, borrowingList.size());
    }
}