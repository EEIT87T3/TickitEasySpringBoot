package com.eeit87t3.tickiteasy.member.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.member.controller.MemberController;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.entity.Member.MemberStatus;
import com.eeit87t3.tickiteasy.member.repository.MemberRepository;
import com.eeit87t3.tickiteasy.util.JWTUtil;
import com.eeit87t3.tickiteasy.util.OAuthLoginRequest;

import jakarta.transaction.Transactional;

@Service
public class MemberService {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTUtil jwtUtil;

    // 會員註冊
    @Transactional
    public String register(String email, String password, String name, String nickname, LocalDate birthDate, String phone) {
        if (memberRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("該電子郵件已被註冊");
        }

        // 生成驗證 token
        String verificationToken = UUID.randomUUID().toString();

        // 加密密碼
        String encodedPassword = passwordEncoder.encode(password);
        
        // 創建新會員
        Member newMember = new Member(email, encodedPassword, name, nickname, birthDate, phone, LocalDate.now(), Member.MemberStatus.未驗證, null);
        newMember.setVerificationToken(verificationToken);

        memberRepository.save(newMember);

        return verificationToken;
    }

    // 處理會員驗證
    @Transactional
    public void verifyMember(String token) {
        Member member = memberRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("無效的驗證連結。"));

        if (member.getStatus() == Member.MemberStatus.已驗證) {
            throw new IllegalArgumentException("此帳號已經過驗證。");
        }

