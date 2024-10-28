package com.eeit87t3.tickiteasy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ReCaptchaConfig {
    
	//從設定檔讀取 reCAPTCHA 的key
    @Value("${google.recaptcha.secret}")
    private String secretKey;
    
    @Value("${google.recaptcha.verify-url}")
    private String verifyUrl;
    
    // 用於發送 HTTP 請求到 Google reCAPTCHA API 的工具
    private final RestTemplate restTemplate;
    
    //初始化 RestTemplate
    public ReCaptchaConfig() {
        this.restTemplate = new RestTemplate();
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public String getVerifyUrl() {
        return verifyUrl;
    }
    
    // 驗證 reCAPTCHA 回應
    public boolean verifyRecaptcha(String recaptchaResponse) {
    	// 建構驗證 API 的參數，包含密鑰和使用者提交的 reCAPTCHA 回應
        String params = String.format("?secret=%s&response=%s", secretKey, recaptchaResponse);
        // 發送 HTTP 請求到 Google reCAPTCHA API 進行驗證並將 API 回應轉換成 ReCaptchaResponse 物件
        ReCaptchaResponse response = restTemplate.getForObject(verifyUrl + params, ReCaptchaResponse.class);
        // 如果 API 沒有回應，驗證失敗
        if (response == null) {
            return false;
        }
        // 檢查 API 回應是否成功、reCAPTCHA 分數是否>= 0.5
        return response.isSuccess() && response.getScore() >= 0.5;
    }
    // 用來表示 Google reCAPTCHA API 的回應
    private static class ReCaptchaResponse {
        private boolean success; // 驗證是否成功
        private float score; // reCAPTCHA 分數，介於 0.0 到 1.0 之間，分數越高表示越可能是真人
        private String action; // reCAPTCHA 執行的動作
        private String challenge_ts; // reCAPTCHA 挑戰的時間戳記
        private String hostname; // 執行 reCAPTCHA 的網站 hostname
        
        
        //用來存取 ReCaptchaResponse 的屬性
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public float getScore() {
            return score;
        }
        
        public void setScore(float score) {
            this.score = score;
        }
        
        public String getAction() {
            return action;
        }
        
        public void setAction(String action) {
            this.action = action;
        }
        
        public String getChallenge_ts() {
            return challenge_ts;
        }
        
        public void setChallenge_ts(String challenge_ts) {
            this.challenge_ts = challenge_ts;
        }
        
        public String getHostname() {
            return hostname;
        }
        
        public void setHostname(String hostname) {
            this.hostname = hostname;
        }
    }
}