// ========================================
// RESET PASSWORD
// ========================================


// ========================================
// GET ELEMENTS
// ========================================

const resetPasswordForm =
    document.getElementById("resetPasswordForm");

const emailInput =
    document.getElementById("email");

const otpInput =
    document.getElementById("otp");

const newPasswordInput =
    document.getElementById("newPassword");

const confirmPasswordInput =
    document.getElementById("confirmPassword");

const errorMessage =
    document.getElementById("resetPasswordError");

const successMessage =
    document.getElementById("resetPasswordSuccess");

const resetPasswordBtn =
    document.getElementById("resetPasswordBtn");


// ========================================
// LOAD EMAIL
// ========================================

const savedEmail =
    sessionStorage.getItem("resetEmail");


if (savedEmail) {

    emailInput.value =
        savedEmail;

}


// ========================================
// FORM SUBMIT
// ========================================

resetPasswordForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        // ========================================
        // CLEAR MESSAGES
        // ========================================

        errorMessage.textContent = "";
        errorMessage.style.display = "none";

        successMessage.textContent = "";
        successMessage.style.display = "none";


        // ========================================
        // GET VALUES
        // ========================================

        const email =
            emailInput.value.trim();

        const otp =
            otpInput.value.trim();

        const newPassword =
            newPasswordInput.value;

        const confirmPassword =
            confirmPasswordInput.value;


        // ========================================
        // VALIDATE EMAIL
        // ========================================

        if (!email) {

            showError(
                "Please enter your email address."
            );

            return;

        }


        // ========================================
        // VALIDATE OTP
        // ========================================

        if (!otp) {

            showError(
                "Please enter the OTP."
            );

            return;

        }


        if (!/^\d{6}$/.test(otp)) {

            showError(
                "OTP must contain 6 digits."
            );

            return;

        }


        // ========================================
        // VALIDATE PASSWORD
        // ========================================

        if (!newPassword) {

            showError(
                "Please enter a new password."
            );

            return;

        }


        if (newPassword.length < 6) {

            showError(
                "Password must be at least 6 characters."
            );

            return;

        }


        // ========================================
        // CONFIRM PASSWORD
        // ========================================

        if (
            newPassword !==
            confirmPassword
        ) {

            showError(
                "Passwords do not match."
            );

            return;

        }


        // ========================================
        // DISABLE BUTTON
        // ========================================

        resetPasswordBtn.disabled = true;

        resetPasswordBtn.textContent =
            "Resetting Password...";


        try {

            // ========================================
            // API REQUEST
            // ========================================

            const response =
                await fetch(
                    "/api/auth/reset-password",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            email: email,

                            otp: otp,

                            newPassword:
                            newPassword

                        })

                    }
                );


            // ========================================
            // HANDLE ERROR
            // ========================================

            if (!response.ok) {

                let message =
                    "Unable to reset password.";

                try {

                    const error =
                        await response.json();

                    message =
                        error.message ||
                        message;

                } catch (e) {

                    console.error(
                        "Could not read error response.",
                        e
                    );

                }

                throw new Error(message);

            }


            // ========================================
            // SUCCESS
            // ========================================

            const message =
                await response.text();


            successMessage.textContent =
                message;

            successMessage.style.display =
                "block";


            // Remove saved reset email

            sessionStorage.removeItem(
                "resetEmail"
            );


            // ========================================
            // REDIRECT TO LOGIN
            // ========================================

            setTimeout(
                function () {

                    window.location.href =
                        "login.html";

                },
                1500
            );


        } catch (error) {

            console.error(
                "Reset password error:",
                error
            );


            showError(
                error.message ||
                "Unable to reset password."
            );


            resetPasswordBtn.disabled =
                false;

            resetPasswordBtn.textContent =
                "Reset Password";

        }

    }
);


// ========================================
// SHOW ERROR
// ========================================

function showError(message) {

    errorMessage.textContent =
        message;

    errorMessage.style.display =
        "block";

}