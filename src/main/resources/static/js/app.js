const searchForm = document.getElementById("searchForm");


// ========================================
// ONLY RUN HOME PAGE CODE IF FORM EXISTS
// ========================================

if (searchForm) {

    const swapBtn =
        document.getElementById("swapBtn");

    const fromStation =
        document.getElementById("from");

    const toStation =
        document.getElementById("to");

    const travelDate =
        document.getElementById("travelDate");


    // ========================================
    // SET MINIMUM TRAVEL DATE
    // ========================================

    if (travelDate) {

        const today = new Date();

        const year =
            today.getFullYear();

        const month =
            String(today.getMonth() + 1)
                .padStart(2, "0");

        const day =
            String(today.getDate())
                .padStart(2, "0");

        travelDate.min =
            `${year}-${month}-${day}`;
    }


    // ========================================
    // SWAP STATIONS
    // ========================================

    if (swapBtn) {

        swapBtn.addEventListener(
            "click",
            () => {

                const temp =
                    fromStation.value;

                fromStation.value =
                    toStation.value;

                toStation.value =
                    temp;
            }
        );
    }


    // ========================================
    // SEARCH TRAINS
    // ========================================

    searchForm.addEventListener(
        "submit",
        async (event) => {

            event.preventDefault();


            const from =
                Number(fromStation.value);

            const to =
                Number(toStation.value);

            const date =
                travelDate.value;

            const passengers =
                Number(
                    document.getElementById(
                        "passengers"
                    ).value
                );


            // ========================================
            // VALIDATION
            // ========================================

            if (!from) {

                alert(
                    "Please select a departure station."
                );

                return;
            }


            if (!to) {

                alert(
                    "Please select a destination station."
                );

                return;
            }


            if (from === to) {

                alert(
                    "Departure and destination stations cannot be the same."
                );

                return;
            }


            if (!date) {

                alert(
                    "Please select a travel date."
                );

                return;
            }


            if (passengers < 1) {

                alert(
                    "At least one passenger is required."
                );

                return;
            }


            // ========================================
            // CREATE REQUEST
            // ========================================

            const searchRequest = {

                sourceStationId: from,

                destinationStationId: to,

                travelDate: date,

                numberOfPassengers:
                passengers
            };


            console.log(
                "Sending train search request:",
                searchRequest
            );


            // ========================================
            // CALL BACKEND
            // ========================================

            try {

                const response =
                    await fetch(
                        "/api/train-search",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify(
                                    searchRequest
                                )
                        }
                    );


                // ========================================
                // CHECK RESPONSE
                // ========================================

                if (!response.ok) {

                    let errorMessage =
                        "Unable to search trains.";

                    try {

                        const error =
                            await response.json();

                        if (error.message) {

                            errorMessage =
                                error.message;
                        }

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


                // ========================================
                // GET RESULTS
                // ========================================

                const results =
                    await response.json();


                console.log(
                    "Train search successful:",
                    results
                );


                // ========================================
                // SAVE SEARCH DATA
                // ========================================

                sessionStorage.setItem(
                    "trainSearch",
                    JSON.stringify(
                        searchRequest
                    )
                );


                sessionStorage.setItem(
                    "trainSearchResults",
                    JSON.stringify(
                        results
                    )
                );


                // ========================================
                // OPEN RESULTS PAGE
                // ========================================

                window.location.href =
                    "search-results.html";

            } catch (error) {

                console.error(
                    "Train search error:",
                    error
                );


                alert(
                    error.message ||
                    "Something went wrong while searching for trains."
                );
            }

        }
    );

}