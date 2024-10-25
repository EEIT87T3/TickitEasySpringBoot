// completeProfile.js

document.addEventListener('DOMContentLoaded', function() {
    if (!Auth.isLoggedIn()) {
        window.location.href = '/TickitEasy/login';
        return;
    }

    const form = document.getElementById('completeProfileForm');

    // 檢查資料狀態
    checkProfileStatus();

    // 表單提交處理
    form.addEventListener('submit', handleSubmit);
});

async function checkProfileStatus() {
    try {
        const response = await axios.get('/TickitEasy/api/member/profile/status', {
            headers: { 'Authorization': `Bearer ${Auth.getToken()}` }
        });

        if (response.data.status === 'success') {
            // 如果資料已經完整，重定向到個人資料頁
            if (!response.data.needsCompletion) {
                window.location.href = '/TickitEasy/profile';
            } else {
                // 顯示缺少的欄位
                highlightMissingFields(response.data.missingFields);
            }
        }
    } catch (error) {
        console.error('檢查資料狀態失敗:', error);
        handleError(error);
    }
}

function highlightMissingFields(missingFields) {
    if (!missingFields) return;

    Object.entries(missingFields).forEach(([field, isMissing]) => {
        if (isMissing) {
            const input = document.getElementById(field);
            if (input) {
                input.classList.add('border-danger');
            }
        }
    });
}

async function handleSubmit(e) {
    e.preventDefault();

    // 獲取表單數據
    const formData = {
        nickname: document.getElementById('nickname').value.trim(),
        name: document.getElementById('name').value.trim(),
        birthDate: document.getElementById('birthDate').value,
        phone: document.getElementById('phone').value.trim()
    };

    // 前端驗證
    if (!validateForm(formData)) {
        return;
    }

    try {
        const response = await axios.put('/TickitEasy/api/member/profile', formData, {
            headers: { 'Authorization': `Bearer ${Auth.getToken()}` }
        });

        if (response.data.message) {
            await Swal.fire({
                icon: 'success',
                title: '成功',
                text: '個人資料已更新完成！',
                confirmButtonText: '確定'
            });
            window.location.href = '/TickitEasy/profile';
        }
    } catch (error) {
        console.error('更新資料失敗:', error);
        handleError(error);
    }
}

function validateForm(data) {
    // 驗證暱稱
    if (!data.nickname) {
        Swal.fire('錯誤', '暱稱不得為空', 'error');
        return false;
    }

    // 驗證姓名
    if (!data.name) {
        Swal.fire('錯誤', '姓名不得為空', 'error');
        return false;
    }

    // 驗證生日
    if (!data.birthDate) {
        Swal.fire('錯誤', '請選擇生日', 'error');
        return false;
    }

    // 驗證手機號碼
    if (!data.phone.match(/^09\d{8}$/)) {
        Swal.fire('錯誤', '請輸入有效的手機號碼', 'error');
        return false;
    }

    return true;
}

function handleError(error) {
    let errorMessage = '發生錯誤，請稍後再試';
    if (error.response) {
        if (error.response.status === 401) {
            Auth.logout();
            return;
        }
        errorMessage = error.response.data.error || error.response.data.message || errorMessage;
    }
    Swal.fire('錯誤', errorMessage, 'error');
}