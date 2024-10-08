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

  // 初始化範圍條的值 (從 Thymeleaf 渲染的 input 初始值取得)
  let initialVal = $("#threshold").val();
  document.getElementById("valBox").innerHTML = initialVal;

  // 使<input type="range">的數值即時顯示在頁面上
  function showVal(newVal) {
    document.getElementById("valBox").innerHTML = newVal;
  }

  // 即時更新範圍條數值
  $("#threshold").on("input", function () {
    showVal(this.value); // 每次範圍條變動都會更新顯示值
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
                                  name="planTitles"
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
                                  name="planUnitPrices"
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
                                  name="planTotalAmounts"
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
                                  name="planBuyAmounts"
                                  placeholder="0"
                                  class="form-control w-25 text-end"
                                  disabled
                                />
                                <input type="hidden" name="planBuyAmounts" value="0" />
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">圖片</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="file"
                                  name="planImages"
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
                                  name="planContents"
                                  maxlength="50"
                                  class="form-control bg-light-subtle w-75"
                                ></textarea>
                              </td>
                            </tr>
                          </tbody>
                        </table>`;
  }

  // 一鍵輸入：募資活動頁面
  // document
  //   .getElementById("autofillBtnProj")
  //   .addEventListener("click", function () {
  //     document.getElementById("title").value = "test";
  //     document.getElementById("category").value = "6";
  //     document.getElementById("tag").value = "4";
  //     document.getElementById("targetAmount").value = "50000";
  //     document.getElementById("currentAmount").value = "0";
  //     document.getElementById("threshold").value = "0.5";
  //     document.getElementById("image").value = "test.jpg";
  //     document.getElementById("description").value = "test測試測試";
  //   });

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
