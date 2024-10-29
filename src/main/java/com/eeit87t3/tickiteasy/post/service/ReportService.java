package com.eeit87t3.tickiteasy.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
