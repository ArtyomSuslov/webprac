package ru.msu.cmc.webprac.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.DAO.BorrowingDAO;
import ru.msu.cmc.webprac.entities.Borrowing;

import java.util.List;

@Repository
public class BorrowingDAOImpl extends CommonDAOImpl<Borrowing, Long> implements BorrowingDAO {

    public BorrowingDAOImpl() {
        super(Borrowing.class);
    }

    @Override
    public List<Borrowing> getAllBorrowingByReaderId(Long readerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Borrowing> query = session
                    .createQuery("FROM Borrowing WHERE reader.id = :gotReaderId", Borrowing.class)
                    .setParameter("gotReaderId", readerId);
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }

    @Override
    public List<Borrowing> getAllBorrowingByBookCopyId(Long bookCopyId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Borrowing> query = session
                    .createQuery("FROM Borrowing WHERE bookCopy.id = :gotBookCopyId", Borrowing.class)
                    .setParameter("gotBookCopyId", bookCopyId);
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }

    @Override
    public List<Borrowing> getAllBorrowingWithoutReturnDate() {
        try (Session session = sessionFactory.openSession()) {
            Query<Borrowing> query = session
                    .createQuery("FROM Borrowing WHERE returnDate IS NULL", Borrowing.class);
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }
}