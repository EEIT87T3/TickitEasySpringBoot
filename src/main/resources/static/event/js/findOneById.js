$(document).ready(function () {
    const eventID = window.location.pathname.substring(window.location.pathname.lastIndexOf('/') + 1);
    
    // 填充資訊
    function dataMaker(data) {
        const eventContent = document.getElementById("event-content-template").content.cloneNode(true);
        
        // ========== 活動 ==========
        const event = data.event;

        // 活動名稱
        // SSR 填充
        
        // 活動主圖 
        const eventPicImg = eventContent.getElementById("event-pic");
        eventPicImg.setAttribute("alt", event.eventName);
        if (event.eventPic != null) {
            eventPicImg.setAttribute("src", contextPath + event.eventPic);
        }

        // 活動介紹
        if (event.eventDesc != null) {
            eventContent.getElementById("event-desc").textContent = event.eventDesc;
        } else {
            eventContent.getElementById("event-desc").textContent = "（無活動介紹）";
        }




        // ========== 票種 ==========
        const ticketTypes = data.ticketTypes;

        if (ticketTypes.length == 0) {  // 無票種
            const noTicketTypeDiv = document.getElementById("no-tickettype-template").content.cloneNode(true);
            eventContent.getElementById("tickettype-content").append(noTicketTypeDiv);
        } else {
            ticketTypes.forEach(ticketType => {
                const ticketTypeDiv = document.getElementById("tickettype-template").content.cloneNode(true);
                
                // 票種名稱
                ticketTypeDiv.querySelector(".tickettype-name").textContent = ticketType.typeName;

                // 數量按鈕
                const quantityInput = ticketTypeDiv.querySelector(".quantity");
                ticketTypeDiv.querySelector(".plus.btn").addEventListener("click", () => {
                    const originalQuantity = Number(quantityInput.value);
                    const newQuantity = originalQuantity + 1;
                    quantityInput.value = newQuantity;
                });
                ticketTypeDiv.querySelector(".minus.btn").addEventListener("click", () => {
                    const originalQuantity = Number(quantityInput.value);
                    if (originalQuantity > 0) {  // 若數量已經為 0，不再減
                        const newQuantity = originalQuantity - 1;
                        quantityInput.value = newQuantity;
                    }
                });

                // 加入購物車
                ticketTypeDiv.querySelector(".add-to-cart").addEventListener("click", () => {
                    if (Number(quantityInput.value) > 0) {
                        ticketTypesCartAdd(ticketType, Number(quantityInput.value));
                        Swal.fire({
                            icon: "success",
                            html: `已將 <b>${ticketType.typeName}</b> ${Number(quantityInput.value)} 張 加入購物車！`,
                        });
                        quantityInput.value = 0;  // 重置為初始值
                    }
                });

                eventContent.getElementById("tickettype-content").append(ticketTypeDiv);
            });
        }




        // 放入 event-content
        document.getElementById("event-content").innerHTML = "";
        document.getElementById("event-content").append(eventContent);
    }


    // 初始執行
    axios.get(contextPath + "/api/event/" + eventID)
        .then(res => {
            console.log(res);
            dataMaker(res.data);
        })
        .catch(err => {
            console.error(err); 
        })
});