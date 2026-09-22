package com.mining.maintenance.model;

public class Asset {

    private int id;
    private String assetCode;
    private String assetName;
    private String category;
    private String type;
    private String brand;
    private String model;
    private String serialNumber;
    private double purchasePrice;
    private String status;
    private String location;
    private String purchaseDate;
    private double operatingHours;
    private String description;

    public Asset() {
    }

    public Asset(
            int id,
            String assetCode,
            String assetName,
            String category,
            String type,
            String brand,
            String model,
            String serialNumber,
            double purchasePrice,
            String status,
            String location,
            String purchaseDate,
            double operatingHours,
            String description) {

        this.id = id;
        this.assetCode = assetCode;
        this.assetName = assetName;
        this.category = category;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.purchasePrice = purchasePrice;
        this.status = status;
        this.location = location;
        this.purchaseDate = purchaseDate;
        this.operatingHours = operatingHours;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(double operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}