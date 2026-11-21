async function loadMenu() {
    const cached = localStorage.getItem('party-menu');

    if (cached) {
        console.log("Loading menu from local cache");

        const profile = JSON.parse(cached);
        renderMenu(profile);

        return;
    }

    console.log("Downloading menu from server");

    const response = await fetch('/api/menu');

    if (!response.ok) {
        throw new Error('Unable to download menu');
    }

    const profile = await response.json();

    // Save it for offline use
    localStorage.setItem('party-menu', JSON.stringify(profile));

    renderMenu(profile);
}


function renderMenu(profile) {
    document.getElementById("party-title").textContent =
        profile.title;

    renderItems("food", profile.menu.food);
    renderItems("treats", profile.menu.treats);
    renderItems("beverages", profile.menu.beverages);
    renderItems("dessert", profile.menu.dessert);
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


// Tab handling
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


// Load menu
loadMenu().catch(error => {
    console.error(error);

    document.getElementById("party-title").textContent =
        "SYSTEM ERROR";
});


// Music Assistant controls
document.getElementById("prev-track").onclick = () =>
    fetch("/api/ma-prev").catch(console.error);

document.getElementById("play-pause").onclick = () =>
    fetch("/api/ma-toggle").catch(console.error);

document.getElementById("next-track").onclick = () =>
    fetch("/api/ma-next").catch(console.error);


// Now playing
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
                        encodeURIComponent(
                            imageUrl.pathname + imageUrl.search
                        );
                }
            }
        })
        .catch(() => {
            // Offline — leave the existing display alone
        });
}


updateNowPlaying();
setInterval(updateNowPlaying, 5000);

updateClock();
setInterval(updateClock, 1000);