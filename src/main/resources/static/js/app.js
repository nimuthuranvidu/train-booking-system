
const searchForm = document.getElementById("searchForm");

// ========================================
// TRAIN SEARCH
// ========================================

if (searchForm) {
    const swapBtn = document.getElementById("swapBtn");
    const fromStation = document.getElementById("from");
    const toStation = document.getElementById("to");
    const travelDate = document.getElementById("travelDate");

    // ========================================
    // SET MINIMUM TRAVEL DATE
    // ========================================

    if (travelDate) {
        const today = new Date();

        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, "0");
        const day = String(today.getDate()).padStart(2, "0");

        travelDate.min = `${year}-${month}-${day}`;
    }

    // ========================================
    // SWAP STATIONS
    // ========================================

    if (swapBtn) {
        swapBtn.addEventListener("click", () => {
            const temp = fromStation.value;

            fromStation.value = toStation.value;
            toStation.value = temp;
        });
    }

    // ========================================
    // SEARCH TRAINS
    // ========================================

    searchForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const from = Number(fromStation.value);
        const to = Number(toStation.value);
        const date = travelDate.value;

        const passengers = Number(
            document.getElementById("passengers").value
        );

        // ========================================
        // VALIDATION
        // ========================================

        if (!from) {
            alert("Please select a departure station.");
            return;
        }

        if (!to) {
            alert("Please select a destination station.");
            return;
        }

        if (from === to) {
            alert("Departure and destination stations cannot be the same.");
            return;
        }

        if (!date) {
            alert("Please select a travel date.");
            return;
        }

        if (passengers < 1) {
            alert("At least one passenger is required.");
            return;
        }

        // ========================================
        // CREATE REQUEST
        // ========================================

        const searchRequest = {
            sourceStationId: from,
            destinationStationId: to,
            travelDate: date,
            numberOfPassengers: passengers
        };

        console.log(
            "Sending train search request:",
            searchRequest
        );

        // ========================================
        // CLEAR OLD BOOKING DATA
        // ========================================

        sessionStorage.removeItem("selectedSeats");
        sessionStorage.removeItem("selectedSeatIds");
        sessionStorage.removeItem("selectedScheduleId");
        sessionStorage.removeItem("passengerDetails");
        sessionStorage.removeItem("confirmedBooking");
        sessionStorage.removeItem("bookingId");

        // ========================================
        // CALL BACKEND
        // ========================================

        try {
            const response = await fetch("/api/train-search", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(searchRequest)
            });

            // ========================================
            // CHECK RESPONSE
            // ========================================

            if (!response.ok) {
                let errorMessage = "Unable to search trains.";

                try {
                    const error = await response.json();

                    if (error.message) {
                        errorMessage = error.message;
                    }
                } catch (e) {
                    console.error(
                        "Could not read error response.",
                        e
                    );
                }

                throw new Error(errorMessage);
            }

            // ========================================
            // GET RESULTS
            // ========================================

            const results = await response.json();

            console.log(
                "Train search successful:",
                results
            );

            // ========================================
            // SAVE SEARCH DATA
            // ========================================

            sessionStorage.setItem(
                "trainSearch",
                JSON.stringify(searchRequest)
            );

            sessionStorage.setItem(
                "trainSearchResults",
                JSON.stringify(results)
            );

            // ========================================
            // OPEN RESULTS PAGE
            // ========================================

            window.location.href = "search-results.html";

        } catch (error) {
            console.error("Train search error:", error);

            alert(
                error.message ||
                "Something went wrong while searching for trains."
            );
        }
    });
}

// ========================================
// UPDATE NAVBAR
// ========================================

function updateNavbar() {
    const navButtons = document.querySelector(".nav-buttons");

    if (!navButtons) {
        return;
    }

    const token = localStorage.getItem("token");
    const userName = localStorage.getItem("userName");
    const userRole = localStorage.getItem("userRole");

    // ========================================
    // USER IS LOGGED IN
    // ========================================

    if (token) {
        navButtons.innerHTML = `
            <a
                href="profile.html"
                class="profile-btn"
            >
                👤 ${userName || "Profile"}
            </a>

            <button
                type="button"
                id="logoutBtn"
                class="logout-btn"
            >
                Logout
            </button>
        `;

        const logoutBtn = document.getElementById("logoutBtn");

        if (logoutBtn) {
            logoutBtn.addEventListener("click", () => {

                // Remove login data

                localStorage.removeItem("token");
                localStorage.removeItem("userId");
                localStorage.removeItem("userName");
                localStorage.removeItem("userEmail");
                localStorage.removeItem("userRole");

                // Clear temporary booking data

                sessionStorage.removeItem("selectedSeats");
                sessionStorage.removeItem("selectedSeatIds");
                sessionStorage.removeItem("selectedScheduleId");
                sessionStorage.removeItem("passengerDetails");
                sessionStorage.removeItem("confirmedBooking");
                sessionStorage.removeItem("bookingId");

                // Return home

                window.location.href = "index.html";
            });
        }

    }

        // ========================================
        // USER IS NOT LOGGED IN
    // ========================================

    else {
        navButtons.innerHTML = `
            <a
                href="login.html"
                class="login-btn"
            >
                Login
            </a>

            <a
                href="register.html"
                class="register-btn"
            >
                Register
            </a>

            <a
                href="/admin-login.html"
                id="adminLoginButton"
                style="
                    display: inline-flex;
                    align-items: center;
                    justify-content: center;
                    padding: 12px 16px;
                    background: #111c36;
                    color: white;
                    border: 2px solid #2563eb;
                    border-radius: 8px;
                    font-size: 14px;
                    font-weight: 700;
                    line-height: 1.2;
                    text-decoration: none;
                    white-space: nowrap;
                "
            >
                Admin Login
            </a>
        `;
    }
}

// ========================================
// RUN NAVBAR UPDATE
// ========================================

updateNavbar();
