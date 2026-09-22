package com.mining.maintenance.controller;

import com.mining.maintenance.model.MaintenanceRecord;
import com.mining.maintenance.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceRestController {

    private final MaintenanceService maintenanceService;

    public MaintenanceRestController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public List<MaintenanceRecord> getAllMaintenance() {
        return maintenanceService.getAllMaintenance();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> getMaintenanceById(@PathVariable int id) {

        MaintenanceRecord record = maintenanceService.findMaintenanceById(id);

        if (record == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(record);
    }

    @GetMapping("/asset/{assetId}")
    public List<MaintenanceRecord> getMaintenanceByAsset(@PathVariable int assetId) {
        return maintenanceService.getMaintenanceByAssetId(assetId);
    }

    @PostMapping
    public ResponseEntity<?> createMaintenance(@RequestBody MaintenanceRecord maintenance) {

        String validationError = maintenanceService.validateMaintenance(maintenance);

        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }

        boolean saved = maintenanceService.addMaintenance(maintenance);

        if (!saved) {
            return ResponseEntity.badRequest().body("Failed to save maintenance record.");
        }

        return ResponseEntity.status(201).body(maintenance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaintenance(@PathVariable int id, @RequestBody MaintenanceRecord maintenance) {

        MaintenanceRecord existing = maintenanceService.findMaintenanceById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        maintenance.setId(id);

        String validationError = maintenanceService.validateMaintenance(maintenance);

        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }

        boolean updated = maintenanceService.updateMaintenance(maintenance);

        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update maintenance record.");
        }

        return ResponseEntity.ok(maintenance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaintenance(@PathVariable int id) {

        MaintenanceRecord existing = maintenanceService.findMaintenanceById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        maintenanceService.deleteMaintenance(id);

        return ResponseEntity.noContent().build();
    }
}