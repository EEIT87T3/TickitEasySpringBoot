$(document).ready(function () {
  var current_fs, next_fs, previous_fs; //fieldsets
  var opacity;
  var current = 1;
  var steps = $("fieldset").length;
  let selectedValue = 1; // 讀取選擇的值
  let output = document.getElementById("outputBlock");

  setProgressBar(current);

  $(".next").click(function () {
    current_fs = $(this).closest("fieldset"); // 獲取當前的 fieldset
    next_fs = current_fs.next(); // 獲取下一個 fieldset

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

  $("#planAmountSelect").change(function () {
    selectedValue = $(this).val();
    console.log(selectedValue);
  });

  $("#planAmount-btn").click(function () {
    let planTitle;
    output.innerHTML = "";
    console.log(selectedValue);
    planTitle = `《方案一》`;
    planTitleMaker(planTitle);
    planContentMaker();
    if (selectedValue == "2") {
      planTitle = `《方案二》`;
      planTitleMaker(planTitle);
      planContentMaker();
    }
  });

  function planTitleMaker(name) {
    output.innerHTML += `<div class="row">
                        <div class="col">
                        <p class="fw-bolder text-start p-3 fs-4">${name}</p>
                        </div></div>`;
  }

  function planContentMaker() {
    output.innerHTML += `  <table class="table table-borderless custom-table">
                          <tbody style="background-color: black">
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">方案名稱</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="text"
                                  name="title"
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
                                  name="targetAmount"
                                  class="form-control w-25 text-end"
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
                                  name="currentAmount"
                                  placeholder="0"
                                  class="form-control w-25 text-end"
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
                                  name="startDate"
                                  class="form-control w-25 text-end"
                                  placeholder="0"
                                  disabled
                                />
                              </td>
                            </tr>
                            <tr>
                              <td class="col-2">
                                <div class="fieldlabels fw-medium">圖片</div>
                              </td>
                              <td class="col-8">
                                <input
                                  type="file"
                                  name="postponeDate"
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
                                  name="description"
                                  maxlength="50"
                                  class="form-control bg-light-subtle w-75"
                                ></textarea>
                              </td>
                            </tr>
                          </tbody>
                        </table>`;
  }
});
