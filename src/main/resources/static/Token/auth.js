// auth.js
const Auth = {
    getToken: function() {
        return localStorage.getItem('jwtToken');
    },
    
    setToken: function(token) {
        localStorage.setItem('jwtToken', token);
    },
    
    removeToken: function() {
        localStorage.removeItem('jwtToken');
    },
    
    isLoggedIn: function() {
        return !!this.getToken();
    },
    
    logout: function() {
        this.removeToken();
        window.location.href = '/TickitEasy/login';
    }
};