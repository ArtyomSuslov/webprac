package ru.msu.cmc.webprac.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.DAO.ReaderDAO;
import ru.msu.cmc.webprac.entities.Borrowing;
import ru.msu.cmc.webprac.entities.Reader;

import java.util.Collections;
import java.util.List;

@Repository
public class ReaderDAOImpl extends CommonDAOImpl<Reader, Long> implements ReaderDAO {

    public ReaderDAOImpl() {
        super(Reader.class);
    }

    @Override
    public List<Reader> getAllReaderByName(String fullName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reader> query = session
                    .createQuery("FROM Reader WHERE fullName LIKE :gotName", Reader.class)
                    .setParameter("gotName", likeExpr(fullName));
            return !query.getResultList().isEmpty() ? query.getResultList() : Collections.emptyList();
        }
    }

    @Override
    public Reader getSingleReaderByLibraryCardNum(String libraryCardNum) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reader> query = session
                    .createQuery("FROM Reader WHERE libraryCardNum LIKE :gotLibraryCardNum", Reader.class)
                    .setParameter("gotLibraryCardNum", likeExpr(libraryCardNum));
            return !query.getResultList().isEmpty() ? query.getResultList().get(0) : null;
        }
    }

    @Override
    public Reader getSingleReaderByAddress(String address) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reader> query = session
                    .createQuery("FROM Reader WHERE address LIKE :gotAddress", Reader.class)
                    .setParameter("gotAddress", likeExpr(address));
            return !query.getResultList().isEmpty() ? query.getResultList().get(0) : null;
        }
    }

    @Override
    public Reader getSingleReaderByPhone(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reader> query = session
                    .createQuery("FROM Reader WHERE phoneNumber LIKE :gotPhoneNumber", Reader.class)
                    .setParameter("gotPhoneNumber", likeExpr(phoneNumber));
            return !query.getResultList().isEmpty() ? query.getResultList().get(0) : null;
        }
    }

    @Override
    public List<Borrowing> getAllBorrowingByReaderFullName(String fullName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Borrowing> query = session.createQuery("SELECT b FROM Borrowing b " +
                            "JOIN b.reader r " +
                            "WHERE r.fullName LIKE :gotFullName", Borrowing.class)
                    .setParameter("gotFullName", likeExpr(fullName));
            return !query.getResultList().isEmpty() ? query.getResultList() : Collections.emptyList();
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}