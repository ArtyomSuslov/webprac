package ru.msu.cmc.webprac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.msu.cmc.webprac.DAO.BorrowingDAO;
import ru.msu.cmc.webprac.DAO.ReaderDAO;
import ru.msu.cmc.webprac.entities.Borrowing;
import ru.msu.cmc.webprac.entities.Reader;


import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/readers")
public class ReaderController {

    @Autowired
    private ReaderDAO readerDAO;

    @Autowired
    private BorrowingDAO borrowingDAO;

    @GetMapping
    public String listReaders(Model model,
                              @RequestParam(required = false) String fullName,
                              @RequestParam(required = false) String cardNumber,
                              @RequestParam(required = false) String address,
                              @RequestParam(required = false) String phoneNumber) {

        List<Reader> readers = new ArrayList<>();
        boolean searchPerformed = false;

        if (fullName != null && !fullName.isEmpty()) {
            readers = readerDAO.getAllReaderByName(fullName);
            searchPerformed = true;
        }
        if (cardNumber != null && !cardNumber.isEmpty()) {
            Reader reader = readerDAO.getSingleReaderByLibraryCardNum(cardNumber);
            readers = reader != null ? Collections.singletonList(reader) : Collections.emptyList();
            searchPerformed = true;
        }
        if (address != null && !address.isEmpty()) {
            Reader readerByAddress = readerDAO.getSingleReaderByAddress(address);
            readers = readerByAddress != null ? Collections.singletonList(readerByAddress) : Collections.emptyList();
            searchPerformed = true;
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            Reader readerByPhoneNumber = readerDAO.getSingleReaderByPhone(phoneNumber);
            readers = readerByPhoneNumber != null ? Collections.singletonList(readerByPhoneNumber) : Collections.emptyList();
            searchPerformed = true;
        }

        if (!searchPerformed) {
            readers = new ArrayList<>(readerDAO.getAll());
        }

        Map<Long, Integer> borrowingsCount = new HashMap<>();
        for (Reader reader : readers) {
            List<Borrowing> borrowings = readerDAO.getAllBorrowingByReaderFullName(reader.getFullName());
            borrowings = borrowings.stream()
                    .filter(borrowing -> borrowing.getReturnDate() == null)
                    .collect(Collectors.toList());
            int count = borrowings.size();
            borrowingsCount.put(reader.getId(), count);
        }

        model.addAttribute("currentPage", "readers");
        model.addAttribute("readers", readers);
        model.addAttribute("borrowingsCount", borrowingsCount);
        model.addAttribute("noResults", searchPerformed && readers.isEmpty());

        return "readers/list";
    }

    @GetMapping("/{id}")
    public String viewReader(@PathVariable Long id, Model model) {

        Reader reader = readerDAO.getById(id);
        if (reader == null) {
            return "redirect:/readers";
        }

        List<Borrowing> activeBorrowings = borrowingDAO.getAllBorrowingByReaderId(id).stream()
                .filter(b -> b.getReturnDate() == null)
                .collect(Collectors.toList());

        List<Borrowing> historyBorrowings = borrowingDAO.getAllBorrowingByReaderId(id).stream()
                .filter(b -> b.getReturnDate() != null)
                .collect(Collectors.toList());

        model.addAttribute("reader", reader);
        model.addAttribute("activeBorrowings", activeBorrowings);
        model.addAttribute("historyBorrowings", historyBorrowings);
        return "readers/view";
    }

    @GetMapping("/new")
    public String newReaderForm(Model model) {

        model.addAttribute("reader", new Reader());
        return "readers/form";
    }

    @PostMapping
    public String createReader(@ModelAttribute Reader reader, BindingResult result) {

        if (result.hasErrors()) {
            return "readers/form";
        }
        readerDAO.save(reader);
        return "redirect:/readers";
    }

    @PostMapping("/{id}/delete")
    public String deleteReader(@PathVariable Long id) {

        List<Borrowing> activeBorrowings = borrowingDAO.getAllBorrowingByReaderId(id).stream()
                .filter(b -> b.getReturnDate() == null)
                .collect(Collectors.toList());

        if (activeBorrowings.isEmpty()) {
            readerDAO.deleteById(id);
            return "redirect:/readers";
        }
        return "redirect:/readers/" + id + "?error=hasActiveBorrowings";
    }
}