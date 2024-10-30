package com.eeit87t3.tickiteasy.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.entity.ReportEntity;
import com.eeit87t3.tickiteasy.post.repository.ReportRepo;

@Service
public class ReportService {
    @Autowired
    private ReportRepo reportRepo;

    public void save(ReportEntity report) {
        reportRepo.save(report);
    }

	public List<ReportEntity> findAllReports() {
		return reportRepo.findAll();
	}

	public ReportEntity findById(Integer reportID) {
	    return reportRepo.findById(reportID).orElse(null); // 返回 ReportEntity，若找不到則返回 null
	}
	 // 查詢與貼文相關的檢舉紀錄
    public List<ReportEntity> findByPost(PostEntity post) {
        return reportRepo.findByPostAndIsDeleted(post, false); // 過濾掉已刪除的檢舉紀錄
    }
}
