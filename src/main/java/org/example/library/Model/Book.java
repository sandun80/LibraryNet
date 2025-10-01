package org.example.library.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private int bookId;
    private String bookName;
    private String author;
    private String isbn;
    private String description;
    @Lob
    private byte[] image;
    @Lob
    private byte[] bookPdf;
}
