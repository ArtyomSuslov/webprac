package ru.msu.cmc.webprac.DAO;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.entities.Borrowing;
import ru.msu.cmc.webprac.entities.Reader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class ReaderDAOTest {

    @Autowired
    private ReaderDAO readerDAO;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {
        List<Reader> readerListAll = (List<Reader>) readerDAO.getAll();
        assertEquals(5, readerListAll.size());

        readerDAO.save(new Reader(
                "Тестовый субъект",
                "123123123",
                "test@test.test",
                "+7(999)999-99")
        );

        readerListAll = readerDAO.getAllReaderByName("Тестовый субъект");
        assertEquals("Тестовый субъект", readerListAll.get(0).getFullName());

        readerDAO.deleteById(readerListAll.get(0).getId());
        readerListAll = (List<Reader>) readerDAO.getAll();
        assertEquals(5, readerListAll.size());

        List<Borrowing> borrowingList = readerDAO.getAllBorrowingByName(readerListAll.get(0).getFullName());
        assertEquals(1, borrowingList.size());
    }
}