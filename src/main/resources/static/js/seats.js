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
    sessionStorage.getItem(
        "selectedScheduleId"
    );


let search = null;

let allSeats = [];

let selectedSeats = [];


// ===============================
// START
// ===============================

if (!searchData || !scheduleId) {

    showError(
        "Train information was not found. Please search again."
    );

} else {

    search =
        JSON.parse(searchData);

    loadSeats();

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

            const error =
                await response.json();

            throw new Error(
                error.message ||
                "Unable to load seats."
            );
        }


        allSeats =
            await response.json();


        console.log(
            "Seats loaded:",
            allSeats
        );


        seatLoading.style.display =
            "none";


        buildTrainOverview();


    } catch (error) {

        console.error(error);

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


    allSeats.forEach(seat => {

        if (!coaches[seat.coachNumber]) {

            coaches[seat.coachNumber] = [];
        }


        coaches[seat.coachNumber].push(
            seat
        );

    });


    Object.keys(coaches).forEach(
        coachNumber => {

            const coach =
                document.createElement("div");

            coach.className =
                "train-coach";


            const windows =
                document.createElement("div");

            windows.className =
                "coach-windows";


            // Create windows

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


            // Click coach

            coach.addEventListener(
                "click",
                () => {

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

    selectedCoachName.textContent =
        `Coach ${coachNumber}`;


    coachSeatGrid.innerHTML = "";


    seats.forEach(seat => {

        const button =
            document.createElement(
                "button"
            );


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


        // Occupied

        if (!seat.available) {

            button.classList.add(
                "occupied"
            );

            button.disabled =
                true;

        } else {

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


        // Already selected

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


    // Hide train

    trainView.style.display =
        "none";


    // Show coach

    coachView.classList.add(
        "active"
    );


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


    // Remove

    if (index !== -1) {

        selectedSeats.splice(
            index,
            1
        );

        button.classList.remove(
            "selected"
        );

    }

    // Add

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


        sessionStorage.setItem(
            "selectedSeatIds",
            JSON.stringify(
                selectedSeats.map(
                    seat =>
                        seat.seatId
                )
            )
        );


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
            dateString + "T00:00:00"
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