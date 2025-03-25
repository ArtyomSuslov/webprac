package ru.msu.cmc.webprac.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.DAO.BookCopyDAO;
import ru.msu.cmc.webprac.entities.BookCopy;
import ru.msu.cmc.webprac.entities.BookStatus;

import java.util.List;

@Repository
public class BookCopyDAOImpl extends CommonDAOImpl<BookCopy, Long> implements BookCopyDAO {

    public BookCopyDAOImpl() {
        super(BookCopy.class);
    }

    @Override
    public List<BookCopy> getAllBookCopyByBookId(Long bookId) {
        try (Session session = sessionFactory.openSession()) {
            Query<BookCopy> query = session
                    .createQuery("FROM BookCopy WHERE book.id = :gotBookId", BookCopy.class)
                    .setParameter("gotBookId", bookId);
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }

    @Override
    public List<BookCopy> getAllBookCopyByStatus(BookStatus status) {
        try (Session session = sessionFactory.openSession()) {
            Query<BookCopy> query = session
                    .createQuery("FROM BookCopy WHERE status = :gotStatus", BookCopy.class)
                    .setParameter("gotStatus", status);
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }
}