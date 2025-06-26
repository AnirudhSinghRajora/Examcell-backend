package com.examcell.resultgen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examcell.resultgen.dto.DashboardDTO;
import com.examcell.resultgen.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard/statistics
     * Retrieves comprehensive statistics for the administrative dashboard.
     */
    @GetMapping("/statistics")
    public ResponseEntity<DashboardDTO> getDashboardStatistics() {
        DashboardDTO statistics = dashboardService.getDashboardStatistics();
        return ResponseEntity.ok(statistics);
    }
} 