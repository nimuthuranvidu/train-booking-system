// =========================================
// LOGIN
// =========================================

const loginForm =
    document.getElementById("loginForm");

const loginButton =
    document.getElementById("loginButton");

const loginError =
    document.getElementById("loginError");


loginForm.addEventListener(
    "submit",
    async (event) => {

        event.preventDefault();

        hideError();

        loginButton.disabled = true;

        loginButton.textContent =
            "Logging in...";


        const email =
            document
                .getElementById("email")
                .value
                .trim();

        const password =
            document
                .getElementById("password")
                .value;


        try {

            const response =
                await fetch(
                    "/api/auth/login",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            email: email,

                            password: password

                        })
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    data.message ||
                    "Invalid email or password."
                );

            }


            // =================================
            // SAVE JWT
            // =================================

            localStorage.setItem(
                "token",
                data.token
            );


            // Save user information too

            localStorage.setItem(
                "userId",
                data.userId
            );

            localStorage.setItem(
                "userName",
                data.name
            );

            localStorage.setItem(
                "userEmail",
                data.email
            );

            localStorage.setItem(
                "userRole",
                data.role
            );


            console.log(
                "Login successful"
            );

            console.log(
                "JWT saved"
            );


            // =================================
            // REDIRECT
            // =================================

            window.location.href =
                "index.html";


        } catch (error) {

            console.error(
                "Login error:",
                error
            );

            showError(
                error.message
            );

            loginButton.disabled =
                false;

            loginButton.textContent =
                "Login";

        }

    }
);


// =========================================
// ERROR
// =========================================

function showError(
    message
) {

    loginError.style.display =
        "block";

    loginError.textContent =
        message;

}


function hideError() {

    loginError.style.display =
        "none";

}