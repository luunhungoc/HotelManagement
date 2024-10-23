document.addEventListener("DOMContentLoaded", function () {
    // Get the radio buttons and the pay button
    const vndRadio = document.getElementById("currency-vnd");
    const usdRadio = document.getElementById("currency-usd");
    const payButton = document.getElementById("pay-button");
    const payForm = document.getElementById("pay-form");

    // Function to update the button text and form action
    function updatePayButton() {
        if (vndRadio.checked) {
            payButton.textContent = "PAY IN VND";
            payForm.action = "/checkOut";
        } else if (usdRadio.checked) {
            payButton.textContent = "PAY IN USD";
            payForm.action = "/payment";
        }
    }

    // Event listeners for currency selection
    vndRadio.addEventListener("change", updatePayButton);
    usdRadio.addEventListener("change", updatePayButton);
});

document.addEventListener("DOMContentLoaded", function () {
    // Tỉ giá hối đoái (1 USD = 25,000 VND)
    const conversionRate = 25000;

    // Lấy các phần tử radio và các phần tử hiển thị
    const vndRadio = document.getElementById("currency-vnd");
    const usdRadio = document.getElementById("currency-usd");
    const totalPriceDisplay = document.getElementById("total-price");
    const payButton = document.getElementById("pay-button");
    const payForm = document.getElementById("pay-form");

    // Lấy giá trị totalAmount từ LocalStorage và hiển thị trong phần tử có id "total-price"
    const initialPriceDisplay = localStorage.getItem("totalAmount");

    // Nếu không có giá trị, gán giá trị mặc định
    if (!initialPriceDisplay) {
        console.error("No total amount found in localStorage.");
        return;
    }

    // Lưu số tiền ban đầu từ giá trị lấy từ LocalStorage (VND)
    let originalPriceInVND = parseInt(initialPriceDisplay.replace(/[^\d]/g, ''), 10);

    // Hàm định dạng số cho VND với dấu chấm làm dấu phân cách hàng nghìn
    function formatVND(number) {
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    }

    // Hàm định dạng số cho USD với dấu phẩy làm dấu phân cách thập phân
    function formatUSD(number) {
        return number.toString().replace(".", ",");
    }

    // Hàm cập nhật nút và tổng số tiền khi thay đổi tiền tệ
    function updatePayButtonAndPrice() {
        if (vndRadio.checked) {
            // Khi chọn VND
            totalPriceDisplay.textContent = `VND ${formatVND(originalPriceInVND)}`;
            payButton.textContent = "PAY IN VND";
            payForm.action = "/checkout";
        } else if (usdRadio.checked) {
            // Khi chọn USD, tính toán số tiền USD từ giá trị ban đầu
            const priceInUSD = (originalPriceInVND / conversionRate).toFixed(2);
            const formattedPriceInUSD = formatUSD(priceInUSD); // Định dạng lại số USD
            totalPriceDisplay.textContent = `USD ${formattedPriceInUSD}`;
            payButton.textContent = "PAY IN USD";
            payForm.action = "/payment";
        }
    }

    // Gắn sự kiện thay đổi cho radio buttons
    vndRadio.addEventListener("change", updatePayButtonAndPrice);
    usdRadio.addEventListener("change", updatePayButtonAndPrice);

    // Thiết lập giá trị mặc định khi trang được tải
    updatePayButtonAndPrice();
});
