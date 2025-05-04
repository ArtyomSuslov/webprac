package ru.msu.cmc.webprac.controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.msu.cmc.webprac.DAO.BookCopyDAO;
import ru.msu.cmc.webprac.DAO.BorrowingDAO;
import ru.msu.cmc.webprac.DAO.ReaderDAO;
import ru.msu.cmc.webprac.entities.BookCopy;
import ru.msu.cmc.webprac.entities.BookStatus;
import ru.msu.cmc.webprac.entities.Borrowing;
import ru.msu.cmc.webprac.entities.Reader;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingDAO borrowingDAO;

    @Autowired
    private BookCopyDAO bookCopyDAO;

    @Autowired
    private ReaderDAO readerDAO;

    @GetMapping("/borrow")
    public String showBorrowForm(Model model) {

        model.addAttribute("currentPage", "borrow");
        model.addAttribute("borrowing", new Borrowing());
        model.addAttribute("bookCopies", bookCopyDAO.getAllBookCopyByStatus(BookStatus.available));

        return "borrowings/borrow-form";
    }

    @PostMapping("/borrow")
    public String borrowBook(@RequestParam String libraryCardNum,
                             @RequestParam Long copyId,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate borrowDate,
                             Model model) {

        Reader reader = readerDAO.getSingleReaderByLibraryCardNum(libraryCardNum);
        BookCopy copy = bookCopyDAO.getById(copyId);

        if (reader != null && copy != null && copy.getStatus() == BookStatus.available) {
            Borrowing borrowing = new Borrowing();
            borrowing.setReader(reader);
            borrowing.setBookCopy(copy);
            borrowing.setBorrowDate(borrowDate);

            copy.setStatus(BookStatus.borrowed);
            bookCopyDAO.update(copy);
            borrowingDAO.save(borrowing);

            model.addAttribute("message", "Книга с ID экземпляра " + copy.getId() + " была успешно выдана.");
        } else {
            model.addAttribute("error", "Ошибка при выдаче книги. Проверьте данные.");
        }

        return "borrowings/borrow-form";
    }

    @GetMapping("/return/search")
    public String searchBorrowings(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String query,
            Model model) {

        List<Borrowing> borrowings;

        if ("reader".equals(searchType) && query != null && !query.isBlank()) {
            Reader reader = readerDAO.getSingleReaderByLibraryCardNum(query);
            if (reader != null) {
                borrowings = readerDAO.getAllBorrowingByReaderFullName(reader.getFullName());
            } else {
                borrowings = Collections.emptyList();
            }
        } else if ("copy".equals(searchType) && query != null && !query.isBlank()) {
            Long copyId = Long.parseLong(query);
            borrowings = borrowingDAO.getAllBorrowingByBookCopyId(copyId);
        } else {
            borrowings = borrowingDAO.getAll().stream().toList();
        }

        borrowings = borrowings.stream()
                .filter(borrowing -> borrowing.getReturnDate() == null)
                .collect(Collectors.toList());

        model.addAttribute("borrowings", borrowings);
        model.addAttribute("currentPage", "return");
        return "borrowings/return-form";
    }

    @GetMapping("/return")
    public String showReturnPage(Model model) {

        model.addAttribute("borrowings", borrowingDAO.getAll());
        model.addAttribute("currentPage", "return");

        return "borrowings/return-form";
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam Long borrowingId,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        Borrowing borrowing = borrowingDAO.getById(borrowingId);

        if (borrowing != null && borrowing.getReturnDate() == null) {
            borrowing.setReturnDate(LocalDate.now());
            borrowing.getBookCopy().setStatus(BookStatus.available);
            bookCopyDAO.update(borrowing.getBookCopy());
            borrowingDAO.update(borrowing);

            redirectAttributes.addFlashAttribute("message", "Книга с ID экземпляра " + borrowing.getId() + " была возвращена.");
        }

        model.addAttribute("currentPage", "return");
        return "redirect:/readers/" + borrowing.getReader().getId();
    }

    @Getter
    public static class BorrowingReportEntry {
        private final Borrowing borrowing;
        private final long daysHeld;
        private final boolean overdue;
        private final String borrowDateFormatted;
        private final String returnDateFormatted;

        public BorrowingReportEntry(Borrowing borrowing) {
            this.borrowing = borrowing;
            LocalDate borrowDate = borrowing.getBorrowDate();
            LocalDate returnDate = borrowing.getReturnDate() != null ? borrowing.getReturnDate() : LocalDate.now();
            this.daysHeld = java.time.temporal.ChronoUnit.DAYS.between(borrowDate, returnDate);
            this.overdue = borrowing.getReturnDate() == null && daysHeld > 30;
            this.borrowDateFormatted = borrowDate.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            this.returnDateFormatted = borrowing.getReturnDate() != null
                    ? borrowing.getReturnDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    : "Не возвращено";
        }
    }

    @GetMapping("/reports")
    public String getReport(
            @RequestParam(name = "reportType", required = false, defaultValue = "ALL") String reportType,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<Borrowing> borrowings = Collections.emptyList();

        switch (reportType) {
            case "NOT_RETURNED" -> {
                List<Borrowing> allNotReturned = borrowingDAO.getAllBorrowingWithoutReturnDate();
                borrowings = allNotReturned.stream()
                        .filter(b -> (startDate == null || !b.getBorrowDate().isBefore(startDate)) &&
                                (endDate == null || !b.getBorrowDate().isAfter(endDate)))
                        .toList();
            }
            case "OVERDUE" -> borrowings = borrowingDAO.getOverdueBorrowings();
            case "ALL" -> borrowings = borrowingDAO.getBorrowingsBetweenDates(startDate, endDate);
        }

        List<BorrowingReportEntry> reportEntries = borrowings.stream()
                .map(BorrowingReportEntry::new)
                .toList();

        model.addAttribute("reportEntries", reportEntries);
        model.addAttribute("reportType", reportType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "borrowings/reports";
    }
}


