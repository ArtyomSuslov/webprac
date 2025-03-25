package ru.msu.cmc.webprac.DAO;

import ru.msu.cmc.webprac.entities.BookCopy;
import ru.msu.cmc.webprac.entities.BookStatus;

import java.util.List;

public interface BookCopyDAO extends CommonDAO<BookCopy, Long> {

    // Getting all copies by the book id
    List<BookCopy> getAllBookCopyByBookId(Long bookId);

    // Getting all copies by the status
    List<BookCopy> getAllBookCopyByStatus(BookStatus status);
}
