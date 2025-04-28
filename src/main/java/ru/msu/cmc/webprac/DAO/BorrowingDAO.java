package ru.msu.cmc.webprac.DAO;

import ru.msu.cmc.webprac.entities.Borrowing;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingDAO extends CommonDAO<Borrowing, Long> {

    // Getting all borrowings by the reader's id
    List<Borrowing> getAllBorrowingByReaderId(Long readerId);

    // Getting all borrowings by the book copy's id
    List<Borrowing> getAllBorrowingByBookCopyId(Long bookCopyId);

    // Getting all borrowings without a return date
    List<Borrowing> getAllBorrowingWithoutReturnDate();

    // Getting all borrowings between dates
    List<Borrowing> getBorrowingsBetweenDates(LocalDate startDate, LocalDate endDate);

    // Getting overdue borrowings
    List<Borrowing> getOverdueBorrowings();
}
