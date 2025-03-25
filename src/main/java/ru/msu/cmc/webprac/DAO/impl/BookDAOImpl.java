package ru.msu.cmc.webprac.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.DAO.BookDAO;
import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;

import java.util.List;

@Repository
public class BookDAOImpl extends CommonDAOImpl<Book, Long> implements BookDAO {

    public BookDAOImpl() {
        super(Book.class);
    }

    @Override
    public Book getSingleBookByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session
                    .createQuery("FROM Book WHERE title LIKE :gotTitle", Book.class)
                    .setParameter("gotTitle", likeExpr(title));
            return !query.getResultList().isEmpty() ? query.getResultList().get(0) : null;
        }
    }

    @Override
    public List<Book> getAllBookByPublisher(String publisher) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session
                    .createQuery("FROM Book WHERE publisher LIKE :gotPublisher", Book.class)
                    .setParameter("gotPublisher", likeExpr(publisher));
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }

    @Override
    public List<Book> getAllBookFromYear(Long year) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session
                    .createQuery("FROM Book WHERE year = :gotYear", Book.class)
                    .setParameter("gotYear", year);
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }

    @Override
    public Book getSingleBookByIsbn(String isbn) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session
                    .createQuery("FROM Book WHERE title LIKE :gotIsbn", Book.class)
                    .setParameter("gotIsbn", likeExpr(isbn));
            return !query.getResultList().isEmpty() ? query.getResultList().get(0) : null;
        }
    }

    @Override
    public List<Author> getAllAuthorByBookId(Long bookId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Author> query = session
                    .createQuery("SELECT ba.author FROM BookAuthor ba " +
                            "WHERE ba.book.id = :gotBookId", Author.class)
                    .setParameter("gotBookId", bookId);
            return !query.getResultList().isEmpty() ? query.getResultList() : null;
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}