package org.example.library.Repository;

import org.example.library.Model.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BorrowRepo extends JpaRepository<BorrowedBook, Integer> {

    @Query("SELECT b FROM BorrowedBook b WHERE b.email = ?1")
    List<BorrowedBook> findByEmail(String email);

    @Query("SELECT b.pdf FROM BorrowedBook b WHERE b.email=?1 AND b.id=?2")
    byte[] getPdf(String email, String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM BorrowedBook b WHERE b.email=?1 AND b.id=?2")
    int deleteBookByDetails(String email, String id);
}
