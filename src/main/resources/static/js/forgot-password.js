// ========================================
// FORGOT PASSWORD
// ========================================

const forgotPasswordForm =
    document.getElementById("forgotPasswordForm");

const emailInput =
    document.getElementById("email");

const errorMessage =
    document.getElementById("forgotPasswordError");

const successMessage =
    document.getElementById("forgotPasswordSuccess");

const sendOtpBtn =
    document.getElementById("sendOtpBtn");


// ========================================
// FORM SUBMIT
// ========================================

forgotPasswordForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        // Clear old messages

        errorMessage.textContent = "";
        errorMessage.style.display = "none";

        successMessage.textContent = "";
        successMessage.style.display = "none";


        const email =
            emailInput.value.trim();


        // ========================================
        // VALIDATE EMAIL
        // ========================================

        if (!email) {

            errorMessage.textContent =
                "Please enter your email address.";

            errorMessage.style.display =
                "block";

            return;

        }


        // ========================================
        // DISABLE BUTTON
        // ========================================

        sendOtpBtn.disabled = true;

        sendOtpBtn.textContent =
            "Sending OTP...";


        try {

            // ========================================
            // SEND OTP
            // ========================================

            const response =
                await fetch(
                    `/api/auth/forgot-password?email=${encodeURIComponent(email)}`,
                    {
                        method: "POST"
                    }
                );


            // ========================================
            // CHECK RESPONSE
            // ========================================

            if (!response.ok) {

                let message =
                    "Unable to send OTP.";

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


            // Save email for reset page

            sessionStorage.setItem(
                "resetEmail",
                email
            );


            // ========================================
            // REDIRECT
            // ========================================

            setTimeout(
                function () {

                    window.location.href =
                        "reset-password.html";

                },
                1000
            );


        } catch (error) {

            console.error(
                "Forgot password error:",
                error
            );


            errorMessage.textContent =
                error.message ||
                "Unable to send OTP.";

            errorMessage.style.display =
                "block";


            sendOtpBtn.disabled = false;

            sendOtpBtn.textContent =
                "Send OTP";

        }

    }
);