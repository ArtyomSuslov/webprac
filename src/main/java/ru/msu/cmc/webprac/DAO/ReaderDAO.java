package ru.msu.cmc.webprac.DAO;

import ru.msu.cmc.webprac.entities.Borrowing;
import ru.msu.cmc.webprac.entities.Reader;

import java.util.List;

public interface ReaderDAO extends CommonDAO<Reader, Long> {

    // Getting all readers by their full name (in case there are multiple)
    List<Reader> getAllReaderByName(String fullName);

    // Getting reader by his library card number
    Reader getSingleReaderByLibraryCardNum(String libraryCardNum);

    // Getting reader by his email adders
    Reader getSingleReaderByAddress(String address);

    // Getting reader by his phone number
    Reader getSingleReaderByPhone(String phoneNumber);

    // Getting all the books reader has taken
    List<Borrowing> getAllBorrowingByName(String fullName);
}
