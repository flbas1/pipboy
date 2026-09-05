async function loadMenu() {
    const response = await fetch("/api/menu");

    if (!response.ok) {
        throw new Error("Unable to load Pip-Boy menu");
    }

    const profile = await response.json();

    document.getElementById("party-title").textContent =
        profile.title;

    renderItems("food", profile.menu.food);
    renderItems("treats", profile.menu.treats);
    renderItems("beverages", profile.menu.beverages);
}

function renderItems(elementId, items) {
    const container = document.getElementById(elementId);

    container.innerHTML = items
        .map(item => `<div class="item">${escapeHtml(item)}</div>`)
        .join("");
}

function escapeHtml(value) {
    return value
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function updateClock() {
    document.getElementById("clock").textContent =
        new Date().toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit"
        });
}

document.querySelectorAll(".tab").forEach(button => {
    button.addEventListener("click", () => {

        document.querySelectorAll(".tab")
            .forEach(tab => tab.classList.remove("active"));

        document.querySelectorAll(".tab-content")
            .forEach(content => content.classList.remove("active"));

        button.classList.add("active");

        document.getElementById(button.dataset.tab)
            .classList.add("active");
    });
});

loadMenu().catch(error => {
    console.error(error);
    document.getElementById("party-title").textContent =
        "SYSTEM ERROR";
});

updateClock();
setInterval(updateClock, 1000);
