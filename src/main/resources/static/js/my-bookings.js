// ========================================
// MY BOOKINGS
// ========================================


const bookingLoading =
    document.getElementById(
        "bookingLoading"
    );

const bookingError =
    document.getElementById(
        "bookingError"
    );

const noBookings =
    document.getElementById(
        "noBookings"
    );

const bookingsContainer =
    document.getElementById(
        "bookingsContainer"
    );


// ========================================
// CHECK LOGIN
// ========================================

const token =
    localStorage.getItem("token");


if (!token) {

    window.location.href =
        "login.html";

}


// ========================================
// LOAD BOOKINGS
// ========================================

async function loadMyBookings() {

    try {

        bookingLoading.style.display =
            "block";

        bookingError.style.display =
            "none";

        noBookings.style.display =
            "none";


        const response =
            await fetch(
                "/api/bookings/my",
                {
                    method: "GET",

                    headers: {

                        "Authorization":
                            `Bearer ${token}`,

                        "Content-Type":
                            "application/json"

                    }

                }
            );


        // ========================================
        // CHECK RESPONSE
        // ========================================

        if (!response.ok) {

            let message =
                "Unable to load your bookings.";


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


            throw new Error(
                message
            );

        }


        // ========================================
        // GET BOOKINGS
        // ========================================

        const bookings =
            await response.json();


        console.log(
            "My bookings:",
            bookings
        );


        bookingLoading.style.display =
            "none";


        // ========================================
        // NO BOOKINGS
        // ========================================

        if (
            !bookings ||
            bookings.length === 0
        ) {

            noBookings.style.display =
                "block";

            return;

        }


        // ========================================
        // DISPLAY BOOKINGS
        // ========================================

        bookingsContainer.innerHTML =
            "";


        bookings.forEach(
            booking => {

                bookingsContainer.appendChild(
                    createBookingCard(
                        booking
                    )
                );

            }
        );


    } catch (error) {

        console.error(
            "Booking loading error:",
            error
        );


        bookingLoading.style.display =
            "none";


        bookingError.textContent =
            error.message ||
            "Unable to load your bookings.";


        bookingError.style.display =
            "block";

    }

}


// ========================================
// CREATE BOOKING CARD
// ========================================

function createBookingCard(
    booking
) {

    const card =
        document.createElement(
            "div"
        );


    card.className =
        "booking-card";


    const status =
        booking.status ||
        "UNKNOWN";


    const statusClass =
        status.toLowerCase();


    // ========================================
    // CANCEL BUTTON
    // ========================================

    let cancelButton = "";


    if (
        status === "CONFIRMED" ||
        status === "PENDING"
    ) {

        cancelButton = `

            <button
                type="button"
                class="cancel-booking-btn"
                onclick="cancelBooking(${booking.id})">

                Cancel Booking

            </button>

        `;

    }


    // ========================================
    // CARD HTML
    // ========================================

    card.innerHTML = `

        <div class="booking-card-header">

            <div>

                <span class="booking-label">
                    BOOKING
                </span>

                <h2>
                    #${booking.id}
                </h2>

            </div>


            <span
                class="booking-status ${statusClass}">

                ${status}

            </span>

        </div>


        <div class="booking-card-body">


            <div class="booking-detail">

                <span>
                    Schedule
                </span>

                <strong>
                    #${booking.scheduleId}
                </strong>

            </div>


            <div class="booking-detail">

                <span>
                    Travel Date
                </span>

                <strong>
                    ${formatDate(
        booking.travelDate
    )}
                </strong>

            </div>


            <div class="booking-detail">

                <span>
                    Total Amount
                </span>

                <strong class="booking-price">

                    Rs.
                    ${formatAmount(
        booking.totalAmount
    )}

                </strong>

            </div>


        </div>


        <div class="booking-card-footer">

            <span>

                ${
        booking.status === "CONFIRMED"

            ? "Your booking is confirmed."

            : booking.status === "CANCELLED"

                ? "This booking has been cancelled."

                : "This booking is pending."

    }

            </span>


            <div class="booking-actions">

                ${cancelButton}

            </div>

        </div>

    `;


    return card;

}


// ========================================
// CANCEL BOOKING
// ========================================

async function cancelBooking(
    bookingId
) {

    // ========================================
    // CONFIRM
    // ========================================

    const confirmed =
        window.confirm(
            `Are you sure you want to cancel Booking #${bookingId}?`
        );


    if (!confirmed) {

        return;

    }


    try {

        // ========================================
        // SEND CANCEL REQUEST
        // ========================================

        const response =
            await fetch(
                `/api/bookings/${bookingId}/cancel`,
                {
                    method: "PUT",

                    headers: {

                        "Authorization":
                            `Bearer ${token}`,

                        "Content-Type":
                            "application/json"

                    }

                }
            );


        // ========================================
        // CHECK RESPONSE
        // ========================================

        if (!response.ok) {

            let message =
                "Unable to cancel the booking.";


            try {

                const error =
                    await response.json();

                message =
                    error.message ||
                    message;

            } catch (e) {

                console.error(
                    "Could not read cancellation error.",
                    e
                );

            }


            throw new Error(
                message
            );

        }


        // ========================================
        // GET UPDATED BOOKING
        // ========================================

        const cancelledBooking =
            await response.json();


        console.log(
            "Booking cancelled:",
            cancelledBooking
        );


        // ========================================
        // RELOAD BOOKINGS
        // ========================================

        await loadMyBookings();


    } catch (error) {

        console.error(
            "Cancellation error:",
            error
        );


        bookingError.textContent =
            error.message ||
            "Unable to cancel the booking.";


        bookingError.style.display =
            "block";


        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });

    }

}


// ========================================
// FORMAT DATE
// ========================================

function formatDate(
    dateString
) {

    if (!dateString) {

        return "-";

    }


    const date =
        new Date(
            dateString +
            "T00:00:00"
        );


    return date.toLocaleDateString(
        "en-US",
        {
            year: "numeric",
            month: "long",
            day: "numeric"
        }
    );

}


// ========================================
// FORMAT AMOUNT
// ========================================

function formatAmount(
    amount
) {

    if (
        amount === null ||
        amount === undefined
    ) {

        return "0.00";

    }


    return Number(amount)
        .toLocaleString(
            "en-US",
            {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }
        );

}


// ========================================
// START
// ========================================

loadMyBookings();