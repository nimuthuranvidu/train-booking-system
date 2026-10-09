// =========================================
// BOOKING SUMMARY
// =========================================


// =========================================
// GET SAVED DATA
// =========================================

const searchData =
    sessionStorage.getItem("trainSearch");

const searchResultsData =
    sessionStorage.getItem("trainSearchResults");

const selectedScheduleId =
    sessionStorage.getItem("selectedScheduleId");

const selectedSeatsData =
    sessionStorage.getItem("selectedSeats");

const selectedSeatIdsData =
    sessionStorage.getItem("selectedSeatIds");

const passengerDetailsData =
    sessionStorage.getItem("passengerDetails");


// =========================================
// ELEMENTS
// =========================================

const fromStation =
    document.getElementById("fromStation");

const toStation =
    document.getElementById("toStation");

const travelDate =
    document.getElementById("travelDate");

const trainName =
    document.getElementById("trainName");

const departureTime =
    document.getElementById("departureTime");

const arrivalTime =
    document.getElementById("arrivalTime");

const selectedSeatsContainer =
    document.getElementById("selectedSeats");

const passengerList =
    document.getElementById("passengerList");

const passengerCount =
    document.getElementById("summaryPassengerCount");

const seatCount =
    document.getElementById("summarySeatCount");

const totalPrice =
    document.getElementById("totalPrice");

const errorElement =
    document.getElementById("summaryError");

const confirmButton =
    document.getElementById("confirmBookingBtn");


// =========================================
// CHECK DATA
// =========================================

if (
    !searchData ||
    !searchResultsData ||
    !selectedScheduleId ||
    !selectedSeatsData ||
    !selectedSeatIdsData ||
    !passengerDetailsData
) {

    showError(
        "Booking information is incomplete. Please go back and complete the booking process."
    );

} else {

    try {

        const search =
            JSON.parse(searchData);

        const results =
            JSON.parse(searchResultsData);

        const seats =
            JSON.parse(selectedSeatsData);

        const seatIds =
            JSON.parse(selectedSeatIdsData);

        const passengers =
            JSON.parse(passengerDetailsData);


        // =====================================
        // CHECK SEATS
        // =====================================

        if (!seatIds || seatIds.length === 0) {

            showError(
                "No seat IDs were found. Please select your seats again."
            );

        } else {

            const selectedTrain =
                results.find(
                    train =>
                        train.scheduleId == selectedScheduleId
                );


            if (!selectedTrain) {

                showError(
                    "Selected train information could not be found."
                );

            } else {

                displayJourney(
                    search,
                    selectedTrain
                );

                displaySeats(
                    seats
                );

                displayPassengers(
                    passengers,
                    seats
                );

                calculatePrice(
                    seats
                );

            }

        }

    } catch (error) {

        console.error(
            "Error loading booking summary:",
            error
        );

        showError(
            "Unable to load booking information."
        );

    }

}


// =========================================
// DISPLAY JOURNEY
// =========================================

function displayJourney(
    search,
    train
) {

    fromStation.textContent =
        getStationName(
            search.sourceStationId
        );

    toStation.textContent =
        getStationName(
            search.destinationStationId
        );

    travelDate.textContent =
        formatDate(
            search.travelDate
        );

    trainName.textContent =
        train.trainName;

    departureTime.textContent =
        train.departureTime;

    arrivalTime.textContent =
        train.arrivalTime;

}


// =========================================
// DISPLAY SEATS
// =========================================

function displaySeats(seats) {

    selectedSeatsContainer.innerHTML = "";

    seatCount.textContent =
        seats.length;


    seats.forEach(
        seat => {

            const element =
                document.createElement("div");

            element.className =
                "summary-seat";

            element.textContent =
                `Coach ${seat.coachNumber} • Seat ${seat.seatNumber}`;

            selectedSeatsContainer.appendChild(
                element
            );

        }
    );

}


// =========================================
// DISPLAY PASSENGERS
// =========================================

function displayPassengers(
    passengers,
    seats
) {

    passengerList.innerHTML = "";

    passengerCount.textContent =
        passengers.length;


    passengers.forEach(
        (passenger, index) => {

            const seat =
                seats[index];


            const element =
                document.createElement("div");

            element.className =
                "passenger-summary";


            element.innerHTML = `

                <div class="passenger-summary-info">

                    <div class="passenger-summary-icon">
                        👤
                    </div>

                    <div>

                        <div class="passenger-summary-name">
                            ${escapeHtml(passenger.name)}
                        </div>

                        <div class="passenger-summary-details">

                            Age ${passenger.age}
                            •
                            ${escapeHtml(passenger.gender)}
                            •
                            NIC: ${escapeHtml(passenger.nic)}

                        </div>

                    </div>

                </div>


                <div class="passenger-summary-seat">

                    Seat ${seat.seatNumber}

                </div>

            `;


            passengerList.appendChild(
                element
            );

        }
    );

}


