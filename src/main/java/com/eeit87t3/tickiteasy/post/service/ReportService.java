package com.eeit87t3.tickiteasy.post.service;

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
}
