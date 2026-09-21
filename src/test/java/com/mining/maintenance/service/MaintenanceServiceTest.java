package com.mining.maintenance.service;

import com.mining.maintenance.model.MaintenanceRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceServiceTest {

    private FakeMaintenanceRepository fakeRepository;
    private MaintenanceService maintenanceService;

    @BeforeEach
    void setUp() {

        fakeRepository = new FakeMaintenanceRepository();
        maintenanceService = new MaintenanceService(fakeRepository);
    }


    // =========================
    // HELPER
    // =========================

    private MaintenanceRecord validMaintenance() {

        return new MaintenanceRecord(
                0,
                1,
                "Preventive",
                "2026-08-15",
                "Engine inspection and oil change",
                "Technician A",
                5000000,
                "active"
        );
    }


    // =========================
    // ADD MAINTENANCE
    // =========================

    @Test
    void addMaintenance_validData_returnsTrueAndSaves() {

        boolean result = maintenanceService.addMaintenance(validMaintenance());

        assertTrue(result);
        assertEquals(1, maintenanceService.getTotalMaintenance());
    }

    @Test
    void addMaintenance_invalidAssetId_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setAssetId(0);

        assertFalse(maintenanceService.addMaintenance(maintenance));
    }

    @Test
    void addMaintenance_emptyMaintenanceType_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setMaintenanceType("  ");

        assertFalse(maintenanceService.addMaintenance(maintenance));
    }

    @Test
    void addMaintenance_invalidDate_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setMaintenanceDate("15-08-2026");

        assertFalse(maintenanceService.addMaintenance(maintenance));
    }

    @Test
    void addMaintenance_emptyDescription_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setDescription("");

        assertFalse(maintenanceService.addMaintenance(maintenance));
    }

    @Test
    void addMaintenance_emptyTechnician_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setTechnician("   ");

        assertFalse(maintenanceService.addMaintenance(maintenance));
    }

    @Test
    void addMaintenance_negativeCost_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setCost(-100);

        assertFalse(maintenanceService.addMaintenance(maintenance));
    }

    @Test
    void addMaintenance_invalidStatus_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setStatus("pending");

        assertFalse(maintenanceService.addMaintenance(maintenance));
    }

    @Test
    void addMaintenance_statusNormalizedToLowercase() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenance.setStatus("COMPLETED");

        maintenanceService.addMaintenance(maintenance);

        assertEquals("completed", maintenance.getStatus());
    }


    // =========================
    // UPDATE MAINTENANCE
    // =========================

    @Test
    void updateMaintenance_validChange_returnsTrue() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenanceService.addMaintenance(maintenance);

        maintenance.setStatus("completed");

        boolean result = maintenanceService.updateMaintenance(maintenance);

        assertTrue(result);
        assertEquals(
                "completed",
                maintenanceService.findMaintenanceById(maintenance.getId())
                        .getStatus()
        );
    }

    @Test
    void updateMaintenance_invalidStatus_returnsFalse() {

        MaintenanceRecord maintenance = validMaintenance();
        maintenanceService.addMaintenance(maintenance);

        maintenance.setStatus("broken");

        assertFalse(maintenanceService.updateMaintenance(maintenance));
    }


    // =========================
    // isValidStatus
    // =========================

    @Test
    void isValidStatus_validValues_returnsTrue() {

        assertTrue(maintenanceService.isValidStatus("active"));
        assertTrue(maintenanceService.isValidStatus("COMPLETED"));
    }

    @Test
    void isValidStatus_invalidValue_returnsFalse() {

        assertFalse(maintenanceService.isValidStatus("pending"));
        assertFalse(maintenanceService.isValidStatus(null));
    }
}