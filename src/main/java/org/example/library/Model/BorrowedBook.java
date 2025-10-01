package org.example.library.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedBook {
    @Id
    private int id;
    private String bookName;
    @Lob
    private byte[] pdf;
    private String email;
}
