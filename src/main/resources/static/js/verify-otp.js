const otpForm =
    document.getElementById("otpForm");

const otpInput =
    document.getElementById("otp");

const otpEmail =
    document.getElementById("otpEmail");

const verifyBtn =
    document.getElementById("verifyBtn");

const otpError =
    document.getElementById("otpError");

const otpSuccess =
    document.getElementById("otpSuccess");


// =========================================
// GET EMAIL FROM REGISTRATION
// =========================================

const email =
    sessionStorage.getItem(
        "registrationEmail"
    );


if (!email) {

    showError(
        "Registration information was not found. Please register again."
    );

    verifyBtn.disabled = true;

} else {

    otpEmail.textContent =
        `OTP sent to: ${email}`;

}


// =========================================
// OTP INPUT
// =========================================

otpInput.addEventListener(
    "input",
    function () {

        // Allow numbers only

        this.value =
            this.value
                .replace(/\D/g, "")
                .slice(0, 6);

    }
);


// =========================================
// VERIFY OTP
// =========================================

otpForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        otpError.style.display =
            "none";

        otpSuccess.style.display =
            "none";


        const otp =
            otpInput.value.trim();


        // =========================================
        // VALIDATION
        // =========================================

        if (!email) {

            showError(
                "Email information is missing."
            );

            return;
        }


        if (otp.length !== 6) {

            showError(
                "Please enter the 6-digit OTP."
            );

            return;
        }


        // =========================================
        // BUTTON
        // =========================================

        verifyBtn.disabled =
            true;

        verifyBtn.textContent =
            "Verifying...";


        try {

            // =========================================
            // VERIFY OTP API
            // =========================================

            const response =
                await fetch(
                    "/api/auth/verify-otp",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            email: email,

                            otp: otp

                        })
                    }
                );


            // Your backend returns plain text

            const message =
                await response.text();


            // =========================================
            // ERROR
            // =========================================

            if (!response.ok) {

                throw new Error(
                    message ||
                    "OTP verification failed."
                );

            }


            // =========================================
            // SUCCESS
            // =========================================

            otpSuccess.textContent =
                message ||
                "Account verified successfully.";

            otpSuccess.style.display =
                "block";


            // Remove temporary registration data

            sessionStorage.removeItem(
                "registrationEmail"
            );


            // =========================================
            // GO TO LOGIN
            // =========================================

            setTimeout(
                () => {

                    window.location.href =
                        "login.html";

                },
                1200
            );


        } catch (error) {

            console.error(
                "OTP verification error:",
                error
            );


            showError(
                error.message ||
                "OTP verification failed."
            );


        } finally {

            verifyBtn.disabled =
                false;

            verifyBtn.textContent =
                "Verify OTP";

        }

    }
);


// =========================================
// SHOW ERROR
// =========================================

function showError(message) {

    otpError.textContent =
        message;

    otpError.style.display =
        "block";

}