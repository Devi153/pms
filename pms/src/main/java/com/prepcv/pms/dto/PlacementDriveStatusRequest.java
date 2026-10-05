package com.prepcv.pms.dto;

import com.prepcv.pms.entity.DriveStatus;
import jakarta.validation.constraints.NotNull;

public class PlacementDriveStatusRequest {

    @NotNull(message = "Status is required")
    private DriveStatus status;

    public PlacementDriveStatusRequest() {
    }

    public DriveStatus getStatus() {
        return status;
    }

    public void setStatus(DriveStatus status) {
        this.status = status;
    }
}