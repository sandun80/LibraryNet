package org.example.library.Repository;

import org.example.library.Model.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepo extends JpaRepository<BorrowedBook, Integer> {

    @Query("SELECT b FROM BorrowedBook b WHERE b.email = ?1")
    List<BorrowedBook> findByEmail(String email);
}
