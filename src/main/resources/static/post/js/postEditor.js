
    //上傳圖片預覽
    const imageInput = document.getElementById('formFileMultiple');
    const imagePreview = document.getElementById('imagePreview');
    const uploadedImages = []; // 用來存儲已上傳的圖片資訊

    imageInput.addEventListener('change', function () {
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
                    const index = parseInt(deleteBtn.dataset.index); // 獲取索引
                    imagePreview.removeChild(imageItem);

                    // 從 uploadedImages 陣列中移除該檔案
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
            }
        }
    });

    const postID = window.location.pathname.split("/")[4];

    console.log("Post ID:", postID); // 調試用
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
         // 判斷跳轉路徑
         const currentUrl = window.location.href;
         if (currentUrl.includes("/admin/")) {  
             window.location.href = `/TickitEasy/admin/post`; 
         } else { 
            window.location.href = `/TickitEasy/user/post/PostList`;
         }
        
    });

    // 當頁面加載時，取得要編輯的貼文內容並填充到表單
    async function fetchPost() {
        try {
            const response = await axios.get(`/TickitEasy/admin/api/post/GET/${postID}`);
            const post = response.data;
            document.getElementById('postTitle').value = post.postTitle;
            document.getElementById('postContent').value = post.postContent;
            document.getElementById('fullPostCategory').value = post.postCategory.categoryId;
            document.getElementById('fullPostTag').value = post.postTag.tagId;

            //圖片清單+刪除按鈕
            const postImagesHTML = (post.images || []).map(image => `
                <div class="image-container" style="position: relative; display: inline-block; margin-bottom: 10px;">
                    <img src="/TickitEasy/images/post/${image.imagePath.split('/').pop()}" alt="Post Image" style="width: 60%; height: auto;" />
                    <i class="fa-solid fa-x" style="position: absolute; top: 0; right: 0; cursor: pointer;" onclick="deleteImage(${image.imageID})"></i>
                </div>`).join('');
            document.getElementById('imagePreview').innerHTML = `
                <div class="post-images">${postImagesHTML}</div>
            `;
        } catch (error) {
            if (error.response) {
                document.getElementById("updateResult").innerText = `Failed to load post: ${error.response.status}`;
            } else {
                document.getElementById("updateResult").innerText = `Error: ${error.message}`;
            }
        }
    }

    // 更新貼文
    async function updatePost() {
        const title = document.getElementById("postTitle").value;
        const content = document.getElementById("postContent").value;
        const category = document.getElementById("fullPostCategory").value;
        const tag = document.getElementById("fullPostTag").value;

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
                    window.location.href = `/TickitEasy/user/post/${postID}`;
                }
                
            }, 1000); // 1秒後跳轉
        } catch (error) {
            if (error.response) {
                document.getElementById("updateResult").innerText = `Failed to update post: ${error.response.status}`;
            } else {
                document.getElementById("updateResult").innerText = `Error: ${error.message}`;
            }
        }
    }

    // 刪除圖片
    async function deleteImage(imageID) {
        try {
            const response = await axios.delete(`/TickitEasy/admin/api/post/images/delete/${imageID}`);
            if (response.status === 200) {
                document.getElementById("updateResult").innerText = "Image deleted successfully!";
                document.getElementById("updateResult").className = "success";
                // 重新載入圖片列表
                fetchPost();
            } else {
                document.getElementById("updateResult").innerText = `Failed to delete image: ${response.status}`;
            }
        } catch (error) {
            document.getElementById("updateResult").innerText = `Error: ${error.message}`;
        }
    }

    // 初始化載入
    fetchPost();
