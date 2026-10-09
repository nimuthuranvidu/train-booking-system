const registerForm =
    document.getElementById("registerForm");

const registerBtn =
    document.getElementById("registerBtn");

const registerError =
    document.getElementById("registerError");

const registerSuccess =
    document.getElementById("registerSuccess");


registerForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        registerError.style.display = "none";
        registerSuccess.style.display = "none";


        const name =
            document.getElementById("name")
                .value
                .trim();

        const email =
            document.getElementById("email")
                .value
                .trim();

        const password =
            document.getElementById("password")
                .value;


        // ===============================
        // VALIDATION
        // ===============================

        if (!name || !email || !password) {

            showError(
                "Please fill in all fields."
            );

            return;
        }


        if (password.length < 6) {

            showError(
                "Password must contain at least 6 characters."
            );

            return;
        }


        // ===============================
        // BUTTON
        // ===============================

        registerBtn.disabled = true;

        registerBtn.textContent =
            "Creating Account...";


        try {

            // ===============================
            // REGISTER API
            // ===============================

            const response =
                await fetch(
                    "/api/auth/register",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            name: name,

                            email: email,

                            password: password

                        })
                    }
                );


            // IMPORTANT:
            // Backend returns plain text,
            // NOT JSON.

            const message =
                await response.text();


            // ===============================
            // ERROR
            // ===============================

            if (!response.ok) {

                throw new Error(
                    message ||
                    "Registration failed."
                );
            }


            // ===============================
            // SUCCESS
            // ===============================

            registerSuccess.textContent =
                message ||
                "OTP sent successfully.";

            registerSuccess.style.display =
                "block";


            // Save email for OTP page
            sessionStorage.setItem(
                "registrationEmail",
                email
            );


            // ===============================
            // GO TO OTP PAGE
            // ===============================

            setTimeout(
                () => {

                    window.location.href =
                        "verify-otp.html";

                },
                800
            );


        } catch (error) {

            console.error(
                "Registration error:",
                error
            );


            showError(
                error.message ||
                "Registration failed."
            );

        } finally {

            registerBtn.disabled = false;

            registerBtn.textContent =
                "Create Account";

        }

    }
);


// ===============================
// SHOW ERROR
// ===============================

function showError(message) {

    registerError.textContent =
        message;

    registerError.style.display =
        "block";

}