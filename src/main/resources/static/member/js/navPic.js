// navPic.js

// 獲取會員資料並更新頭像與名稱
async function loadMemberInfo() {
    console.log('開始獲取會員頭像與名稱');
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
        updateMemberInfoUI(response.data);
    } catch (error) {
        console.error('獲取會員資料時發生錯誤:', error);
        // 處理 API 錯誤，例如 401 未授權
        if (error.response && error.response.status === 401) {
            Auth.logout();
        } else {
            Swal.fire('錯誤', '無法獲取會員資料', 'error');
        }
    }
}

// 更新 UI 顯示會員頭像和名稱
function updateMemberInfoUI(memberData) {
    // 更新導航欄的會員名字和頭像
    const navMemberName = document.getElementById('memberName');
    const navMemberAvatar = document.getElementById('memberAvatar');
    
    if (navMemberName) {
        navMemberName.textContent = memberData.nickname || memberData.name || '會員';
        console.log(`導航欄名稱更新為: ${navMemberName.textContent}`);
    } else {
        console.error('未能找到 navMemberName 元素');
    }
    
    if (navMemberAvatar) {
        navMemberAvatar.src = memberData.profilePic
            ? `/TickitEasy/api/member/profilePic/${memberData.memberID}?t=${new Date().getTime()}`
            : '/TickitEasy/images/member/default-avatar.png';
        console.log(`導航欄頭像更新為: ${navMemberAvatar.src}`);
    } else {
        console.error('未能找到 navMemberAvatar 元素');
    }

    // 更新側邊欄的會員名字和頭像
    const sidebarMemberName = document.getElementById('memberName1');
    const sidebarProfilePic = document.getElementById('profilePicDisplay');
    
    if (sidebarMemberName) {
        sidebarMemberName.textContent = memberData.nickname || memberData.name || '會員';
        console.log(`側邊欄名稱更新為: ${sidebarMemberName.textContent}`);
    } else {
        console.error('未能找到 sidebarMemberName 元素');
    }
    
    if (sidebarProfilePic) {
        sidebarProfilePic.src = memberData.profilePic
            ? `/TickitEasy/api/member/profilePic/${memberData.memberID}?t=${new Date().getTime()}`
            : '/TickitEasy/images/member/default-avatar.png';
        console.log(`側邊欄頭像更新為: ${sidebarProfilePic.src}`);
    } else {
        console.error('未能找到 sidebarProfilePic 元素');
    }
}

// 當頁面加載完成時執行
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM 加載完成，開始獲取會員資料');
    loadMemberInfo();
});
