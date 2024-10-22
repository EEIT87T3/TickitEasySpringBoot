$(document).ready(function () {
  const planTitle = document
    .getElementById("planTitle")
    .getAttribute("data-plan-title");
  const planID = document
    .getElementById("planTitle")
    .getAttribute("data-plan-id");
  const projectID = document
    .getElementById("fundProject")
    .getAttribute("data-project-id");

  //監聽加碼欄位，同時變動加碼金額&總金額
  const bonusInput = document.getElementById("bonusInput");
  const bonusResult = document.getElementById("bonusResult");
  const unitPrice = document
    .getElementById("unitPrice")
    .getAttribute("data-value");
  const unitPriceValue = parseInt(unitPrice);
  const totalAmount = document.getElementById("totalAmount");
  let bonusValue = 0;
  let totalAmountValue = 0;

  //初始化總金額
  updateTotalAmount();

  //監聽加碼欄位，同時變動加碼金額&總金額
  bonusInput.addEventListener("input", function (e) {
    e.preventDefault();
    bonusValue = bonusInput.value;

    if (bonusValue === "") {
      bonusValue = 0;
    }
    bonusValue = parseInt(bonusValue); // 確保是數字類型
    bonusResult.value = bonusValue;
    bonusResult.textContent = `+ NT$ ${bonusValue.toLocaleString()}元`;

    updateTotalAmount();
  });

  // 更新總金額
  function updateTotalAmount() {
    totalAmountValue = unitPriceValue + parseInt(bonusValue);
    totalAmount.value = totalAmountValue;
    totalAmount.textContent = ` NT$ ${totalAmountValue.toLocaleString()}元`;
  }

  const linePayButton = document.getElementById("linePayButton");
  linePayButton.addEventListener("click", function (e) {
    e.preventDefault();
    requestLinePay();
  });

  //一鍵填入btn

  document
    .getElementById("autofillBtn")
    .addEventListener("click", function (e) {
      e.preventDefault();
      document.getElementById("email").value = "demo@example.com";
      document.getElementById("phone").value = "0912345678";
      document.getElementById("city").value = "3";
      document.getElementById("district").value = "3";
      document.getElementById("district").value = "3";
      document.getElementById("addressDetail").value = "新生路二段 421 號";
    });

  function requestLinePay() {
    axios
      .post("http://localhost:8080/TickitEasy/api/linepay/request", {
        projectID: projectID,
        planID: planID,
        amount: totalAmountValue,
        currency: "TWD",
        packages: [
          {
            id: "1",
            amount: unitPriceValue,
            products: [
              {
                id: planID,
                name: planTitle,
                quantity: 1,
                price: unitPriceValue,
              },
            ],
          },
          {
            id: "2",
            amount: bonusValue,
            products: [
              {
                id: 999,
                name: "加碼金額",
                quantity: 1,
                price: bonusValue,
              },
            ],
          },
        ],
      })
      .then(function (response) {
        const transactionId = response.data.info.transactionId;
        const paymentUrl = response.data.info.paymentUrl.web;
        console.log(response.data);
        console.log("TransactionID:", transactionId);
        console.log("paymentUrl", paymentUrl);
        // 將 transactionId 附加到 confirmUrl 中
        window.location.href = paymentUrl;
        // return axios.post(
        //   `http://localhost:8080/TickitEasy/api/linepay/confirm/${transactionID}`
        // )
      });
    //   .then(function (response) {
    //     const paymentConfirm = response.data;
    //     console.log(paymentConfirm);
    //   });
  }
});
