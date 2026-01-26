const API_BASE = ""; // same-origin (served by Spring Boot)
let token = localStorage.getItem("token");

/* -------------------------
   PAGE GUARD (IMPORTANT)
-------------------------- */
window.onload = () => {
    const path = window.location.pathname;

    if (path.endsWith("dashboard.html")) {
        if (!token) {
            window.location.href = "/login.html";
        } else {
            loadUrls();
        }
    }

    if (path.endsWith("login.html")) {
        if (token) {
            window.location.href = "/dashboard.html";
        }
    }
};

/* -------------------------
   AUTH
-------------------------- */
function login() {
    fetch(`${API_BASE}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        return res.json();
    })
    .then(data => {
        localStorage.setItem("token", data.token);
        window.location.href = "/dashboard.html";
    })
    .catch(() => {
        document.getElementById("loginStatus").innerText = "Login failed";
    });
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
}

/* -------------------------
   URL SHORTEN
-------------------------- */
function shortenUrl() {
    fetch(`${API_BASE}/shorten`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            longUrl: document.getElementById("longUrl").value
        })
    })
    .then(res => res.json())
    .then(data => {
        document.getElementById("shortUrlResult").innerHTML =
            `<a href="/r/${data.shortCode}" target="_blank">
                ${window.location.origin}/r/${data.shortCode}
             </a>`;
        loadUrls();
    });
}

/* -------------------------
   LOAD URLS
-------------------------- */
function loadUrls() {
    const list = document.getElementById("urlList");
    list.innerHTML = "";

    fetch(`${API_BASE}/shorten?page=0&size=10`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(data => {

        if (!data.content || data.content.length === 0) {
            const li = document.createElement("li");
            li.innerText = "No URLs found";
            list.appendChild(li);
            return;
        }

        data.content.forEach(url => {
            const li = document.createElement("li");

            const text = document.createElement("span");
            text.innerText = `${url.shortCode} → ${url.longUrl}`;

            const delBtn = document.createElement("button");
            delBtn.innerText = "Delete";
            delBtn.style.marginLeft = "10px";
            delBtn.style.backgroundColor = "#dc2626";
            delBtn.onclick = () => deleteUrl(url.shortCode);

            li.appendChild(text);
            li.appendChild(delBtn);
            list.appendChild(li);
        });
    });
}

/* -------------------------
   DELETE URL (ADMIN)
-------------------------- */
function deleteUrl(code) {
    if (!confirm("Are you sure you want to delete this URL?")) return;

    fetch(`${API_BASE}/shorten/${code}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(res => {
        if (res.status === 204) {
            loadUrls();
        } else {
            alert("Delete failed (Admin only)");
        }
    });
}
