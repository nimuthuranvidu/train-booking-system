// ===============================
// SEAT PAGE
// ===============================

const trainView =
    document.getElementById("trainView");

const coachOverview =
    document.getElementById("coachOverview");

const coachView =
    document.getElementById("coachView");

const coachSeatGrid =
    document.getElementById("coachSeatGrid");

const selectedCoachName =
    document.getElementById("selectedCoachName");

const backToTrain =
    document.getElementById("backToTrain");

const seatLoading =
    document.getElementById("seatLoading");

const seatError =
    document.getElementById("seatError");

const journeyInfo =
    document.getElementById("journeyInfo");

const selectedSeatsElement =
    document.getElementById("selectedSeats");

const selectedCountElement =
    document.getElementById("selectedCount");

const continueBtn =
    document.getElementById("continueBtn");


// ===============================
// GET SAVED DATA
// ===============================

const searchData =
    sessionStorage.getItem("trainSearch");

const scheduleId =
    sessionStorage.getItem("selectedScheduleId");


let search = null;

let allSeats = [];


// ===============================
// LOAD SELECTED SEATS
// ===============================
// FIX:
// Previously selectedSeats was not declared.
// This caused:
// ReferenceError: selectedSeats is not defined

let selectedSeats = JSON.parse(
    sessionStorage.getItem("selectedSeats") || "[]"
);


// ===============================
// START
// ===============================

if (!searchData || !scheduleId) {

    showError(
        "Train information was not found. Please search again."
    );

} else {

    try {

        search =
            JSON.parse(searchData);

        loadSeats();

    } catch (error) {

        console.error(error);

        showError(
            "Invalid train search information."
        );
    }
}


// ===============================
// LOAD SEATS
// ===============================

async function loadSeats() {

    try {

        journeyInfo.textContent =
            `${getStationName(search.sourceStationId)}
            → ${getStationName(search.destinationStationId)}
            • ${formatDate(search.travelDate)}
            • ${search.numberOfPassengers} passenger(s)`;


        const response =
            await fetch(
                `/api/seat-availability?scheduleId=${scheduleId}&travelDate=${search.travelDate}`
            );


        if (!response.ok) {

            let errorMessage =
                "Unable to load seats.";

            try {

                const error =
                    await response.json();

                errorMessage =
                    error.message ||
                    errorMessage;

            } catch (e) {

                console.error(
                    "Could not read error response.",
                    e
                );
            }

            throw new Error(
                errorMessage
            );
        }


        allSeats =
            await response.json();


        console.log(
            "Seats loaded:",
            allSeats
        );


        if (!Array.isArray(allSeats)) {

            throw new Error(
                "Invalid seat data received from server."
            );
        }


        seatLoading.style.display =
            "none";


        buildTrainOverview();


    } catch (error) {

        console.error(
            "Seat loading error:",
            error
        );

        showError(
            error.message ||
            "Unable to load seats."
        );

    }

}


// ===============================
// BUILD TRAIN
// ===============================

function buildTrainOverview() {

    coachOverview.innerHTML = "";


    const coaches = {};


    // Group seats by coach
    allSeats.forEach(seat => {

        if (!coaches[seat.coachNumber]) {

            coaches[seat.coachNumber] = [];
        }


        coaches[seat.coachNumber].push(
            seat
        );

    });


    console.log(
        "Coaches:",
        coaches
    );


    // If no coaches
    if (Object.keys(coaches).length === 0) {

        showError(
            "No coaches or seats are available for this train."
        );

        return;
    }


    // Create each coach
    Object.keys(coaches).forEach(
        coachNumber => {

            const coach =
                document.createElement("div");

            coach.className =
                "train-coach";


            // ===============================
            // WINDOWS
            // ===============================

            const windows =
                document.createElement("div");

            windows.className =
                "coach-windows";


            for (
                let i = 0;
                i < Math.min(
                    coaches[coachNumber].length,
                    6
                );
                i++
            ) {

                const window =
                    document.createElement("div");

                window.className =
                    "coach-window";

                windows.appendChild(
                    window
                );

            }


            // ===============================
            // COACH LABEL
            // ===============================

            const label =
                document.createElement("div");

            label.className =
                "coach-label";

            label.textContent =
                `COACH ${coachNumber}`;


            coach.appendChild(
                windows
            );

            coach.appendChild(
                label
            );


            // ===============================
            // CLICK COACH
            // ===============================

            coach.addEventListener(
                "click",
                () => {

                    console.log(
                        "Coach clicked:",
                        coachNumber
                    );

                    openCoach(
                        coachNumber,
                        coaches[coachNumber]
                    );

                }
            );


            coachOverview.appendChild(
                coach
            );

        }
    );

}


// ===============================
// OPEN COACH
// ===============================

