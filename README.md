![TickitEasy](./images/logo/logoWithText.svg)
### TickitEasy：SpringBoot 開發版本  
EEIT87 第三組 專題

## 專案簡介

### 會員
- 前台
    - 註冊、登入、登出
    - Google第三方登入
    - 忘記密碼
    - 會員個人資料修改
    - JavaMail寄發驗證信
    - 正規表達式驗證
    - reCAPTCHA驗證
    - Dialogflow聊天機器人
- 後台
    - 會員頭像移除
    - 會員狀態修改
    - 會員統計圖表（Chart.js）

### 活動
- 前台
    - 活動查詢
        - 多條件動態查詢
    - Google Map 可互動地圖
    - 將活動票券加入購物車
- 後台
    - 活動查詢
        - 多條件動態查詢
    - 活動管理
        - 新增活動、修改資訊
            - summernote 所見即所得編輯
            - 編輯狀態（未上架／已上架／已啟售）
        - 票種管理

### 周邊商品
- 前台
    - 周邊商品⾴⾯
    - 周邊商品推薦
    - 周邊商品購物⾞
    - 周邊商品收藏
    - JavaMail寄發商品上架通知
- 後台
    - 周邊商品列表顯⽰（AJAX）
    - 周邊商品資料管理
    - 周邊商品圖⽚管理（FilePond）
    - 商品統計圖表（Chart.js）

### 訂單
- 前台
    - 串接 ⾦流 綠界
    - 串接 ⾦流 LinePay
    - 查看會員訂單紀錄
    - 查看訂單記錄明細
    - JavaMail寄發訂單編號
- 後台
    - 模糊搜尋訂單（AJAX）
    - 訂單刪除
    - 訂單明細查看

### 討論區
- 前台
    - 類別分區貼⽂列表
    - 發表編輯刪除貼⽂/留⾔
    - 按讚與紀錄瀏覽次數
    - 我的貼⽂/按讚貼⽂收藏⾴
- 後台
    - 貼⽂查詢&刪除
    - 留⾔查詢&刪除

### 募資活動
- 前台
    - 募資進度（Chart.js）
    - 會員追蹤募資活動
    - 募資訂單管理
    - LINE pay API串接
    - 達⽬標⾦額 SpringBoot mail 寄信通知
- 後台
    - 募資活動 CRUD
    - 募資列表顯⽰ （AJAX）
    - 分⾴ （Pageable）

## 開發用資訊

### Java 文件

https://eeit87t3.github.io/TickitEasySpringBoot/
<br>

### 測試網址

1. 資料庫連線  
http://localhost:8080/TickitEasy/test/connection  

2. 後台模板  
http://localhost:8080/TickitEasy/test/admin-template  

3. 圖片讀取  
http://localhost:8080/TickitEasy/images/test/logo.png