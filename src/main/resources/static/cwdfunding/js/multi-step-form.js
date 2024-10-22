$(document).ready(function () {
  var current_fs, next_fs, previous_fs; //fieldsets
  var opacity;
  var current = 1;
  var steps = $("fieldset").length;
  let selectedValue = 1; // 讀取選擇的值
  let output = document.getElementById("outputBlock");
  let resultOutput = document.getElementById("resultBlock");
  const submitBtn = document.getElementById("submit-btn");

  // 換頁 [next] [previos] 按鈕邏輯
  setProgressBar(current);

  $(".next").click(function () {
    current_fs = $(this).closest("fieldset");
    next_fs = current_fs.next();

    console.log(current_fs);
    console.log(next_fs);

    //Add Class Active
    $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

    //show the next fieldset
    next_fs.show();
    //hide the current fieldset with style
    current_fs.animate(
      { opacity: 0 },
      {
        step: function (now) {
          // for making fielset appear animation
          opacity = 1 - now;

          current_fs.css({
            display: "none",
            position: "relative",
          });
          next_fs.css({ opacity: opacity });
        },
        duration: 500,
      }
    );
    setProgressBar(++current);
  });

  $(".previous").click(function () {
    current_fs = $(this).closest("fieldset");
    previous_fs = current_fs.prev(); // 獲取前一個 fieldset

    //Remove class active
    $("#progressbar li")
      .eq($("fieldset").index(current_fs))
      .removeClass("active");

    //show the previous fieldset
    previous_fs.show();

    //hide the current fieldset with style
    current_fs.animate(
      { opacity: 0 },
      {
        step: function (now) {
          // for making fielset appear animation
          opacity = 1 - now;

          current_fs.css({
            display: "none",
            position: "relative",
          });
          previous_fs.css({ opacity: opacity });
        },
        duration: 500,
      }
    );
    setProgressBar(--current);
  });

  function setProgressBar(curStep) {
    var percent = parseFloat(100 / steps) * curStep;
    percent = percent.toFixed();
    $(".progress-bar").css("width", percent + "%");
  }

  $(".submit").click(function () {
    return false;
  });

  // 步驟3: 填方案內容
  $("#planAmountSelect").change(function () {
    selectedValue = $(this).val();
    console.log(selectedValue);
  });

  $("#planAmount-btn").click(function () {
    let planTitle;
    output.innerHTML = "";
    console.log(selectedValue);
    for (let i = 0; i < selectedValue; i++) {
      planContentMaker(i);
    }
  });

  function planContentMaker(i) {
    output.innerHTML += `<div class="row">
                        <div class="col">
                        <p class="fw-bolder text-start p-3 fs-4">《方案 ${
                          i + 1
                        }》</p>
                        </div></div>
                        <table class="table table-borderless custom-table">
                          <tbody style="background-color: black">
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">方案名稱</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="text"
                                  id="planTitles"
                                  name="fundplanList[${i}].planTitle"
                                  id="title"
                                  class="form-control borderhidden bg-light-subtle text-start m-0 p-0 w-50"
                                />
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">單價</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="number"
                                  id="planUnitPrices"
                                  name="fundplanList[${i}].planUnitPrice"
                                  class="form-control w-25 text-end"
                                  min="0"
                                />
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">總數量</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="number"
                                  id="planTotalAmounts"
                                  name="fundplanList[${i}].planTotalAmount"
                                  placeholder="0"
                                  class="form-control w-25 text-end"
                                  min="0"
                                />
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">已購數量</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="number"
                                  id="planBuyAmounts"
                                  placeholder="0"
                                  class="form-control w-25 text-end"
                                  disabled
                                />
                                <input type="hidden" name="fundplanList[${i}].planBuyAmount" value="0" />
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">圖片</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="file"
                                  name="fundplanList[${i}].planImageFile"
                                  class="form-control w-50"
                                />
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">內容</div>
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2" colspan="2">
                                <textarea
                                  rows="2"
                                  id="planContents"
                                  name="fundplanList[${i}].planContent"
                                  maxlength="50"
                                  class="form-control bg-light-subtle w-75"
                                ></textarea>
                              </td>
                            </tr>
                          </tbody>
                        </table>`;
  }

  // 一鍵輸入：募資活動頁面
  const now = new Date();

  const month = now.getMonth() + 1; // 月份是从0开始的，所以要加1
  const day = now.getDate();
  const hours = now.getHours();
  const minutes = now.getMinutes();
  const seconds = now.getSeconds();
  const titleWithTime = `test${month}${day}${hours}${minutes}${seconds}`;
  document
    .getElementById("autofillBtnProj")
    .addEventListener("click", function (e) {
      e.preventDefault();
      document.getElementById("title").value = titleWithTime;
      document.getElementById("categoryId").value = "6";
      document.getElementById("tagId").value = "4";
      document.getElementById("targetAmount").value = "50000";
      document.getElementById("currentAmount").value = "0";
      document.getElementById("description").value = "test測試測試";
      document.getElementById("startDate").value = "2024-06-13T01:00";
      document.getElementById("endDate").value = "2024-11-08T16:30";
      // document.getElementById("image").src =
      //   "/TickitEasy/images/cwdfunding/demo.jpg";
    });
  // 一鍵輸入：募資活動頁面
  document
    .getElementById("autofillBtnPlan")
    .addEventListener("click", function (e) {
      e.preventDefault();
      document.getElementById("planTitles").value = "planAA";
      document.getElementById("planUnitPrices").value = "1000";
      document.getElementById("planTotalAmounts").value = "200";
      document.getElementById("planBuyAmounts").value = "0";
      document.getElementById("planContents").value = "test內容";
    });
  // 監聽送出按鈕
  submitBtn.addEventListener("click", function () {
    submitForm();
  });

  function spinner() {
    resultOutput.innerHTML = "";
    resultOutput.innerHTML = `<div class="d-flex m-5 justify-content-center">
       <div class="spinner-border" role="status">
         <span class="visually-hidden">Loading...</span>
       </div>
     </div>`;
  }

  // 使用ajax送出表單
  function submitForm() {
    // 創建一個 FormData 物件，將表單資料自動打包
    const form = document.getElementById("msform");
    const formData = new FormData(form);
    // 將所有input的value取出，加進json object

    spinner();
    // 使用 Axios 發送 POST 請求
    axios
      .post(
        "http://localhost:8080/TickitEasy/admin/api/fundproject",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      )
      .then(function (response) {
        const data = response.data;
        console.log(data);
        resultOutput.innerHTML = "";
        if (data.status === "success") {
          resultOutput.innerHTML = `<div class="d-flex m-5 justify-content-center">
                                        ${data.message}
                                    </div>`;
        } else {
          resultOutput.innerHTML = `<div class="d-flex m-5 justify-content-center">
                                        ${data.message}
                                    </div>`;
        }
      })
      .catch((error) => {
        resultOutput.innerHTML = `${error}`;
      });
  }
});
