// ===============================
// PASSENGER DETAILS PAGE
// ===============================

const passengerContainer =
    document.getElementById("passengerContainer");

const passengerCount =
    document.getElementById("passengerCount");

const continueBtn =
    document.getElementById("continueBtn");

const passengerError =
    document.getElementById("passengerError");

const journeyInfo =
    document.getElementById("journeyInfo");


// ===============================
// GET SAVED DATA
// ===============================

const searchData =
    sessionStorage.getItem("trainSearch");

const selectedSeatsData =
    sessionStorage.getItem("selectedSeats");


// ===============================
// CHECK DATA
// ===============================

if (!searchData || !selectedSeatsData) {

    showError(
        "Passenger information was not found. Please select your seats again."
    );

} else {

    const search =
        JSON.parse(searchData);

    const selectedSeats =
        JSON.parse(selectedSeatsData);

    displayJourney(search);

    createPassengerForms(selectedSeats);
}


// ===============================
// DISPLAY JOURNEY
// ===============================

function displayJourney(search) {

    journeyInfo.textContent =
        `${getStationName(search.sourceStationId)}
        → ${getStationName(search.destinationStationId)}
        • ${formatDate(search.travelDate)}`;

}


// ===============================
// CREATE PASSENGER FORMS
// ===============================

function createPassengerForms(
    selectedSeats
) {

    passengerContainer.innerHTML = "";

    passengerCount.textContent =
        selectedSeats.length;


    selectedSeats.forEach(
        (seat, index) => {

            const card =
                document.createElement("div");

            card.className =
                "passenger-card";

            card.style.animationDelay =
                `${index * 0.08}s`;


            card.innerHTML = `

                <div class="passenger-card-header">

                    <div class="passenger-title">

                        <div class="passenger-number">
                            ${index + 1}
                        </div>

                        <h2>
                            Passenger ${index + 1}
                        </h2>

                    </div>

                    <div class="passenger-seat">
                        Seat ${seat.coachNumber}-${seat.seatNumber}
                    </div>

                </div>


                <div class="passenger-form">

                    <div class="passenger-field full-width">

                        <label>
                            Full Name
                        </label>

                        <input
                            type="text"
                            class="passenger-name"
                            placeholder="Enter passenger name"
                            required
                        >

                    </div>


                    <div class="passenger-field">

                        <label>
                            Age
                        </label>

                        <input
                            type="number"
                            class="passenger-age"
                            min="1"
                            max="120"
                            placeholder="Age"
                            required
                        >

                    </div>


                    <div class="passenger-field">

                        <label>
                            Gender
                        </label>

                        <select
                            class="passenger-gender"
                            required>

                            <option value="">
                                Select
                            </option>

                            <option value="MALE">
                                Male
                            </option>

                            <option value="FEMALE">
                                Female
                            </option>

                            <option value="OTHER">
                                Other
                            </option>

                        </select>

                    </div>


                    <div class="passenger-field">

                        <label>
                            NIC
                        </label>

                        <input
                            type="text"
                            class="passenger-nic"
                            placeholder="NIC number"
                            required
                        >

                    </div>

                </div>

            `;


            passengerContainer.appendChild(
                card
            );

        }
    );

}


// ===============================
// CONTINUE
// ===============================

continueBtn.addEventListener(
    "click",
    () => {

        const cards =
            document.querySelectorAll(
                ".passenger-card"
            );


        const passengers = [];

        let valid = true;


        cards.forEach(
            card => {

                const name =
                    card.querySelector(
                        ".passenger-name"
                    ).value.trim();

                const age =
                    Number(
                        card.querySelector(
                            ".passenger-age"
                        ).value
                    );

                const gender =
                    card.querySelector(
                        ".passenger-gender"
                    ).value;

                const nic =
                    card.querySelector(
                        ".passenger-nic"
                    ).value.trim();


                if (
                    !name ||
                    !age ||
                    !gender ||
                    !nic
                ) {

                    valid = false;

                    return;
                }


                passengers.push({

                    name: name,

                    age: age,

                    gender: gender,

                    nic: nic

                });

            }
        );


        if (!valid) {

            showError(
                "Please complete all passenger details."
            );

            return;
        }


        // Save passenger information

        sessionStorage.setItem(
            "passengerDetails",
            JSON.stringify(
                passengers
            )
        );


        // Continue to booking summary

        window.location.href =
            "booking-summary.html";

    }
);


// ===============================
// ERROR
// ===============================

function showError(message) {

    passengerError.style.display =
        "block";

    passengerError.textContent =
        message;

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

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