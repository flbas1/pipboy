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

document.getElementById("prev-track").onclick = () =>
    fetch("/api/ma-prev");

document.getElementById("play-pause").onclick = () =>
    fetch("/api/ma-toggle");

document.getElementById("next-track").onclick = () =>
    fetch("/api/ma-next");


function updateNowPlaying() {
    fetch("/api/ma-now-playing")
        .then(response => response.json())
        .then(song => {
            if (song.title) {
                document.getElementById("now-playing").textContent =
                    song.artist + " - " + song.title;

                if (song.image_url) {
                    const imageUrl = new URL(song.image_url);

                    document.getElementById("album-art").src =
                        "/api/ma-image?path=" +
                        encodeURIComponent(imageUrl.pathname + imageUrl.search);
                }
            }
        });
}



updateNowPlaying();
setInterval(updateNowPlaying, 5000);

updateClock();
setInterval(updateClock, 1000);
