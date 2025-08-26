(function () {
  // Elements
  const resultsSection = document.getElementById("resultsSection");
  const resultsTableBody = document.querySelector("#resultsTable tbody");
  const searchForm = document.getElementById("searchForm");

  const bookingModal = document.getElementById("bookingModal");
  const bookingForm = document.getElementById("bookingForm");
  const closeModal = document.getElementById("closeModal");
  const cancelBooking = document.getElementById("cancelBooking");

  const btnReservations = document.getElementById("btn-reservations");
  const myReservations = document.getElementById("myReservations");

  const cancelForm = document.getElementById("cancelForm");
  const cancelMsg = document.getElementById("cancelMsg");

  // Show results section when search submitted
  searchForm.addEventListener("submit", function (e) {
    // Let the browser perform the GET request (or you can intercept and fetch via AJAX)
    // For demo, prevent submission and show a demo row
    e.preventDefault();

    // Clear previous rows
    resultsTableBody.innerHTML = "";

    // Demo: create two example rows (replace with real data from server)
    const demoData = [
      {
        id: 1,
        number: "12001",
        name: "Shatabdi Express",
        from: "City A",
        to: "City B",
        dep: "06:00",
        arr: "12:30",
        classType: "AC",
        seats: 8,
      },
      {
        id: 2,
        number: "12567",
        name: "InterCity",
        from: "City A",
        to: "City B",
        dep: "08:30",
        arr: "14:00",
        classType: "SLEEPER",
        seats: 5,
      },
    ];

    demoData.forEach((tr) => {
      const trEl = document.createElement("tr");
      trEl.innerHTML = `
            <td>${tr.number}</td>
            <td>${tr.name}</td>
            <td>${tr.from}</td>
            <td>${tr.to}</td>
            <td>${tr.dep}</td>
            <td>${tr.arr}</td>
            <td>${tr.classType}</td>
            <td>${tr.seats}</td>
            <td><button class="btn btn-sm btn-primary book-btn" 
                data-train-id="${tr.id}" 
                data-train-number="${tr.number}" 
                data-train-name="${tr.name}" 
                data-from="${tr.from}"
                data-to="${tr.to}"
                data-class="${tr.classType}"
              >Book</button></td>
          `;
      resultsTableBody.appendChild(trEl);
    });

    resultsSection.hidden = false;
  });

  // Delegate clicks on book buttons
  resultsTableBody.addEventListener("click", function (e) {
    if (e.target && e.target.matches(".book-btn")) {
      const btn = e.target;
      openBookingModal({
        trainId: btn.dataset.trainId,
        trainNumber: btn.dataset.trainNumber,
        trainName: btn.dataset.trainName,
        from: btn.dataset.from,
        to: btn.dataset.to,
        classType: btn.dataset.class,
      });
    }
  });

  function openBookingModal(data) {
    document.getElementById("trainId").value = data.trainId;
    document.getElementById("trainNumber").value = data.trainNumber;
    document.getElementById("trainName").value = data.trainName;
    document.getElementById("fromStation").value = data.from;
    document.getElementById("toStation").value = data.to;
    document.getElementById("bookingClass").value = data.classType;
    // optionally set default journey date from search
    bookingModal.removeAttribute("aria-hidden");
    bookingModal.style.display = "block";
  }

  closeModal.addEventListener("click", closeBooking);
  cancelBooking.addEventListener("click", closeBooking);
  function closeBooking() {
    bookingModal.style.display = "none";
    bookingModal.setAttribute("aria-hidden", "true");
  }

  // Toggle reservations panel
  btnReservations.addEventListener("click", async function () {
    if (myReservations.hidden) {
      // fetch user's reservations from /api/reservations/user/{userId}
      const res = await fetch("/api/reservations/user/me");
      if (!res.ok) {
        console.error("failed to fetch trains");
        return;
      }
      const reservations = await res.json();
      const tbody = myReservations.querySelector("tbody");
      tbody.innerHTML = "";
      reservations.forEach((r) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${r.pnrNumber}</td>
            <td>${r.train.trainName}</td>
            <td>${r.passengerName}</td>
            <td>${new Date(r.journeyDate).toLocaleDateString()}</td>
            <td>${r.fromStation}</td>
            <td>${r.toStation}</td>
            <td>${r.classType}</td>
            <td>${r.status}</td>
            <td>${
              r.status !== "CANCELLED"
                ? `<button class="btn btn-sm btn-danger cancel-pnr">Cancel</button>`
                : ""
            }</td>
          `;
        tbody.appendChild(tr);
      });

      myReservations.hidden = false;
    } else {
      myReservations.hidden = true;
    }
  });

  // Cancel PNR from reservation list (demo)
  document
    .querySelector("#reservationsTable tbody")
    .addEventListener("click", function (e) {
      if (e.target && e.target.matches(".cancel-pnr")) {
        const confirmed = confirm(
          "Are you sure you want to cancel this booking?"
        );
        if (confirmed) {
          // TODO: call API to cancel
          e.target.closest("tr").querySelector("td:nth-child(8)").textContent =
            "CANCELLED";
          e.target.remove();
        }
      }
    });

  // Cancel via quick form
  cancelForm.addEventListener("submit", function (e) {
    e.preventDefault();
    const pnr = document.getElementById("pnr").value.trim();
    if (!pnr) {
      cancelMsg.textContent = "Please provide a PNR.";
      return;
    }
    cancelMsg.textContent = "Processing cancellation...";

    // Demo: pretend success after 1s
    setTimeout(() => {
      cancelMsg.textContent = `PNR ${pnr} cancelled successfully.`;
      cancelForm.reset();
    }, 1000);
  });

  // Booking form submit (demo)
  bookingForm.addEventListener("submit", function (e) {
    // let the form submit to server in real app; here we demo
    e.preventDefault();
    const payload = Object.fromEntries(new FormData(bookingForm).entries());
    // Example: send fetch POST to /api/reservations/book with JSON
    console.log("Booking payload", payload);
    alert(
      "Booking confirmed (demo). In production, form will be submitted to server."
    );
    closeBooking();
  });
})();
