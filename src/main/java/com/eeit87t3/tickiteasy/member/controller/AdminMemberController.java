package com.eeit87t3.tickiteasy.member.controller;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminMemberController {

    @Autowired
    private MemberService memberService;

    // 會員列表頁面
    @GetMapping("/member")
    public String memberList() {
        return "admin/memberList"; // 返回會員列表頁面
    }

    // 會員圖表分析頁面
    @GetMapping("/member/analytics")
    public String memberAnalytics() {
        return "admin/memberAnalytics"; // 返回會員分析頁面
    }

    // 取得所有會員的 JSON 資料
    @GetMapping("/member/api")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/member/api/age-distribution")
    public ResponseEntity<Map<String, Long>> getAgeDistribution() {
        List<Member> members = memberService.getAllMembers();
        // 根據生日計算年齡，並分組統計
        Map<String, Long> ageDistribution = members.stream().collect(Collectors.groupingBy(
            member -> calculateAgeRange(member.getBirthDate()), Collectors.counting()
        ));
        return ResponseEntity.ok(ageDistribution);
    }

    @GetMapping("/member/api/registration-trend")
    public ResponseEntity<Map<String, Long>> getRegistrationTrend() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<Member> recentMembers = memberService.getRecentRegisteredMembers(sixMonthsAgo);
        // 按月份分組並統計
        Map<String, Long> registrationTrend = recentMembers.stream().collect(Collectors.groupingBy(
            member -> member.getRegisterDate().getMonth().toString(), Collectors.counting()
        ));
        return ResponseEntity.ok(registrationTrend);
    }


    // 更改會員狀態
    @PutMapping("/member/api/{memberId}/status")
    public ResponseEntity<String> updateMemberStatus(@PathVariable Integer memberId, @RequestBody Map<String, String> request) {
        Optional<Member> member = memberService.getMemberById(memberId);
        if (member.isPresent()) {
            String newStatus = request.get("status");
            memberService.updateMemberStatus(memberId, Member.MemberStatus.valueOf(newStatus));
            return ResponseEntity.ok("會員狀態已更新");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 移除會員頭貼
    @DeleteMapping("/member/api/{memberId}/profile-pic")
    public ResponseEntity<String> removeProfilePic(@PathVariable Integer memberId) {
        try {
            memberService.removeProfilePic(memberId);
            return ResponseEntity.ok("會員頭貼已移除");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("移除頭貼失敗");
        }
    }

 // 匯出會員資料為 CSV
    @GetMapping("/member/api/export/csv")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportCsv() throws IOException {
        List<Member> members = memberService.getAllMembers();

        StringBuilder csvBuilder = new StringBuilder();
        // 標題
        csvBuilder.append("ID,名稱,暱稱,電子郵件,電話,註冊日期,狀態\n");
        for (Member member : members) {
            csvBuilder.append(member.getMemberID()).append(",");
            csvBuilder.append(escapeCsv(member.getName())).append(",");
            csvBuilder.append(escapeCsv(member.getNickname())).append(",");
            csvBuilder.append(escapeCsv(member.getEmail())).append(",");
            csvBuilder.append(escapeCsv(member.getPhone())).append(",");
            csvBuilder.append(member.getRegisterDate()).append(",");
            csvBuilder.append(member.getStatus()).append("\n");
        }

        byte[] csvData = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(csvData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=members.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvData.length)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    // 匯出會員資料為 Excel
    @GetMapping("/member/api/export/excel")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportExcel() throws IOException {
        List<Member> members = memberService.getAllMembers();

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Members");

        // 標題
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "名稱", "暱稱", "電子郵件", "電話", "註冊日期", "狀態"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // 數據
        int rowNum = 1;
        for (Member member : members) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(member.getMemberID());
            row.createCell(1).setCellValue(member.getName());
            row.createCell(2).setCellValue(member.getNickname());
            row.createCell(3).setCellValue(member.getEmail());
            row.createCell(4).setCellValue(member.getPhone());
            row.createCell(5).setCellValue(member.getRegisterDate().toString());
            row.createCell(6).setCellValue(member.getStatus().toString());
        }

        // 自動調整列寬
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 將 Workbook 轉換為 ByteArray
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        byte[] excelData = out.toByteArray();
        ByteArrayResource resource = new ByteArrayResource(excelData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=members.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelData.length)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

 // 輔助方法：計算年齡範圍
    private String calculateAgeRange(LocalDate birthDate) {
        int age = LocalDate.now().getYear() - birthDate.getYear();
        if (age < 20) {
            return "20歲以下";
        } else if (age < 30) {
            return "20-29";
        } else if (age < 40) {
            return "30-39";
        } else if (age < 50) {
            return "40-49";
        } else {
            return "50 and above";
        }
    }

    // CSV 資料轉義，以處理逗號和引號
    private String escapeCsv(String data) {
        if (data == null) {
            return "";
        }
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            data = data.replace("\"", "\"\"");
            data = "\"" + data + "\"";
        }
        return data;
    }
    
    
}