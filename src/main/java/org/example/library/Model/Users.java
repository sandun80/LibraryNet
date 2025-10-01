package org.example.library.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Lob
    private byte[] profilePicture;
    private String name;
    private String email;
    private int age;
    private String password;
}
