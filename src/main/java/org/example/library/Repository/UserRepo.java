package org.example.library.Repository;

import org.example.library.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {


    Users findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.password=?1 WHERE u.email=?2")
    void updatePassword(String password, String email);
}
