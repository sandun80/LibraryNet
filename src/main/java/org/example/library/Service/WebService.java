package org.example.library.Service;

import org.example.library.Model.Book;
import org.example.library.Model.BorrowedBook;
import org.example.library.Model.Users;
import org.example.library.Repository.BookRepo;
import org.example.library.Repository.BorrowRepo;
import org.example.library.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class WebService {
    @Autowired
    private BookRepo repo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BorrowRepo repo3;


    public Object saveBook(Book book, MultipartFile image, MultipartFile pdf) throws IOException {
        book.setBookName(book.getBookName().toLowerCase());
        book.setBookPdf(pdf.getBytes());
        book.setImage(image.getBytes());
        return repo.save(book);
    }

    public List<Book> getBooks() {
        return repo.findAll();
    }

    public Book getBookByName(String name) {
        return repo.getBookByBookName(name);
    }

    public Object registerUser(Users user, MultipartFile dp) throws IOException {
        user.setProfilePicture(dp.getBytes());
        return userRepo.save(user);
    }

    public Users checkUser(String email) {
        return userRepo.findByEmail(email);
    }

    public BorrowedBook saveBorrowedData(BorrowedBook book) {
        return repo3.save(book);
    }

    public List<Book> getUsersBooks(String email) {
        List<Book> avaBooks = repo.findAll();
        List<BorrowedBook> borrowedBooks = repo3.findByEmail(email);

        avaBooks.removeIf(book -> borrowedBooks.stream()
                .anyMatch(borrowb -> book.getBookName().equals(borrowb.getBookName()))
        );

        return avaBooks;


    }

    public List<BorrowedBook> getBorrowedBooks(String email) {
        return repo3.findByEmail(email);
    }

    public byte[] getBookPdf(String email, String id) {
        return repo3.getPdf(email,id);
    }

    public int markBookAsRead(String email, String id) {
        return repo3.deleteBookByDetails(email,id);
    }

    public void changePassword(String password, String email) {
        userRepo.updatePassword(password,email);
    }
}
