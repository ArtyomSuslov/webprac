package ru.msu.cmc.webprac.DAO.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprac.DAO.BookAuthorDAO;
import ru.msu.cmc.webprac.entities.BookAuthor;

@Repository
public class BookAuthorDAOImpl extends CommonDAOImpl<BookAuthor, Long> implements BookAuthorDAO {

    public BookAuthorDAOImpl() {
        super(BookAuthor.class);
    }

}