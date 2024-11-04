package com.eeit87t3.tickiteasy.util;

/**
 * @author Lilian (Curriane)
 */
import com.eeit87t3.tickiteasy.member.entity.Member;

public class ProfileValidator {
    
    
     // 檢查會員資料是否完整
    public static boolean isProfileComplete(Member member) {
        return member != null 
            && hasValidPhone(member)
            && hasValidBirthDate(member)
            && hasValidName(member)
            && hasValidNickname(member);
    }
    
    //檢查是否需要完善資料
    public static boolean needsProfileCompletion(Member member) {
        boolean isOAuthUser = member.getGoogleId() != null;
        return isOAuthUser && !isProfileComplete(member);
    }
    
    // 檢查手機號碼
    private static boolean hasValidPhone(Member member) {
        return member.getPhone() != null 
            && !member.getPhone().trim().isEmpty()
            && member.getPhone().matches("^09\\d{8}$");
    }
    
    // 檢查生日
    private static boolean hasValidBirthDate(Member member) {
        return member.getBirthDate() != null;
    }
    
    // 檢查姓名
    private static boolean hasValidName(Member member) {
        return member.getName() != null 
            && !member.getName().trim().isEmpty();
    }
    
    // 檢查暱稱
    private static boolean hasValidNickname(Member member) {
        return member.getNickname() != null 
            && !member.getNickname().trim().isEmpty();
    }
}
