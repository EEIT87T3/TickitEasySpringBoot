let currentUserID ;
if (!Auth.isLoggedIn()) {
  // 如果未登入，重定向到登入頁面
  Auth.logout(); 
} else {
// 動態填充分類和標籤選項
currentMember() ;
axios.get('/TickitEasy/admin/api/post/categories')
  .then(response => {
    const categories = response.data;
    categories.forEach(category => {
      $('#fullPostCategory').append(new Option(category.categoryName, category.categoryId));
    });
  });

axios.get('/TickitEasy/admin/api/post/tags')
  .then(response => {
    const tags = response.data;
    tags.forEach(tag => {
      $('#fullPostTag').append(new Option(tag.tagName, tag.tagId));
    });
  });

$('.postList').on('click', function () {
  const currentUrl = window.location.href;
  if (currentUrl.includes("/admin/")) {
    window.location.href = `/TickitEasy/admin/post`;
  } else {
    window.location.href = `/TickitEasy/post/PostList`;
  }
});

//上傳圖片預覽
const imageInput = document.getElementById('formFileMultiple');
const imagePreview = document.getElementById('imagePreview');
const uploadedImages = []; // 用來存儲已上傳的圖片資訊

imageInput.addEventListener('change', function () {
  // imagePreview.innerHTML = '';  // 清空上次的圖片預覽
  const files = imageInput.files;

  //加入多張圖片並驗證檔案是否重複
  for (let i = 0; i < files.length; i++) {
    if (!uploadedImages.some(img => img.name === files[i].name)) {
      const fileName = files[i].name;


      // 將檔案資訊加入陣列
      uploadedImages.push(files[i]);


      // 創建顯示檔名和圖片的容器
      const imageItem = document.createElement('div');
      imageItem.classList.add('image-item');

      // 創建預覽圖像元素
      const imgElement = document.createElement('img');
      imgElement.src = URL.createObjectURL(files[i]);

      imgElement.style.width = '100px';
      imgElement.style.marginRight = '10px';


      // 創建顯示檔名的元素
      const fileNameElement = document.createElement('span');
      fileNameElement.textContent = fileName;


      // 創建刪除按鈕
      const deleteBtn = document.createElement('span');
      deleteBtn.innerHTML = '<i class="fa-solid fa-x"></i>';
      deleteBtn.classList.add('delete-btn');
      deleteBtn.dataset.index = i; // 儲存索引以便後續查找
      deleteBtn.addEventListener('click', function () {
        // 從預覽中刪除該項
        const index = parseInt(deleteBtn.dataset.index); // 獲取索引
        imagePreview.removeChild(imageItem);

        if (index > -1) {
          uploadedImages.splice(index, 1);

        }
      });



      // 將圖片和檔名添加到容器
      imageItem.appendChild(imgElement);
      imageItem.appendChild(fileNameElement);
      imageItem.appendChild(deleteBtn);

      // 將容器添加到預覽區域
      imagePreview.appendChild(imageItem);
      ;
    }
  }
});

$('#fullCreatePostForm').on('submit', function (event) {
  event.preventDefault();
  const memberID =currentUserID;
  const formData = new FormData();
  const button = event.target;
    button.disabled = true;
    console.log('按鈕禁用')
    
   
  console.log($('#fullPostCategory').val());

  // 使用formData取代json傳遞資料
  formData.append('postTitle', $('#fullPostTitle').val());
  formData.append('postContent', $('#fullPostContent').val());
  formData.append('categoryID', $('#fullPostCategory').val());
  formData.append('tagID', $('#fullPostTag').val());
  formData.append('memberID', memberID);

  // 附加所有已上傳的檔案
  uploadedImages.forEach(file => {
    formData.append('images', file); // 將所有未被刪除的檔案一起上傳
  });


  axios.post('/TickitEasy/admin/api/post/POST/', formData, {
    headers: {
      'Authorization': `Bearer ${Auth.getToken()}`,
      'Content-Type': 'multipart/form-data'
    }
  })
    .then(response => {
      if (response.data && response.data.postID) {  // 確認回應包含 postID
        const postID = response.data.postID;  // 抓取生成的 postID
        swal("貼文新增成功！", "", "success");
        setTimeout(() => {
          // 上傳完成後再重新啟用按鈕
          button.disabled = false; console.log('按鈕啟用')
        }, 100);
        const currentUrl = window.location.href;
        if (currentUrl.includes("/admin/")) {
          setTimeout(() => {
          window.location.href = `/TickitEasy/admin/post/${postID}`;
        }, 2000);
        } else {
          setTimeout(() => {
          window.location.href = `/TickitEasy/post/${postID}`;
        }, 2000);
        }

      } else {
        // alert("新增成功，但無法取得貼文 ID。");
        swal("無法取得貼文ID", "請稍後再試", "error");
      }
    })
    .catch(error => {
      console.error("新增貼文失敗:", error);
      // alert("新增失敗，請稍後再試。");
      swal("新增失敗！", "請稍後再試", "error");
      if (error.response && error.response.status === 401) {
        Auth.logout(); // 如果錯誤會移除token並自動定向到登入頁面
      }
    });


});
}