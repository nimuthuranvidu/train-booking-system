
const contactForm = document.getElementById("contactForm");
const contactFeedback = document.getElementById("contactFeedback");

contactForm.addEventListener("submit", function (event) {
    event.preventDefault();

    const name = document.getElementById("contactName").value.trim();
    const email = document.getElementById("contactEmail").value.trim();
    const subject = document.getElementById("contactSubject").value;
    const message = document.getElementById("contactMessage").value.trim();

    if (!name || !email || !subject || !message) {
        showFeedback("Please complete all fields.", "error");
        return;
    }

    showFeedback(
        "Form validated successfully. Message sending is not connected yet.",
        "success"
    );
});

function showFeedback(message, type) {
    contactFeedback.textContent = message;
    contactFeedback.className =
        "contact-feedback is-visible is-" + type;
}