function openCoach(
    coachNumber,
    seats
) {

    console.log(
        "Opening coach:",
        coachNumber
    );

    console.log(
        "Seats in coach:",
        seats
    );


    selectedCoachName.textContent =
        `Coach ${coachNumber}`;


    coachSeatGrid.innerHTML = "";


    // ===============================
    // CREATE SEATS
    // ===============================

    seats.forEach(seat => {

        const button =
            document.createElement(
                "button"
            );


        button.type =
            "button";


        button.className =
            "train-seat";


        button.innerHTML = `

            <span class="train-seat-number">
                ${seat.seatNumber}
            </span>

            <span class="train-seat-type">
                ${seat.seatType}
            </span>

        `;


        // ===============================
        // OCCUPIED
        // ===============================

        if (!seat.available) {

            button.classList.add(
                "occupied"
            );

            button.disabled =
                true;

        }

            // ===============================
            // AVAILABLE
        // ===============================

        else {

            button.addEventListener(
                "click",
                () => {

                    toggleSeat(
                        seat,
                        button
                    );

                }
            );

        }


        // ===============================
        // ALREADY SELECTED
        // ===============================

        if (
            selectedSeats.some(
                selected =>
                    selected.seatId ===
                    seat.seatId
            )
        ) {

            button.classList.add(
                "selected"
            );

        }


        coachSeatGrid.appendChild(
            button
        );

    });


    // ===============================
    // SHOW COACH VIEW
    // ===============================

    trainView.style.display =
        "none";


    coachView.classList.add(
        "active"
    );


    // Extra safety in case CSS is missing
    coachView.style.display =
        "block";


    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

}


// ===============================
// BACK TO TRAIN
// ===============================

backToTrain.addEventListener(
    "click",
    () => {

        coachView.classList.remove(
            "active"
        );


        // Extra safety
        coachView.style.display =
            "none";


        setTimeout(() => {

            trainView.style.display =
                "flex";

        }, 100);


        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });

    }
);


// ===============================
// TOGGLE SEAT
// ===============================

function toggleSeat(
    seat,
    button
) {

    const index =
        selectedSeats.findIndex(
            selected =>
                selected.seatId ===
                seat.seatId
        );


    // ===============================
    // REMOVE SEAT
    // ===============================

    if (index !== -1) {

        selectedSeats.splice(
            index,
            1
        );


        button.classList.remove(
            "selected"
        );

    }


        // ===============================
        // ADD SEAT
    // ===============================

    else {

        const limit =
            Number(
                search.numberOfPassengers
            );


        if (
            selectedSeats.length >=
            limit
        ) {

            alert(
                `You can select only ${limit} seat(s).`
            );

            return;
        }


        selectedSeats.push(
            seat
        );


        button.classList.add(
            "selected"
        );

    }


    // ===============================
    // SAVE
    // ===============================

    sessionStorage.setItem(
        "selectedSeats",
        JSON.stringify(
            selectedSeats
        )
    );


    sessionStorage.setItem(
        "selectedSeatIds",
        JSON.stringify(
            selectedSeats.map(
                seat =>
                    seat.seatId
            )
        )
    );


    updateSelection();

}


// ===============================
// UPDATE SUMMARY
// ===============================

function updateSelection() {

    selectedCountElement.textContent =
        selectedSeats.length;


    if (selectedSeats.length === 0) {

        selectedSeatsElement.textContent =
            "None";

        continueBtn.disabled =
            true;

        return;
    }


    selectedSeatsElement.textContent =
        selectedSeats
            .map(
                seat =>
                    `${seat.coachNumber}-${seat.seatNumber}`
            )
            .join(", ");


    continueBtn.disabled =
        false;

}


// ===============================
// CONTINUE
// ===============================

continueBtn.addEventListener(
    "click",
    () => {

        if (
            selectedSeats.length === 0
        ) {

            return;
        }


        // Save seat IDs
        sessionStorage.setItem(
            "selectedSeatIds",
            JSON.stringify(
                selectedSeats.map(
                    seat =>
                        seat.seatId
                )
            )
        );


        // Save full seat objects
        sessionStorage.setItem(
            "selectedSeats",
            JSON.stringify(
                selectedSeats
            )
        );


        window.location.href =
            "passengers.html";

    }
);


// ===============================
// ERROR
// ===============================

function showError(message) {

    seatLoading.style.display =
        "none";


    seatError.style.display =
        "block";


    seatError.textContent =
        message;

}


// ===============================
// STATION NAME
// ===============================

function getStationName(
    stationId
) {

    const stations = {

        1: "Colombo Fort",

        2: "Kandy",

        3: "Galle",

        4: "Matara"

    };


    return stations[stationId] ||
        "Unknown Station";

}


// ===============================
// FORMAT DATE
// ===============================

function formatDate(
    dateString
) {

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