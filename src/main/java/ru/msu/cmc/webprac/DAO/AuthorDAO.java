package ru.msu.cmc.webprac.DAO;

import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;

import java.util.List;

public interface AuthorDAO extends CommonDAO<Author, Long> {

    // Getting all authors by their full name (in case there are multiple)
    List<Author> getAllAuthorByName(String fullName);

    // Getting all the books from the author via author_id
    List<Book> getAllBookByAuthorId(Long authorId);
}
