package com.mining.maintenance.model;

public class MaintenanceRecord {

    private int id;
    private int assetId;
    private String maintenanceType;
    private String maintenanceDate;
    private String description;
    private String technician;
    private double cost;
    private String status;

    public MaintenanceRecord() {
    }

    public MaintenanceRecord(
            int id,
            int assetId,
            String maintenanceType,
            String maintenanceDate,
            String description,
            String technician,
            double cost,
            String status) {

        this.id = id;
        this.assetId = assetId;
        this.maintenanceType = maintenanceType;
        this.maintenanceDate = maintenanceDate;
        this.description = description;
        this.technician = technician;
        this.cost = cost;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}