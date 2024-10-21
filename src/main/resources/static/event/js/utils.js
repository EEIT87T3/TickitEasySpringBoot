
// 取得「localhost:8080/TickitEasy」
const contextPath = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split("/")[1];

// 取得目前的完整網址
const currentPath = window.location.protocol + "//" + window.location.host + window.location.pathname;

// 將後端傳來的 ISO 日期時間字串轉換為方便閱讀的日期時間字串。
function dateFormat(datetimeString) {
    const dateObject = new Date(datetimeString);
    const options = {
        year: 'numeric',
        month: 'long', // 使用 'long' 會顯示完整的月份名稱
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    };
    return dateObject.toLocaleString('zh-TW', options);
}