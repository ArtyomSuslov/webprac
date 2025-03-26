package ru.msu.cmc.webprac.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;

import org.springframework.stereotype.Repository;

import ru.msu.cmc.webprac.DAO.BookDAO;
import ru.msu.cmc.webprac.entities.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
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
    public List<Book> getAllBookFromYear(Long year) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session
                    .createQuery("FROM Book WHERE year = :gotYear", Book.class)
                    .setParameter("gotYear", year);
            return !query.getResultList().isEmpty() ? query.getResultList() : Collections.emptyList();
        }
    }

    @Override
    public Book getSingleBookByIsbn(String isbn) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session
                    .createQuery("FROM Book WHERE isbn LIKE :gotIsbn", Book.class)
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
            return !query.getResultList().isEmpty() ? query.getResultList() : Collections.emptyList();
        }
    }

    @Override
    public List<BookCopy> getAllAvailableBookCopyByBookId(Long bookId) {
        try (Session session = sessionFactory.openSession()) {
            Query<BookCopy> query = session
                    .createQuery("SELECT bc FROM BookCopy bc " +
                            "WHERE bc.book.id = :gotBookId " +
                            "AND bc.status = :gotStatus", BookCopy.class)
                    .setParameter("gotBookId", bookId)
                    .setParameter("gotStatus", BookStatus.available);
            return !query.getResultList().isEmpty() ? query.getResultList() : Collections.emptyList();
        }
    }

    @Override
    public List<Book> getAllBookByFilter(Filter filter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getAuthorId() != null) {
                Join<Book, BookAuthor> bookAuthorJoin = root.join("bookAuthorList");
                Join<BookAuthor, Author> authorJoin = bookAuthorJoin.join("author");
                predicates.add(cb.equal(authorJoin.get("id"), filter.getAuthorId()));
            }

            if (filter.getPublisher() != null) {
                predicates.add(cb.equal(root.get("publisher"), filter.getPublisher()));
            }

            if (filter.getYear() != null) {
                predicates.add(cb.equal(root.get("year"), filter.getYear()));
            }

            if (filter.getStatus() != null) {
                Join<Book, BookCopy> bookCopyJoin = root.join("bookCopyList");
                predicates.add(cb.equal(bookCopyJoin.get("status"), filter.getStatus()));
            }

            query.select(root).where(predicates.toArray(new Predicate[0])).distinct(true);

            return session.createQuery(query).getResultList();
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}