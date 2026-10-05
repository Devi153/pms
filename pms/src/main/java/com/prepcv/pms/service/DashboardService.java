package com.prepcv.pms.service;

import com.prepcv.pms.dto.AdminDashboardResponse;
import com.prepcv.pms.dto.StudentDashboardResponse;

public interface DashboardService {

    AdminDashboardResponse getAdminDashboard();

    StudentDashboardResponse getStudentDashboard(
            String email
    );
}