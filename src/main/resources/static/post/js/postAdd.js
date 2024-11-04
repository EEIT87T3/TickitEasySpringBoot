
$(document).ready(function () {

  $('#fullPostContent').summernote({
    placeholder: '',
    tabsize: 4,
    height: 200,
    toolbar: [
      ['style', ['bold', 'italic', 'underline']],
      ['font', ['strikethrough', 'superscript', 'subscript']],
      ['fontsize', ['fontsize']],
      ['color', ['color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['insert', ['link']],
      ['view', ['fullscreen', 'help']]
    ],
    callbacks: {
      onChange: function () {//如果輸入內容則清除無填入錯誤提示
        const content = $('#fullPostContent').summernote('code').trim();
        if (content) {
          $('#contentError').html(''); 
          $('.note-editor').removeClass('input-error'); 
        }
      }
    }
  });
  //如果輸入內容則清除無填入錯誤提示
  $('#fullPostTitle').on('input', function () {
    const title = $(this).val().trim();
    if (title) {
      $('#titleError').html('');
      $('#fullPostTitle').removeClass('input-error');
    }
  });

  if (!Auth.isLoggedIn()) {
    // 如果未登入，重定向到登入頁面
    Auth.logout();
  } else {
    // 動態填充分類和標籤選項
    // currentMember();
    // console.log("當前會員ID:" + currentUserID);
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



    $(document).on('click', '#submitButton', function () {

      event.preventDefault();
      currentMember().then(() => {
        const memberID = currentUserID;
        console.log("#submitButton當前會員ID:" + memberID);
        const formData = new FormData();
        // const button = event.submitter;
        // button.disabled = true;

        // 清除所有錯誤提示
        $('.error-message').empty();
        $('#fullPostTitle').removeClass('input-error');
        $('.note-editor').removeClass('input-error');

        // 獲取標題和內容
        const title = $('#fullPostTitle').val().trim();
        const content = $('#fullPostContent').summernote('code').trim();
        const contentText = content.replace(/<[^>]*>/g, '').trim();

 
        let isValid = true;

        // 驗證標題
        if (!title) {
          $('#titleError').html('標題不能為空！');
          $('#fullPostTitle').addClass('input-error');
          isValid = false;
        }

        // 驗證內容
        if (!contentText) {
          $('#contentError').html('內容不能為空！');
          $('.note-editor').addClass('input-error');
          isValid = false;
        }

        if (!isValid) {
          // button.disabled = false;
          // 滾動到第一個錯誤
          const firstError = $('.error-message').filter(function () {
            return $(this).html() !== '';
          }).first();
          if (firstError.length) {
            $('html, body').animate({
              scrollTop: firstError.offset().top - 100
            }, 500);
          }
          return;
        }

        // 準備表單數據
        formData.append('postTitle', title);
        formData.append('postContent', content);
        formData.append('categoryID', $('#fullPostCategory').val());
        formData.append('tagID', $('#fullPostTag').val());
        formData.append('memberID', memberID);

        // 添加圖片
        uploadedImages.forEach(file => {
          formData.append('images', file);
        });

        // 發送請求
        axios.post('/TickitEasy/admin/api/post/POST/', formData, {
          headers: {
            'Authorization': `Bearer ${Auth.getToken()}`,
            'Content-Type': 'multipart/form-data'
          }
        })
          .then(response => {
            if (response.data && response.data.postID) {
              swal("貼文新增成功！", "", "success");
              setTimeout(() => {
                // button.disabled = false;
                const currentUrl = window.location.href;
                if (currentUrl.includes("/admin/")) {
                  window.location.href = `/TickitEasy/admin/post/${response.data.postID}`;
                } else {
                  window.location.href = `/TickitEasy/post/${response.data.postID}`;
                }
              }, 2000);
            } else {
              swal("無法取得貼文ID", "請稍後再試", "error");
              // button.disabled = false;
            }
          })
          .catch(error => {
            console.error("新增貼文失敗:", error);
            swal("新增失敗！", "請稍後再試", "error");
            // button.disabled = false;
            if (error.response && error.response.status === 401) {
              Auth.logout();
            }
          });
      })
    })
  }
});

$(document).ready(function () {
    $(document).on('click', '#saveButton', async function (event) {
        event.preventDefault(); // 防止表單默認提交
        console.log("updatePost called");

        // 禁用按鈕，避免重複點擊
        const button = document.getElementById("saveButton");
        button.disabled = true;

        const title = document.getElementById("postTitle").value;
        const content = $('#postContent').summernote('code'); // 使用 Summernote 獲取內容
        const category = document.getElementById("fullPostCategory").value;
        const tag = document.getElementById("fullPostTag").value;

        // 清除所有錯誤提示
        $('.error-message').empty();
        $('#fullPostTitle').removeClass('input-error');
        $('.note-editor').removeClass('input-error');

        // 獲取內容
        const contentText = content.replace(/<[^>]*>/g, '').trim();

        let isValid = true;

        // 驗證標題
        if (!title) {
            $('#titleError').html('標題不能為空！');
            $('#fullPostTitle').addClass('input-error');
            isValid = false;
        }

        // 驗證內容
        if (!contentText) {
            $('#contentError').html('內容不能為空！');
            $('.note-editor').addClass('input-error');
            isValid = false;
        }

        // 如果驗證失敗，重新啟用按鈕並退出
        if (!isValid) {
            button.disabled = false; // 重新啟用按鈕
            return; // 不執行後續的提交邏輯
        }

        // 生成表單數據
        const formData = new FormData();
        formData.append('postTitle', title);
        formData.append('postContent', content);
        formData.append('postCategory.categoryId', category);
        formData.append('postTag.tagId', tag);
        uploadedImages.forEach(file => {
            formData.append('images', file); // 將所有未被刪除的檔案一起上傳
        });

        try {
            const response = await axios.put(`/TickitEasy/admin/api/post/PUT/${postID}`, formData, {
                headers: {
                    'Authorization': `Bearer ${Auth.getToken()}`,
                    'Content-Type': 'multipart/form-data' // 設置內容類型
                }
            });

            document.getElementById("updateResult").innerText = "Post updated successfully!";
            document.getElementById("updateResult").className = "success";

            setTimeout(() => {
                const currentUrl = window.location.href;
                if (currentUrl.includes("/admin/")) {
                    window.location.href = `/TickitEasy/admin/post/${postID}`;
                } else {
                    window.location.href = `/TickitEasy/post/${postID}`;
                }
                button.disabled = false; // 在重定向之前重新啟用按鈕
            }, 100);
        } catch (error) {
            if (error.response) {
                document.getElementById("updateResult").innerText = `Failed to update post: ${error.response.status}`;
            } else {
                document.getElementById("updateResult").innerText = `Error: ${error.message}`;
            }
            button.disabled = false; // 在發生錯誤時重新啟用按鈕
        }
    });
});

