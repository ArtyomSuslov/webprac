package ru.msu.cmc.webprac.DAO;

import lombok.Builder;
import lombok.Getter;

import ru.msu.cmc.webprac.entities.Author;
import ru.msu.cmc.webprac.entities.Book;
import ru.msu.cmc.webprac.entities.BookCopy;
import ru.msu.cmc.webprac.entities.BookStatus;

import java.util.List;

public interface BookDAO extends CommonDAO<Book, Long> {

    // Getting a book by its title
    Book getSingleBookByTitle(String title);

    // Getting all books from a certain year
    List<Book> getAllBookFromYear(Long year);

    // Getting a book by its isbn
    Book getSingleBookByIsbn(String isbn);

    // Getting all authors of a book via book's id
    List<Author> getAllAuthorByBookId(Long bookId);

    // Getting all available copies of the book via its book is
    List<BookCopy> getAllAvailableBookCopyByBookId(Long bookId);

    List<Book> getAllBookByFilter(Filter filter);

    @Builder
    @Getter
    class Filter {
        private Long authorId;
        private String publisher;
        private Long year;
        private BookStatus status;
    }
}
