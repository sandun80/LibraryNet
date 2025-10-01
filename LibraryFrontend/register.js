const fileInput = document.getElementById("profilePic");
const registerbtn = document.getElementById("registerbtn");
registerbtn.addEventListener("click", getImage);

fileInput.addEventListener("change", function() {
  const file = fileInput.files[0];
  if (!file) return; // <-- Prevents error if no file selected

  // Preview the selected image
  const reader = new FileReader();
  reader.onload = function(e) {
    document.getElementById("avatarPreview").src = e.target.result;
  };
  reader.readAsDataURL(file);
});

// Example function to get image data later
function getImage() {
  const file = fileInput.files[0];
  if (!file) {
    console.error("No file selected");
    return null;
  }
  console.log(file);
  RegisterUser(file);
}

async function RegisterUser(file) {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const age = parseInt(document.getElementById("age").value);
    const password = document.getElementById("password").value;

    const user = {
        name: name,
        email: email,
        age: age,
        password: password
    };

    const formdata = new FormData();
    formdata.append("user", new Blob([JSON.stringify(user)], { type: "application/json" }));
    formdata.append("dp", file);

    try {
        const response = await fetch("http://localhost:8080/api/signup", {
            method: "POST",
            body: formdata
        });

        if (response.ok) {
            alert("Done");
        } else {
            alert("Not Done: " + (await response.text()));
        }
    } catch (e) {
        alert("Error: " + e.message);
    }
}
