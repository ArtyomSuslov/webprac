package ru.msu.cmc.webprac.DAO.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.DAO.AuthorDAO;
import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;

import java.util.Collections;
import java.util.List;

@Repository
public class AuthorDAOImpl extends CommonDAOImpl<Author, Long> implements AuthorDAO {

    public AuthorDAOImpl() {
        super(Author.class);
    }

    @Override
    public List<Author> getAllAuthorByName(String fullName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Author> query = session
                    .createQuery("FROM Author WHERE fullName LIKE :gotName", Author.class)
                    .setParameter("gotName", likeExpr(fullName));
            return !query.getResultList().isEmpty() ? query.getResultList() : Collections.emptyList();
        }
    }

    @Override
    public List<Book> getAllBookByAuthorId(Long authorId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session
                    .createQuery("SELECT ba.book FROM BookAuthor ba " +
                            "WHERE ba.author.id = :gotAuthorId", Book.class)
                    .setParameter("gotAuthorId", authorId);
            return !query.getResultList().isEmpty() ? query.getResultList() : Collections.emptyList();
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}