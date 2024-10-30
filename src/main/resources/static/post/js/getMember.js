function currentMember() {
	return new Promise((resolve, reject) => {
		const token = Auth.getToken(); // 確保這裡獲取到有效的 Token
		if (!token || !token.includes('.')) {
			console.error('會員未登入,無效的 JWT Token');
			return; // 可以選擇提前返回或處理未授權情況
		}
		axios.get(`/TickitEasy/admin/api/post/member`, {
			headers: {
				'Authorization': `Bearer ${Auth.getToken()}`
			}
		})
			.then(response => {

				currentUserID = response.data;
				console.log("currentMember當前會員ID:" + currentUserID);
				resolve(currentUserID);  // 成功取得 userID 後 resolve
			}).catch(error => {
				// 檢查錯誤的狀態碼
				if (error.response) {
					reject(error);  // 若發生錯誤則 reject
					if (error.response.status === 401 || error.response.status === 403) {
						console.warn('會員未登入，無法獲取當前會員ID。');
						currentUserID = null; // 設定預設值
					} else {
						console.error('發生未知錯誤:', error.response.status);
					}
				} else {
					console.error('無法獲取會員資訊:', error.message);
				}

			});
	})
}