package com.prepcv.pms.controller;

import com.prepcv.pms.dto.AdminDashboardResponse;
import com.prepcv.pms.dto.StudentDashboardResponse;
import com.prepcv.pms.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<AdminDashboardResponse>
    getAdminDashboard() {

        return ResponseEntity.ok(
                dashboardService.getAdminDashboard()
        );
    }

    @GetMapping("/student/dashboard")
    public ResponseEntity<StudentDashboardResponse>
    getStudentDashboard(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                dashboardService.getStudentDashboard(email)
        );
    }
}