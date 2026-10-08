// ===============================
// SEARCH RESULTS PAGE
// ===============================

const resultsContainer =
    document.getElementById("resultsContainer");

const loading =
    document.getElementById("loading");

const errorMessage =
    document.getElementById("errorMessage");

const journeyInfo =
    document.getElementById("journeyInfo");


// ===============================
// STATION NAMES
// ===============================

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


// ===============================
// FORMAT DATE
// ===============================

function formatDate(dateString) {

    const date =
        new Date(dateString + "T00:00:00");

    return date.toLocaleDateString(
        "en-US",
        {
            year: "numeric",
            month: "long",
            day: "numeric"
        }
    );
}


// ===============================
// DISPLAY RESULTS
// ===============================

function displayResults(results, search) {

    console.log("Displaying results...");
    console.log(results);

    loading.style.display = "none";


    journeyInfo.textContent =
        `${getStationName(search.sourceStationId)}
        → ${getStationName(search.destinationStationId)}
        • ${formatDate(search.travelDate)}
        • ${search.numberOfPassengers} passenger(s)`;


    // No trains found

    if (!results || results.length === 0) {

        resultsContainer.innerHTML = `

            <div class="no-results">

                <h2>No trains found</h2>

                <p>
                    There are no available trains
                    for your selected journey.
                </p>

            </div>

        `;

        return;
    }


    // Clear existing results

    resultsContainer.innerHTML = "";


    // Create train cards

    results.forEach(train => {

        const card =
            document.createElement("div");

        card.className = "train-card";


        card.innerHTML = `

            <div class="train-main">

                <div class="train-name">
                    ${train.trainName}
                </div>

                <div class="train-number">
                    Train ${train.trainNumber}
                </div>

                <div class="train-time">

                    <span class="time">
                        ${train.departureTime}
                    </span>

                    <span class="time-line"></span>

                    <span class="time">
                        ${train.arrivalTime}
                    </span>

                </div>

            </div>


            <div class="train-info">

                <div class="available-label">
                    Available Seats
                </div>

                <div class="available-seats">
                    ${train.availableSeats}
                </div>

            </div>


            <button
                class="select-train-btn"
                onclick="selectTrain(${train.scheduleId})">

                Select Train

            </button>

        `;


        resultsContainer.appendChild(card);

    });

}


// ===============================
// SELECT TRAIN
// ===============================

function selectTrain(scheduleId) {

    console.log(
        "Selected schedule:",
        scheduleId
    );


    sessionStorage.setItem(
        "selectedScheduleId",
        scheduleId
    );


    window.location.href =
        "seats.html";
}


// ===============================
// LOAD SAVED SEARCH
// ===============================

console.log(
    "search.js loaded successfully"
);


const searchData =
    sessionStorage.getItem("trainSearch");

const resultData =
    sessionStorage.getItem("trainSearchResults");


console.log(
    "Saved search:",
    searchData
);

console.log(
    "Saved results:",
    resultData
);


// ===============================
// CHECK DATA
// ===============================

if (!searchData || !resultData) {

    loading.style.display = "none";

    errorMessage.style.display = "block";

    errorMessage.textContent =
        "No search information found. Please return to the home page and search again.";

} else {

    try {

        const search =
            JSON.parse(searchData);

        const results =
            JSON.parse(resultData);


        displayResults(
            results,
            search
        );


    } catch (error) {

        console.error(
            "Error loading search results:",
            error
        );


        loading.style.display = "none";

        errorMessage.style.display = "block";

        errorMessage.textContent =
            "Unable to display train results.";

    }

}