        // 更新會員狀態為已驗證，並清除token
        member.setStatus(Member.MemberStatus.已驗證);
        member.setVerificationToken(null);  // 清除 token
        memberRepository.save(member);
    }
    
 // 處理會員登入，驗證會員憑證並生成 JWT Token
    @Transactional
    public Optional<String> login(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        
        // 檢查會員是否存在
        if (member == null) {
            throw new IllegalArgumentException("該會員帳號不存在"); // 直接拋出例外
        }

        // 檢查會員是否完成驗證
        if (member.getStatus() != Member.MemberStatus.已驗證) {
            throw new IllegalArgumentException("會員尚未驗證，請先完成驗證程序");
        }

        // 比對密碼
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("密碼錯誤");  // 拋出異常
        }

        // 生成 JWT Token
        String token = jwtUtil.generateToken(member.getEmail());
        
        return Optional.of(token);  // 登入成功，返回 JWT Token
    }

 // 根據email找會員
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    // 更新會員基本資料，不包括圖片
    @Transactional
    public void updateProfile(Member currentMember, Member updatedInfo) {
        currentMember.setName(updatedInfo.getName());
        currentMember.setNickname(updatedInfo.getNickname());
        currentMember.setPhone(updatedInfo.getPhone());
        currentMember.setBirthDate(updatedInfo.getBirthDate());

        memberRepository.save(currentMember);  // 保存更新後的會員資料
    }

    // 上傳會員頭像
    @Transactional
    public void uploadProfilePic(Integer memberId, MultipartFile file) throws IllegalStateException, IOException {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String baseName = UUID.randomUUID().toString();
            String imagePath = imageUtil.saveImage(ImageDirectory.MEMBER, file, baseName);
            member.setProfilePic(imagePath);  // 更新會員的頭像路徑
            memberRepository.save(member);
            System.out.println("圖片已更新，儲存路徑為：" + member.getProfilePic());  // 確認路徑
        } else {
            throw new IllegalArgumentException("會員不存在");
        }
    }

    //密碼變更
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        Member member = findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("會員不存在");
        }
        
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("當前密碼不正確");
        }
        
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }
    
    //生成忘記密碼Token
    @Transactional
    public String generatePasswordResetToken(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("找不到該電子信箱的帳號");
        }

        String token = UUID.randomUUID().toString();
        member.setResetPasswordToken(token);
        member.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(24)); // Token 有效期 24 小時
        memberRepository.save(member);
        
        return token;
    }
    //忘記密碼重設
    @Transactional
    public void resetPassword(String token, String newPassword) {
        logger.info("開始處理密碼重設請求");
        
        if (token == null || token.trim().isEmpty()) {
            logger.error("重設密碼token為空");
            throw new IllegalArgumentException("無效的重設密碼連結");
        }

        Member member = memberRepository.findByResetPasswordToken(token)
            .orElseThrow(() -> {
                logger.error("找不到對應的重設密碼token: {}", token);
                return new IllegalArgumentException("無效的重設密碼連結");
            });

        if (member.getResetPasswordTokenExpiry() == null) {
            logger.error("重設密碼token過期時間為空");
            throw new IllegalArgumentException("無效的重設密碼連結");
        }

        if (member.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            logger.error("重設密碼token已過期");
            throw new IllegalArgumentException("重設密碼連結已過期，請重新申請");
        }

        try {
            // 更新密碼
            member.setPassword(passwordEncoder.encode(newPassword));
            // 清除重設密碼相關資訊
            member.setResetPasswordToken(null);
            member.setResetPasswordTokenExpiry(null);
            
            memberRepository.save(member);
            logger.info("密碼重設成功，會員ID: {}", member.getMemberID());
        } catch (Exception e) {
            logger.error("密碼重設過程中發生錯誤: ", e);
            throw new IllegalStateException("密碼重設失敗，請稍後再試");
        }
    }
 // 添加一個方法來檢查token的有效性
    public boolean isValidResetToken(String token) {
        logger.info("檢查重設密碼token的有效性: {}", token);
        
        try {
            Optional<Member> memberOpt = memberRepository.findByResetPasswordToken(token);
            if (memberOpt.isEmpty()) {
                logger.warn("找不到對應的重設密碼token");
                return false;
            }
            
            Member member = memberOpt.get();
            if (member.getResetPasswordTokenExpiry() == null || 
                member.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
                logger.warn("重設密碼token已過期");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            logger.error("檢查重設密碼token時發生錯誤: ", e);
            return false;
        }
    }
    
 // 第三方登入，僅保留 Google 登入邏輯
    @Transactional
    public Optional<String> oauthLogin(OAuthLoginRequest request) {
        Member member;
        
        // 根據提供者ID查找會員
        if ("google".equals(request.getProvider())) {
            member = findByGoogleId(request.getProviderId());
        } else {
            throw new IllegalArgumentException("不支援的登入提供者");
        }

        // 如果會員不存在，創建新會員
        if (member == null) {
            member = createOAuthMember(request);
        }

        // 生成 JWT Token
        String token = jwtUtil.generateToken(member.getEmail());
        return Optional.of(token);
    }

    private Member createOAuthMember(OAuthLoginRequest request) {
        // 檢查信箱是否已被使用
        Member existingMember = findByEmail(request.getEmail());
        if (existingMember != null) {
            return existingMember;
        }

        // 創建新會員
        Member newMember = new Member();
        newMember.setEmail(request.getEmail());
        newMember.setName(request.getName());
        newMember.setNickname(request.getName());
        newMember.setRegisterDate(LocalDate.now());
        newMember.setStatus(Member.MemberStatus.已驗證);  // OAuth登入的用戶直接設為已驗證

        newMember.setGoogleId(request.getProviderId());  // 設置 Google ID
        return memberRepository.save(newMember);
    }

    // 根據 Google ID 查找會員
    public Member findByGoogleId(String googleId) {
        return memberRepository.findByGoogleId(googleId);
    }
    
    /**************後台*********************/

    // 取得所有會員列表
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 根據會員ID取得會員資料
    public Optional<Member> getMemberById(Integer memberId) {
        return memberRepository.findById(memberId);
    }

   
    // 讀取頭貼
    public byte[] getProfilePic(Integer memberId) throws IOException {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String profilePicPath = member.getProfilePicPath();
            return imageUtil.getImageByteArray(profilePicPath);
        }
        return imageUtil.getImageByteArray("/images/member/default-avatar.png");
    }
    
   //獲取頭貼檔案名稱
    public String getProfilePicFilename(Integer memberId) {
    	Optional<Member> optionalMember = memberRepository.findById(memberId);
    	if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			String profilePicPath = member.getProfilePicPath();
			return profilePicPath.substring(profilePicPath.lastIndexOf("/") + 1);
		}
    	return "default-avatar.png";
    }

    // 移除會員頭貼
    @Transactional
    public void removeProfilePic(Integer memberId) throws IOException {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String profilePic = member.getProfilePic();
            if (profilePic != null && !profilePic.isEmpty()) {
                imageUtil.deleteImage(profilePic);
            }
            member.setProfilePic(null);
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("會員不存在");
        }
    }

    // 更改會員狀態
    @Transactional
    public void updateMemberStatus(Integer memberId, Member.MemberStatus status) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setStatus(status);
            memberRepository.save(member);
        }
    }

    // 查詢最近六個月內註冊的會員
    public List<Member> getRecentRegisteredMembers(LocalDate sixMonthsAgo) {
        return memberRepository.findByRegisterDateAfter(sixMonthsAgo);
    }

    // 查詢某個年齡範圍內的會員
    public List<Member> getMembersByAgeRange(LocalDate startDate, LocalDate endDate) {
        return memberRepository.findByBirthDateBetween(startDate, endDate);
    }
    //統計卡片
    public Map<String, Long> getMemberStats() {
        Map<String, Long> stats = new HashMap<>();
        
        // 獲取今日註冊人數
        LocalDate today = LocalDate.now();
        long todayRegistrations = memberRepository.countByRegisterDate(today);
        
        // 獲取本週註冊人數
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        long weekRegistrations = memberRepository.countByRegisterDateBetween(weekStart, today);
        
        // 獲取總會員數
        long totalMembers = memberRepository.count();
        
        // 獲取活躍會員數（已驗證的會員）
        long activeMembers = memberRepository.countByStatus(Member.MemberStatus.已驗證);
        
        stats.put("todayRegistrations", todayRegistrations);
        stats.put("weekRegistrations", weekRegistrations);
        stats.put("totalMembers", totalMembers);
        stats.put("activeMembers", activeMembers);
        
        return stats;
    }
}
