package com.eeit87t3.tickiteasy.member.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberID")
    private Integer memberID;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "nickname")
    private String nickname;
    
    @Column(name = "birthDate")
    private LocalDate birthDate;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "registerDate")
    private LocalDate registerDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status;
    
    @Column(name = "profilePic")
    private String profilePic;
    
    @Column(name = "verificationToken")
    private String verificationToken;
    
    @Column(name = "googleId")
    private String googleId;

    

	@Column(name = "facebookId")
    private String facebookId;

    
    // 無參構造函數
    public Member() {
    }

    // 帶參數的構造函數
    public Member(String email, String password, String name, String nickname, LocalDate birthDate, String phone, LocalDate registerDate, MemberStatus status, String profilePic) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.phone = phone;
        this.registerDate = registerDate;
        this.status = status;
        this.profilePic = profilePic;
    }

    // Getter 和 Setter 方法
    public String getGoogleId() {
    	return googleId;
    }
    
    public void setGoogleId(String googleId) {
    	this.googleId = googleId;
    }
    
    public String getFacebookId() {
    	return facebookId;
    }
    
    public void setFacebookId(String facebookId) {
    	this.facebookId = facebookId;
    }
    
    public Integer getMemberID() {
        return memberID;
    }

    public void setMemberID(Integer memberID) {
        this.memberID = memberID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    // 自定義方法，檢查是否有自訂頭貼，若無則返回預設頭貼
    public String getProfilePicPath() {
        return (profilePic != null && !profilePic.isEmpty()) ? profilePic : "/images/member/default-avatar.png";
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberID=" + memberID +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthDate=" + birthDate +
                ", phone='" + phone + '\'' +
                ", registerDate=" + registerDate +
                ", status='" + status + '\'' +
                '}';
    }
    
    // Enum 狀態
    public enum MemberStatus {
        已驗證,
        未驗證,
        討論區停權
    }
}
