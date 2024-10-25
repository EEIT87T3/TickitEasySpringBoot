
    // 動態填充分類和標籤選項
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
            window.location.href = `/TickitEasy/user/post/PostList`;
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

      const formData = new FormData();

      console.log($('#fullPostCategory').val());

      // 使用formData取代json傳遞資料
      formData.append('postTitle', $('#fullPostTitle').val());
      formData.append('postContent', $('#fullPostContent').val());
      formData.append('categoryID', $('#fullPostCategory').val());
      formData.append('tagID', $('#fullPostTag').val());

      // 附加所有已上傳的檔案
      uploadedImages.forEach(file => {
        formData.append('images', file); // 將所有未被刪除的檔案一起上傳
      });


      axios.post('/TickitEasy/admin/api/post/POST/', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          if (response.data && response.data.postID) {  // 確認回應包含 postID
            const postID = response.data.postID;  // 抓取生成的 postID
            alert("貼文新增成功！");
            const currentUrl = window.location.href;
            if (currentUrl.includes("/admin/")) {  
                window.location.href = `/TickitEasy/admin/post/${postID}`; 
            } else { 
                window.location.href = `/TickitEasy/user/post/${postID}`;
            }
            
          } else {
            alert("新增成功，但無法取得貼文 ID。");
          }
        })
        .catch(error => {
          console.error("新增貼文失敗:", error);
          alert("新增失敗，請稍後再試。");
        });


    });