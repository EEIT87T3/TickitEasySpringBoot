package com.eeit87t3.tickiteasy.member.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.entity.Member.MemberStatus;
import com.eeit87t3.tickiteasy.member.repository.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 會員註冊
    @Transactional
    public void register(String email, String password, String name, String nickname, LocalDate birthDate, String phone) {
        if (memberRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("該電子郵件已被註冊");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Member newMember = new Member(email, encodedPassword, name, nickname, birthDate, phone, LocalDate.now(), MemberStatus.未驗證, null);
        memberRepository.save(newMember);
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


    // 處理會員登入
    public Optional<Member> login(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return Optional.of(member);
        }
        return Optional.empty();
    }

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
}
