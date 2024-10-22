// ========== 購物車行為 ==========

// 加入購物車
function ticketTypesCartAdd(ticketType, quantity) {
    const ticketTypesCart = JSON.parse(localStorage.getItem("ticketTypesCart"));
    if (ticketTypesCart[ticketType.ticketTypeID]) {  // 此票種已經存在於購物車內
        ticketTypesCartChange(ticketType.ticketTypeID, quantity);
    } else {
        ticketTypesCart[ticketType.ticketTypeID] = {
            ticketTypeID: ticketType.ticketTypeID,
            eventPic: ticketType.event.eventPic,
            eventName: ticketType.event.eventName,
            typeName: ticketType.typeName,
            price: ticketType.price,
            quantity: quantity,
            quantityAvailable: 100,  // 暫時寫死
            totalPrice: ticketType.price * quantity
        };
    }
    localStorage.setItem("ticketTypesCart", JSON.stringify(ticketTypesCart))
}

// 自購物車移除
function ticketTypesCartRemove(ticketTypeID) {
    const ticketTypesCart = JSON.parse(localStorage.getItem("ticketTypesCart"));
    delete ticketTypesCart[ticketTypeID];
    localStorage.setItem("ticketTypesCart", JSON.stringify(ticketTypesCart));
}

// 增加或減少數量：已確保已經存在於購物車內
function ticketTypesCartChange(ticketTypeID, quantity) {
    const ticketTypesCart = JSON.parse(localStorage.getItem("ticketTypesCart"));
    if (quantity > 0) {  // 增加
        ticketTypesCart[ticketTypeID].quantity += quantity;
        ticketTypesCart[ticketTypeID].totalPrice = ticketTypesCart[ticketTypeID].quantity * ticketTypesCart[ticketTypeID].price;  // 重新計算總價
    } else if (quantity < 0) {  // 減少
        if (ticketTypesCart[ticketTypeID].quantity > Math.abs(quantity)) {  // 減少後仍有剩餘
            ticketTypesCart[ticketTypeID].quantity -= Math.abs(quantity);
            ticketTypesCart[ticketTypeID].totalPrice = ticketTypesCart[ticketTypeID].quantity * ticketTypesCart[ticketTypeID].price;  // 重新計算總價
        }
    }
    localStorage.setItem("ticketTypesCart", JSON.stringify(ticketTypesCart))
}


// ========== 總數計算 ==========

// 計算總數量
function ticketTypesCartTotalQuantity() {
    let totalQuantity = 0;
    Object.values(JSON.parse(localStorage.getItem("ticketTypesCart"))).forEach((tickettypeCartItem) => {
        totalQuantity += tickettypeCartItem.quantity;
    });
    return totalQuantity;
}
// 計算總金額
function ticketTypesCartTotalAmount() {
    let totalAmount = 0;
    Object.values(JSON.parse(localStorage.getItem("ticketTypesCart"))).forEach((tickettypeCartItem) => {
        totalAmount += tickettypeCartItem.totalPrice;
    });
    return totalAmount;
}


// ========== 初始執行 ==========
$(document).ready(function () {
    // 初始化 localStorage 內的 ticketTypesCart
    (() => {
        if (localStorage.getItem("ticketTypesCart") == null) {
            localStorage.setItem("ticketTypesCart", JSON.stringify({}));
        }
    })();






    // ========== 測試 ==========
    (() => {
        console.log("==========")

        console.log("ticketTypesCart");
        console.log(JSON.parse(localStorage.getItem("ticketTypesCart")));
        
    //     console.log("ticketTypesCartChange(1, 1)")
    //     ticketTypesCartChange(1, 1);
        
    //     console.log("ticketTypesCart");
    //     console.log(JSON.parse(localStorage.getItem("ticketTypesCart")));
        
    //     console.log("ticketTypesCartChange(1, -5)")
    //     ticketTypesCartChange(1, -5);

    //     console.log("ticketTypesCart");
    //     console.log(JSON.parse(localStorage.getItem("ticketTypesCart")));
    })();

    
    
    // const checkoutTicketTypeCart = [
    //     {
    //         ticketTypeID: 1,
    //         eventName: "活動01",
    //         ticketTypeName: "票種01",
    //         eventPic: "/image/event/1.jpg",
    //         quantity: 3,
    //         price: 100,
    //         totalPrice: 300
    //     },
    //     {
    //         ticketTypeID: 2,
    //         eventName: "活動01",
    //         ticketTypeName: "票種02",
    //         eventPic: "/image/event/1.jpg",
    //         quantity: 2,
    //         price: 120,
    //         totalPrice: 240
    //     },
    // ];
    
    // localStorage.setItem("ticketTypeCart", JSON.stringify(ticketTypeCart));




});