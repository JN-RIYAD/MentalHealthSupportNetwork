//// scripts.js
//    function toggleCertificatesField() {
//      var userType = document.getElementById("userType").value;
//      var certificatesField = document.getElementById("certificatesField");
//      if (userType === "Therapist") {
//        certificatesField.style.display = "block";
//      } else {
//        certificatesField.style.display = "none";
//      }
//    }

    $(document).ready(function () {
        // Add an event listener to the form on submission
        $("form").submit(function (event) {
            // Get the values of the password and confirmPassword fields
            var password = $("#password").val();
            var confirmPassword = $("#confirmPassword").val();

            // Check if the passwords match and are at least 6 characters long
            if (password !== confirmPassword) {
                // Display an error message
                $("#passwordError").text("Passwords do not match");
                event.preventDefault(); // Prevent form submission
            } else if (password.length < 6) {
                // Display an error message
                $("#passwordError").text("Password must be at least 6 characters");
                event.preventDefault(); // Prevent form submission
            } else {
                // Clear any previous error messages
                $("#passwordError").text("");
            }
        });
    });