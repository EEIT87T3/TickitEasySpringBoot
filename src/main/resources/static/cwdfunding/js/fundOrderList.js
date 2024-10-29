$(document).ready(async function () {
  let memberID = await getMemberProfile();
  // 獲取會員資料
  async function getMemberProfile() {
    if (!Auth.isLoggedIn()) {
      Auth.logout();
      return;
    }
    try {
      const response = await axios.get(
        "http://localhost:8080/TickitEasy/member/api/fundproject/getMemberID",
        {
          headers: { Authorization: `Bearer ${Auth.getToken()}` },
        }
      );
      console.log("API 回應:", response.data);
      let rmemberID = response.data.memberID;
      return rmemberID;
    } catch (error) {
      console.error("獲取會員資料時發生錯誤:", error);
      return error;
    }
  }

  async function getFundOderByID(memberID) {
    try {
      const response = await axios.get("/TickitEasy/member/api/fundOrderList", {
        params: { memberID: memberID },
      });
      return response.data;
    } catch (error) {
      console.log(error);
      return null;
    }
  }

  async function htmlMaker(obj) {
    console.log("htmlMAker here");
    let output = document.getElementById("outputBlock");
    let pageBtnBlock = document.getElementById("pageBtnBlock");

    output.innerHTML = "";
    pageBtnBlock.innerHTML = "";

    console.log(obj);

    if (obj != null) {
      obj.forEach((el) => {
        const tr = document.createElement("tr");

        tr.innerHTML = `<td> <a href="/TickitEasy/user/fundOrderDetail/${
          el.tickitID
        }">${el.tickitID}</a></td>
         <td> ${el.orderDate} </td>
         <td> ${Number(el.bonus).toLocaleString()} </td>
         <td> ${Number(el.totalAmount).toLocaleString()} </td>`;
        output.append(tr);
      });
    } else {
      console.log("找不到訂單資訊！");
    }
  }

  let responseObj = await getFundOderByID(memberID);
  await htmlMaker(responseObj);
});
