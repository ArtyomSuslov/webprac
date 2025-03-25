package ru.msu.cmc.webprac.DAO;

import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;

import java.util.List;

public interface BookDAO extends CommonDAO<Book, Long> {

    // Getting a book by its title
    Book getSingleBookByTitle(String title);

    // Getting all the books by their publisher
    List<Book> getAllBookByPublisher(String publisher);

    // Getting all books from a certain year
    List<Book> getAllBookFromYear(Long year);

    // Getting a book by its isbn
    Book getSingleBookByIsbn(String isbn);

    // Getting all authors of a book via book's id
    List<Author> getAllAuthorByBookId(Long bookId);
}
