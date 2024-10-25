// memberProfile.js

// 獲取會員資料並更新個人資料表單
async function getMemberProfile() {
    console.log('開始獲取會員資料');
    if (!Auth.isLoggedIn()) {
        console.log('未登入，重定向到登入頁面');
        Auth.logout();
        return;
    }

    try {
        const response = await axios.get('/TickitEasy/api/member/profile', {
            headers: { 'Authorization': `Bearer ${Auth.getToken()}` }
        });
        console.log('API 回應:', response.data);
        updateProfileForm(response.data);
        loadMemberInfo(); // 觸發 memberInfo.js 更新 UI
    } catch (error) {
        console.error('獲取會員資料時發生錯誤:', error);
        handleApiError(error);
    }
}

// 更新個人資料表單的 UI
function updateProfileForm(memberData) {
    const elements = {
        email: document.getElementById('email'),
        name: document.getElementById('name'),
        nickname: document.getElementById('nickname'),
        birthDate: document.getElementById('birthDate'),
        phone: document.getElementById('phone')
    };

    // 檢查並更新表單字段
    for (const [key, element] of Object.entries(elements)) {
        if (element) {
            element.value = memberData[key] || '';
            console.log(`更新 ${key}: ${element.value}`);
        } else {
            console.warn(`找不到元素: ${key}`);
        }
    }
}

// 更新會員資料
async function updateProfile(formData) {
    console.log('開始更新會員資料:', formData);
    if (!Auth.isLoggedIn()) return;

    try {
        const response = await axios.put('/TickitEasy/api/member/profile', formData, {
            headers: { 'Authorization': `Bearer ${Auth.getToken()}` }
        });
        console.log('更新資料回應:', response.data);
        Swal.fire('成功', '資料更新成功', 'success');
        await getMemberProfile(); // 更新個人資料表單
        // loadMemberInfo() 已在 getMemberProfile() 中被呼叫
    } catch (error) {
        console.error('更新會員資料時發生錯誤:', error);
        handleApiError(error, '資料更新失敗');
    }
}

// 更新頭像
async function updateProfilePic(file) {
    console.log('開始更新頭像');
    if (!Auth.isLoggedIn()) return;

    const formData = new FormData();
    formData.append('profilePic', file);

    // 即時顯示選擇的圖片作為預覽
    const reader = new FileReader();
    reader.onload = function(e) {
        const profilePicDisplay = document.getElementById('profilePicDisplay');
        if (profilePicDisplay) {
            profilePicDisplay.src = e.target.result;
            console.log('即時預覽頭貼圖片');
        }

        const navMemberAvatar = document.getElementById('memberAvatar');
        if (navMemberAvatar) {
            navMemberAvatar.src = e.target.result;
            console.log('即時預覽導航欄頭貼圖片');
        }
    };
    reader.readAsDataURL(file);

    try {
        const response = await axios.post('/TickitEasy/api/member/profilePic', formData, {
            headers: {
                'Authorization': `Bearer ${Auth.getToken()}`,
                'Content-Type': 'multipart/form-data'
            }
        });
        console.log('更新頭貼回應:', response.data);
        Swal.fire('成功', '頭貼更新成功', 'success');
        await getMemberProfile(); // 更新個人資料表單並觸發 memberInfo.js 更新 UI
    } catch (error) {
        console.error('更新頭貼時發生錯誤:', error);
        handleApiError(error, '頭貼更新失敗');
    }
}

// 變更密碼
async function changePassword(currentPassword, newPassword, confirmPassword) {
    console.log('開始變更密碼');
    if (!Auth.isLoggedIn()) return;

    try {
        const response = await axios.post('/TickitEasy/api/member/change-password', {
            currentPassword,
            newPassword,
            confirmPassword
        }, {
            headers: { 'Authorization': `Bearer ${Auth.getToken()}` }
        });
        console.log('變更密碼回應:', response.data);
        Swal.fire('成功', '密碼已成功變更', 'success');

        // 清空密碼欄位
        document.getElementById('currentPassword').value = '';
        document.getElementById('newPassword').value = '';
        document.getElementById('confirmPassword').value = '';
        console.log('密碼欄位已清空');

        // 可選：重置整個表單
        const changePasswordForm = document.getElementById('changePasswordForm');
        if (changePasswordForm) {
            changePasswordForm.reset();
            console.log('密碼變更表單已重置');
        }
    } catch (error) {
        console.error('變更密碼時發生錯誤:', error);
        handleApiError(error, '密碼變更失敗');
    }
}

// 處理 API 錯誤
function handleApiError(error, defaultMessage = '操作失敗') {
    console.error('API 錯誤:', error);
    let errorMessage = defaultMessage;
    if (error.response) {
        if (error.response.status === 401) {
            console.log('Token 無效，清除並重定向到登入頁面');
            Auth.logout();
            return;
        }
        errorMessage = error.response.data.error || error.response.data.message || defaultMessage;
    }
    Swal.fire('錯誤', errorMessage, 'error');
}

// 當頁面加載完成時執行
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM 加載完成，開始初始化');

    if (!Auth.isLoggedIn()) {
        Auth.logout();
        return;
    }

    // 獲取並顯示會員資料
    getMemberProfile();

    // 設置個人資料表單提交事件
    const editProfileForm = document.getElementById('editProfileForm');
    if (editProfileForm) {
        editProfileForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = {
                name: document.getElementById('name').value,
                nickname: document.getElementById('nickname').value,
                birthDate: document.getElementById('birthDate').value,
                phone: document.getElementById('phone').value
            };
            updateProfile(formData);
        });
    } else {
        console.error('找不到 editProfileForm 元素');
    }

    // 設置頭像更新事件
    const profilePicInput = document.getElementById('profilePic');
    if (profilePicInput) {
        profilePicInput.addEventListener('change', function(e) {
            if (e.target.files.length > 0) {
                updateProfilePic(e.target.files[0]);
            }
        });
    } else {
        console.error('找不到 profilePic 元素');
    }

    // 設置密碼變更表單提交事件
    const changePasswordForm = document.getElementById('changePasswordForm');
    if (changePasswordForm) {
        changePasswordForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const currentPassword = document.getElementById('currentPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword !== confirmPassword) {
                Swal.fire('錯誤', '新密碼與確認密碼不符', 'error');
                return;
            }
            
            changePassword(currentPassword, newPassword, confirmPassword);
        });
    } else {
        console.error('找不到 changePasswordForm 元素');
    }
});
