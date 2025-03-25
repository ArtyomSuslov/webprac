package ru.msu.cmc.webprac.DAO;

import ru.msu.cmc.webprac.entities.Borrowing;

import java.util.List;

public interface BorrowingDAO extends CommonDAO<Borrowing, Long> {

    // Getting all borrowings by the reader's id
    List<Borrowing> getAllBorrowingByReaderId(Long readerId);

    // Getting all borrowings by the book copy's id
    List<Borrowing> getAllBorrowingByBookCopyId(Long bookCopyId);

    // Getting all borrowings without return date
    List<Borrowing> getAllBorrowingWithoutReturnDate();
}
