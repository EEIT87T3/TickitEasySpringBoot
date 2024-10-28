$(document).ready(function () {
    loadProductDetail();
    loadRecommendProducts();  // 加載推薦商品
	});

function loadProductDetail() {
    axios.get(`/TickitEasy/user/api/product/${productID}`)
        .then(response => {
            const product = response.data;
            renderProductDetail(product);
        })
        .catch(error => {
            console.error('Error loading product details:', error);
            document.getElementById('productDetail').innerHTML = '<p class="text-danger">商品加載失敗，請稍後再試。</p>';
        });
}

function renderProductDetail(product) {
    const template = document.getElementById('productDetailTemplate').content.cloneNode(true);

    // 設定主圖
    const mainImage = template.getElementById('mainImage');
    mainImage.src = `/TickitEasy${product.productPic}`;
    mainImage.alt = product.productName;

    // 設定縮略圖
    const thumbnailsContainer = template.getElementById('thumbnails');
    const mainThumbnail = document.createElement('img');
    mainThumbnail.src = `/TickitEasy${product.productPic}`;
    mainThumbnail.alt = product.productName;
    mainThumbnail.classList.add('thumbnail-image');
    mainThumbnail.onclick = () => changeMainImage(mainThumbnail.src);
    thumbnailsContainer.appendChild(mainThumbnail);

    // 添加其他副圖
    product.detailPhotos.forEach(photo => {
        const img = document.createElement('img');
        img.src = `/TickitEasy${photo}`;
        img.alt = product.productName;
        img.classList.add('thumbnail-image');
        img.onclick = () => changeMainImage(img.src);
        thumbnailsContainer.appendChild(img);
    });

    // 設定商品信息
    template.getElementById('productName').textContent = product.productName;
    template.getElementById('productPrice').textContent = product.price;
    template.getElementById('productStock').textContent = product.stock;
    template.getElementById('productDesc').textContent = product.productDesc;

    // 顯示相應的按鈕
    if (product.status === 2) {
        template.getElementById('notificationButtonContainer').style.display = 'block'; // 顯示補貨通知按鈕
		template.getElementById('cartButtonsContainer').style.display = 'none';         // 隱藏購物車按鈕
		template.getElementById('productStocknone').style.display = 'none';         // 隱藏庫存
		template.getElementById('quantityInputnone').style.display = 'none';         // 隱藏數量輸入框
    } else {
		template.getElementById('notificationButtonContainer').style.display = 'none';  // 隱藏補貨通知按鈕
        template.getElementById('cartButtonsContainer').style.display = 'block';			// 顯示購物車按鈕
		   }
    

    document.getElementById('productDetail').appendChild(template);
}

function changeMainImage(src) {
    document.getElementById('mainImage').src = src;
}

// 推薦商品載入函數
function loadRecommendProducts() {
    axios.get(`/TickitEasy/user/api/product/${productID}/recommend`)
        .then(response => {
            const products = response.data;
            const container = document.getElementById('recommendedProductsContainer');
            container.innerHTML = '';  // 清空現有內容

            if (!products || products.length === 0) {
                container.innerHTML = '<div class="col-12 text-center">暫無推薦商品</div>';
                return;
            }

            products.forEach(product => {
                const template = document.getElementById('recommendedProductTemplate').content.cloneNode(true);
                template.querySelector('.recommended-img').src = `/TickitEasy${product.productPic}`;
                template.querySelector('.recommended-img').alt = product.productName;
                template.querySelector('.recommended-name').textContent = product.productName;
                template.querySelector('.recommended-price').textContent = product.price;
                template.querySelector('a').href = `/TickitEasy/product/${product.productID}`;
                container.appendChild(template);
            });
        })
        .catch(error => {
            console.error('Error loading recommended products:', error);
            document.getElementById('recommendedProductsContainer').innerHTML = '<div class="col-12 text-center text-danger">載入推薦商品時發生錯誤</div>';
        });
}

// 增加商品數量
function increaseQuantity() {
    const quantityInput = document.getElementById('quantityInput');
    let quantity = parseInt(quantityInput.value, 10);
    quantityInput.value = quantity + 1;
}

// 減少商品數量
function decreaseQuantity() {
    const quantityInput = document.getElementById('quantityInput');
    let quantity = parseInt(quantityInput.value, 10);
    if (quantity > 1) {
        quantityInput.value = quantity - 1;
    }
}

// 修改商品數量
function updateQuantity(value) {
    const quantityInput = document.getElementById('quantityInput');
    if (value < 1) {
        quantityInput.value = 1;
    }
}

// 補貨通知
function setNotification() {
    axios.post(`/TickitEasy/user/api/product/notify/${productID}`, {}, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('jwtToken')}` }
    })
    .then(response => {
        alert(response.data.message || '設定補貨通知成功！');
    })
    .catch(error => {
        if (error.response && error.response.status === 401) {
            alert('請先登入會員');
            window.location.href = '/TickitEasy/login';
        } else {
            alert('設定失敗，請稍後再試');
        }
    });
}

// 加入購物車
function addToCart(buyNow) {
    const quantity = parseInt(document.getElementById('quantityInput').value, 10);
    if (isNaN(quantity) || quantity < 1) {
        alert('請輸入大於0的數量');
        return;
    }

    // 獲取商品ID及其他詳細資訊
    const productID = window.productID;
    const productName = document.getElementById('productName').textContent;
    const productPrice = parseInt(document.getElementById('productPrice').textContent, 10);
    const productPic = document.getElementById('mainImage').src;

    // 創建購物車項目
    const cartItem = {
        productID: productID,
        productName: productName,
        price: productPrice,
        quantity: quantity,
        productPic: productPic
    };

    // 從 localStorage 中取得現有購物車數據
    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    // 檢查商品是否已存在於購物車中
    const existingItemIndex = cart.findIndex(item => item.productID === cartItem.productID);

    if (existingItemIndex > -1) {
        // 更新數量
        cart[existingItemIndex].quantity += quantity;
    } else {
        // 新增至購物車
        cart.push(cartItem);
    }

    // 將更新後的購物車數據存回 localStorage
    localStorage.setItem('cart', JSON.stringify(cart));

    // 決定跳轉至購物車頁面還是顯示提示信息
    if (buyNow) {
        window.location.href = '/TickitEasy/user/cart';
    } else {
        alert('商品已加入購物車');
    }
}

// 收藏功能
function toggleFavorite() {
    axios.post(`/TickitEasy/user/api/product/favorite/${productID}`, {}, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('jwtToken')}` }
    })
    .then(response => alert(response.data.message))
    .catch(error => {
        if (error.response && error.response.status === 401) {
            alert('請先登入會員');
            window.location.href = '/TickitEasy/login';
        } else {
            alert('操作失敗，請稍後再試');
        }
    });
}
