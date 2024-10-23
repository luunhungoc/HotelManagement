document.addEventListener('DOMContentLoaded', function () {
    const checkInDate = document.getElementById('checkInDate');
    const checkOutDate = document.getElementById('checkOutDate');
    // Get today's date in YYYY-MM-DD format
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // Months are 0-based
    const day = String(today.getDate()).padStart(2, '0');
    const todayString = `${year}-${month}-${day}`;

    // Set the min attribute to today's date
    checkInDate.setAttribute('min', todayString);

     // Add event listener for check-in date change
    checkInDate.addEventListener('change', function () {
    const selectedDate = new Date(checkInDate.value);
    selectedDate.setDate(selectedDate.getDate() + 1); // Set check-out to one day after check-in
    checkOutDate.value = selectedDate.toISOString().split('T')[0]; // Update check-out date
    checkOutDate.setAttribute('min', checkInDate.value); // Update min for check-out date
    });
});