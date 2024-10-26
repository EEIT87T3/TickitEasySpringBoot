// /static/js/recaptcha.js
const RecaptchaUtil = {
    // 從 data 屬性獲取 site key
    getSiteKey() {
        const metaElement = document.querySelector('meta[name="recaptcha-site-key"]');
        if (!metaElement) {
            throw new Error('RecaptchaUtil: Site key not found');
        }
        return metaElement.getAttribute('content');
    },
    
    // 獲取 reCAPTCHA token
    async getToken(action) {
        try {
            const siteKey = this.getSiteKey();
            return await grecaptcha.execute(siteKey, { action: action });
        } catch (error) {
            console.error('reCAPTCHA error:', error);
            throw error;
        }
    },
    
    // 為表單添加 reCAPTCHA 驗證
    async addRecaptchaToken(data, action) {
        try {
            const token = await this.getToken(action);
            return { ...data, recaptchaToken: token };
        } catch (error) {
            console.error('Failed to get reCAPTCHA token:', error);
            Swal.fire('錯誤', '驗證失敗，請稍後再試', 'error');
            throw error;
        }
    }
};