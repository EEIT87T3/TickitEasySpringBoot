package com.eeit87t3.tickiteasy.util;

/**
 * @author Lilian (Curriane)
 */
//用來接收登入請求
public class JwtRequest {

 private String username;
 private String password;

 // getters and setters
 public String getUsername() {
     return username;
 }

 public void setUsername(String username) {
     this.username = username;
 }

 public String getPassword() {
     return password;
 }

 public void setPassword(String password) {
     this.password = password;
 }
}