// =========================================
// PRICE
// =========================================

function calculatePrice(seats) {

    const pricePerSeat = 1000;

    const total =
        seats.length *
        pricePerSeat;


    totalPrice.textContent =
        `Rs. ${total.toLocaleString()}`;

}


// =========================================
// CONFIRM BUTTON
// =========================================

confirmButton.addEventListener(
    "click",
    async () => {

        await confirmBooking();

    }
);


// =========================================
// MAIN BOOKING PROCESS
// =========================================

async function confirmBooking() {

    try {

        confirmButton.disabled = true;

        confirmButton.textContent =
            "Processing Booking...";

        hideError();


        // =====================================
        // GET DATA
        // =====================================

        const search =
            JSON.parse(searchData);

        const results =
            JSON.parse(searchResultsData);

        const seats =
            JSON.parse(selectedSeatsData);

        const seatIds =
            JSON.parse(selectedSeatIdsData);

        const passengers =
            JSON.parse(passengerDetailsData);


        // =====================================
        // FIND TRAIN
        // =====================================

        const selectedTrain =
            results.find(
                train =>
                    train.scheduleId == selectedScheduleId
            );


        if (!selectedTrain) {

            throw new Error(
                "Selected train information could not be found."
            );

        }


        // =====================================
        // CHECK SEATS
        // =====================================

        if (
            !seatIds ||
            seatIds.length === 0
        ) {

            throw new Error(
                "No seats were selected."
            );

        }


        if (
            seatIds.some(
                id =>
                    id === null ||
                    id === undefined
            )
        ) {

            throw new Error(
                "One or more selected seats have an invalid ID. Please select your seats again."
            );

        }


        // =====================================
        // GET JWT
        // =====================================

        const token =
            localStorage.getItem("token");


        if (!token) {

            throw new Error(
                "You must be logged in to complete a booking."
            );

        }


        // =====================================
        // CALCULATE TOTAL
        // =====================================

        const totalAmount =
            seats.length * 1000;


        // =====================================
        // STEP 1
        // CREATE BOOKING
        // =====================================

        console.log(
            "STEP 1: Creating booking..."
        );


        const bookingResponse =
            await fetch(
                "/api/bookings",
                {
                    method: "POST",

                    headers: {

                        "Content-Type":
                            "application/json",

                        "Authorization":
                            `Bearer ${token}`

                    },

                    body: JSON.stringify({

                        scheduleId:
                            Number(
                                selectedScheduleId
                            ),

                        travelDate:
                        search.travelDate,

                        totalAmount:
                        totalAmount

                    })

                }
            );


        if (!bookingResponse.ok) {

            throw new Error(
                await getErrorMessage(
                    bookingResponse
                )
            );

        }


        const booking =
            await bookingResponse.json();


        console.log(
            "Booking created:",
            booking
        );


        const bookingId =
            booking.bookingId ||
            booking.id;


        if (!bookingId) {

            throw new Error(
                "Booking was created but no booking ID was returned."
            );

        }


        // =====================================
        // STEP 2
        // SELECT SEATS
        // =====================================

        console.log(
            "STEP 2: Selecting seats..."
        );


        const seatResponse =
            await fetch(
                "/api/booking-seats/select",
                {
                    method: "POST",

                    headers: {

                        "Content-Type":
                            "application/json",

                        "Authorization":
                            `Bearer ${token}`

                    },

                    body: JSON.stringify({

                        bookingId:
                            Number(bookingId),

                        seatIds:
                            seatIds.map(
                                id => Number(id)
                            )

                    })

                }
            );


        if (!seatResponse.ok) {

            throw new Error(
                await getErrorMessage(
                    seatResponse
                )
            );

        }


        console.log(
            "Seats selected successfully."
        );


        // =====================================
        // STEP 3
        // ADD PASSENGERS
        // =====================================

        console.log(
            "STEP 3: Adding passengers..."
        );


        for (
            let i = 0;
            i < passengers.length;
            i++
        ) {

            const passenger =
                passengers[i];

            const seatId =
                seatIds[i];


            if (!seatId) {

                throw new Error(
                    `Seat ID missing for passenger ${i + 1}.`
                );

            }


            const passengerResponse =
                await fetch(
                    "/api/passengers",
                    {
                        method: "POST",

                        headers: {

                            "Content-Type":
                                "application/json",

                            "Authorization":
                                `Bearer ${token}`

                        },

                        body: JSON.stringify({

                            name:
                            passenger.name,

                            age:
                                Number(
                                    passenger.age
                                ),

                            gender:
                            passenger.gender,

                            nic:
                            passenger.nic,

                            bookingId:
                                Number(
                                    bookingId
                                ),

                            seatId:
                                Number(
                                    seatId
                                )

                        })

                    }
                );


            if (!passengerResponse.ok) {

                throw new Error(
                    await getErrorMessage(
                        passengerResponse
                    )
                );

            }

        }


        console.log(
            "Passengers added successfully."
        );


        // =====================================
        // STEP 4
        // CONFIRM BOOKING
        // =====================================

        console.log(
            "STEP 4: Confirming booking..."
        );


        const confirmResponse =
            await fetch(
                `/api/bookings/${bookingId}/confirm`,
                {
                    method: "PUT",

                    headers: {

                        "Authorization":
                            `Bearer ${token}`

                    }

                }
            );


        if (!confirmResponse.ok) {

            throw new Error(
                await getErrorMessage(
                    confirmResponse
                )
            );

        }


        const confirmedBooking =
            await confirmResponse.json();


        console.log(
            "Booking confirmed:",
            confirmedBooking
        );


        // =====================================
        // CREATE CONFIRMATION DATA
        // =====================================

        const confirmationData = {

            // Backend booking information
            id:
            bookingId,

            status:
            confirmedBooking.status,

            // Journey
            trainName:
            selectedTrain.trainName,

            trainNumber:
            selectedTrain.trainNumber,

            journey:
                `${getStationName(search.sourceStationId)} → ${getStationName(search.destinationStationId)}`,

            fromStation:
                getStationName(search.sourceStationId),

            toStation:
                getStationName(search.destinationStationId),

            travelDate:
            search.travelDate,

            departureTime:
            selectedTrain.departureTime,

            arrivalTime:
            selectedTrain.arrivalTime,

            // Seats
            selectedSeats:
                seats
                    .map(
                        seat =>
                            `Coach ${seat.coachNumber} - Seat ${seat.seatNumber}`
                    )
                    .join(", "),

            seatCount:
            seats.length,

            // Passengers
            passengerCount:
            passengers.length,

            passengers:
                passengers.map(
                    passenger => ({
                        name: passenger.name,
                        age: passenger.age,
                        gender: passenger.gender,
                        nic: passenger.nic
                    })
                ),

            // Price
            totalAmount:
            totalAmount

        };


        console.log(
            "Confirmation data:",
            confirmationData
        );


        // =====================================
        // SAVE CONFIRMATION DATA
        // =====================================

        sessionStorage.setItem(
            "confirmedBooking",
            JSON.stringify(
                confirmationData
            )
        );


        sessionStorage.setItem(
            "bookingId",
            bookingId
        );


        // =====================================
        // GO TO CONFIRMATION
        // =====================================

        window.location.href =
            "booking-confirmation.html";


    } catch (error) {

        console.error(
            "BOOKING ERROR:",
            error
        );


        showError(
            error.message ||
            "Something went wrong while creating the booking."
        );


        confirmButton.disabled =
            false;

        confirmButton.textContent =
            "Confirm Booking →";

    }

}


// =========================================
// BACKEND ERROR
// =========================================

async function getErrorMessage(response) {

    try {

        const data =
            await response.json();


        if (data.message) {

            return data.message;

        }


        return `Request failed with status ${response.status}.`;

    } catch {

        return `Request failed with status ${response.status}.`;

    }

}


// =========================================
// STATION NAME
// =========================================

function getStationName(stationId) {

    const stations = {

        1: "Colombo Fort",

        2: "Kandy",

        3: "Galle",

        4: "Matara"

    };


    return stations[stationId] ||
        "Unknown Station";

}


// =========================================
// DATE
// =========================================

function formatDate(dateString) {

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


// =========================================
// ESCAPE HTML
// =========================================

function escapeHtml(value) {

    const div =
        document.createElement("div");

    div.textContent =
        value;

    return div.innerHTML;

}


// =========================================
// ERROR
// =========================================

function showError(message) {

    errorElement.style.display =
        "block";

    errorElement.textContent =
        message;

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

}


function hideError() {

    errorElement.style.display =
        "none";

}