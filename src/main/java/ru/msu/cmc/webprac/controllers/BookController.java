package ru.msu.cmc.webprac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.msu.cmc.webprac.DAO.*;
import ru.msu.cmc.webprac.entities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BookCopyDAO bookCopyDAO;

    @Autowired
    private BorrowingDAO borrowingDAO;

    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private BookAuthorDAO bookAuthorDAO;

    @GetMapping
    public String listBooks(Model model,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String isbn,
                            @RequestParam(required = false) String authorFullName,
                            @RequestParam(required = false) String publisher,
                            @RequestParam(required = false) Long year,
                            @RequestParam(required = false) BookStatus status) {

        List<Author> authorList = authorDAO.getAllAuthorByName(authorFullName);
        Long authorId = null;
        if (authorList.size() == 1) {
            authorId = authorList.get(0).getId();
        }

        BookDAO.Filter filter = BookDAO.Filter.builder()
                .authorId(authorId != null && authorId != 0 ? authorId : null)
                .publisher(publisher != null && !publisher.isEmpty() ? publisher : null)
                .year(year != null && year != 0 ? year : null)
                .status(status != null ? status : null)
                .build();

        List<Book> books;
        if (title != null && !title.isEmpty()) {
            Book book = bookDAO.getSingleBookByTitle(title);
            books = book != null ? Collections.singletonList(book) : Collections.emptyList();
        } else if (isbn != null && !isbn.isEmpty()) {
            Book book = bookDAO.getSingleBookByIsbn(isbn);
            books = book != null ? Collections.singletonList(book) : Collections.emptyList();
        } else {
            books = new ArrayList<>(bookDAO.getAllBookByFilter(filter));
        }

        for (Book book : books) {
            if (book.getBookAuthorList() == null) {
                book.setBookAuthorList(Collections.emptyList());
            }
        }

        model.addAttribute("currentPage", "books");
        model.addAttribute("books", books);
        return "books/list";
    }


    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {

        Book book = bookDAO.getById(id);
        if (book == null) {
            return "redirect:/books";
        }

        List<Author> authors = bookDAO.getAllAuthorByBookId(id);
        List<BookCopy> allCopies = bookCopyDAO.getAllBookCopyByBookId(id);

        List<Borrowing> borrowingsList = new ArrayList<>(Collections.emptyList());
        for (BookCopy bookCopy : allCopies) {
            borrowingsList.addAll(borrowingDAO.getAllBorrowingByBookCopyId(bookCopy.getId()));
        }

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("allCopies", allCopies);
        model.addAttribute("borrowingList", borrowingsList);
        return "books/view";
    }

    @GetMapping("/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/form";
    }

    @PostMapping
    public String createBook(@ModelAttribute Book book,
                             @RequestParam("authorNames") String authorNames,
                             BindingResult result) {

        // Тут важно найти авторов либо их зарегистрировать и приписать их к книге
        String[] authorNameArray = authorNames.split(",");
        List<BookAuthor> bookAuthorList = new ArrayList<>();

        for (String name : authorNameArray) {
            name = name.trim();
            List<Author> tmpAuthorList = authorDAO.getAllAuthorByName(name);
            Author author = !tmpAuthorList.isEmpty() ? tmpAuthorList.get(0) : null;

            if (author == null) {
                author = new Author();
                author.setFullName(name);
                authorDAO.save(author);
            }

            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBook(book);
            bookAuthor.setAuthor(author);

            bookAuthorList.add(bookAuthor);
        }

        book.setBookAuthorList(bookAuthorList);

        if (result.hasErrors()) {
            return "books/form";
        }

        bookDAO.save(book);
        bookAuthorDAO.saveCollection(bookAuthorList);

        return "redirect:/books";
    }

    @PostMapping("/{id}/copies")
    public String addBookCopy(@PathVariable Long id) {

        Book book = bookDAO.getById(id);

        if (book != null) {
            BookCopy copy = new BookCopy();
            copy.setBook(book);
            copy.setStatus(BookStatus.available);
            bookCopyDAO.save(copy);
        }
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/copies/{copyId}")
    public String deleteBookCopy(@PathVariable Long copyId) {

        BookCopy copy = bookCopyDAO.getById(copyId);

        if (copy != null && copy.getStatus() == BookStatus.available) {
            bookCopyDAO.delete(copy);
            return "redirect:/books/" + copy.getBook().getId();
        }
        return "redirect:/books?error=cannotDelete";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {

        List<BookCopy> copies = bookCopyDAO.getAllBookCopyByBookId(id);
        boolean canDelete = copies.stream()
                .allMatch(copy -> copy.getStatus() == BookStatus.available);

        if (canDelete) {
            bookDAO.deleteById(id);
            return "redirect:/books";
        }
        return "redirect:/books/" + id + "?error=hasActiveBorrowings";
    }
}
