document.addEventListener("DOMContentLoaded", displaybooks);

async function displaybooks() {
    
    try{
        const reponse = await fetch("http://localhost:8080/api/showbook", {
            method : "GET",
            headers : {"Content-Type" : "application/json"},
        });

        if(reponse.ok){
            const booklist = await reponse.json();
            renderBooks(booklist);
        }
        
    }catch(e){
        alert(e);
    }
}

function renderBooks(bookList) {
    const container = document.getElementById("book-container");
    container.innerHTML = "";

    for (let book of bookList) {
        const card = document.createElement("div");
        card.className = "book-card";

        // Convert image byte[] (Base64) to usable image URL
        let imageSrc = book.image ? `data:image/jpeg;base64,${book.image}` : "default-cover.jpg";

        card.innerHTML = `
            <img src="${imageSrc}" alt="${book.bookName}" class="book-image">
            <h3>${book.bookName}</h3>
            <p class="author">${book.author}</p>
            <button class="borrow">Borrow</button>
        `;

        container.appendChild(card);
    }
}

const srhbtn = document.getElementById("srhbtn");
srhbtn.addEventListener("click", extractBook);

function extractBook(){
    const searchInput = document.getElementById("searchInput").value
    
    searchBook(searchInput);
}

async function searchBook(bookName) {
    try {
        const response = await fetch(`http://localhost:8080/api/getbook/${bookName}`, {
            method: "GET",
            headers: { "Content-Type": "application/json" }
        });

        if (response.ok) {
            const filteredBooks = await response.json();

            // If no books found, display a message
            if (filteredBooks.length === 0) {
                alert("fefef")
            } else {
                renderBooks(filteredBooks);
            }
        } else {
            alert("Failed to fetch books. Status: " + response.status);
        }
    } catch (e) {
        alert("Error while fetching books: " + e.message);
    }
}

