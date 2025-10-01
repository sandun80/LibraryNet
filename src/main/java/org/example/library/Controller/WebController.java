package org.example.library.Controller;

import org.example.library.Model.*;
import org.example.library.Service.JwtService;
import org.example.library.Service.UserPrincipals;
import org.example.library.Service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class WebController {
    @Autowired
    private WebService service;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/addbook")
    public ResponseEntity<?> addBooks(@RequestPart Book book, @RequestPart MultipartFile image, @RequestPart MultipartFile pdf) {
        try{
            return new ResponseEntity<>(service.saveBook(book,image,pdf), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/showbook")
    public ResponseEntity<?> getBooks(){
        List<Book> books = service.getBooks();
        if (books != null) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/userbooks")
    public ResponseEntity<?> getUserBooks(@RequestHeader("Authorization") String token){
        String authToken = token.substring(7);
        String email = jwtService.extractUserName(authToken);

        if (jwtService.validateToken(authToken,email)){

            List<Book> books = service.getUsersBooks(email);

            if (books != null) {
                return new ResponseEntity<>(books, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getbook/{name}")
    public ResponseEntity<?> getBook(@PathVariable String name){
        Book book = service.getBookByName(name);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerBook(@RequestPart Users user, @RequestPart MultipartFile dp){
        try{
           // Object file = service.registerUser(user,dp);
//            if (file != null) {
//                return new ResponseEntity<>(file, HttpStatus.CREATED);
//            }

            Users users = service.checkUser(user.getEmail());
            if (users == null) {
                user.setPassword(encoder.encode(user.getPassword()));
                return new ResponseEntity<>(service.registerUser(user,dp),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }


        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> loginUser(@RequestBody LoginUser LogUser){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        LogUser.getEmail(),
                        LogUser.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            UserPrincipals principal = (UserPrincipals) authentication.getPrincipal();
            Users user = principal.getUser();
            String token = jwtService.generateToken(user.getEmail());
            LoginDTO dto = new LoginDTO(token, user.getEmail(), user.getProfilePicture());

            return new ResponseEntity<>(dto,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody BorrowedBook book,
                                        @RequestHeader("Authorization") String tokenHeader) {
        try {
            // 1. Remove "Bearer " prefix
            String token = tokenHeader.substring(7);

            // 2. Extract email (username) from token
            String email = jwtService.extractUserName(token);

            // 3. Validate the token
            if (!jwtService.validateToken(token, email)) {
                return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            // 4. Attach the user email to the book
            book.setEmail(email);

            // 5. Save borrowed data
            BorrowedBook savedBook = service.saveBorrowedData(book);

            return new ResponseEntity<>(savedBook, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error processing request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mybooks")
    public ResponseEntity<?> getMyBooks(@RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String email = jwtService.extractUserName(token);
        if (jwtService.validateToken(token, email)) {
            List<BorrowedBook> borrowedBooks = service.getBorrowedBooks(email);

            if (borrowedBooks!=null){
                return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<byte[]> findTheBook(@RequestHeader("Authorization") String header, @PathVariable String id){
        String token = header.substring(7);
        String email = jwtService.extractUserName(token);
        byte[] pdf = service.getBookPdf(email,id);

        if (pdf != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/markread/{id}")
    public ResponseEntity<Integer> markAsRead(@RequestHeader("Authorization") String header,@PathVariable String id){
        String token = header.substring(7);
        String email = jwtService.extractUserName(token);
        return new ResponseEntity<>(service.markBookAsRead(email,id),HttpStatus.OK);
    }
}
