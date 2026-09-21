package com.mining.maintenance.service;

import com.mining.maintenance.model.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssetServiceTest {

    private FakeAssetRepository fakeRepository;
    private AssetService assetService;

    @BeforeEach
    void setUp() {

        fakeRepository = new FakeAssetRepository();
        assetService = new AssetService(fakeRepository);
    }


    // =========================
    // HELPER
    // =========================

    private Asset validAsset() {

        return new Asset(
                0,
                "EXC-001",
                "Excavator Test",
                "Heavy Equipment",
                "Excavator",
                "Komatsu",
                "PC200",
                "SN-001",
                1000000.0,
                "active",
                "Pit A",
                "2026-01-01",
                100.0,
                "Test asset"
        );
    }


    // =========================
    // ADD ASSET
    // =========================

    @Test
    void addAsset_validData_returnsTrueAndSaves() {

        boolean result = assetService.addAsset(validAsset());

        assertTrue(result);
        assertEquals(1, assetService.getTotalAssets());
    }

    @Test
    void addAsset_emptyAssetCode_returnsFalse() {

        Asset asset = validAsset();
        asset.setAssetCode("   ");

        assertFalse(assetService.addAsset(asset));
    }

    @Test
    void addAsset_emptyAssetName_returnsFalse() {

        Asset asset = validAsset();
        asset.setAssetName("");

        assertFalse(assetService.addAsset(asset));
    }

    @Test
    void addAsset_negativePurchasePrice_returnsFalse() {

        Asset asset = validAsset();
        asset.setPurchasePrice(-1);

        assertFalse(assetService.addAsset(asset));
    }

    @Test
    void addAsset_negativeOperatingHours_returnsFalse() {

        Asset asset = validAsset();
        asset.setOperatingHours(-5);

        assertFalse(assetService.addAsset(asset));
    }

    @Test
    void addAsset_invalidStatus_returnsFalse() {

        Asset asset = validAsset();
        asset.setStatus("broken");

        assertFalse(assetService.addAsset(asset));
    }

    @Test
    void addAsset_invalidDate_returnsFalse() {

        Asset asset = validAsset();
        asset.setPurchaseDate("01-01-2026");

        assertFalse(assetService.addAsset(asset));
    }

    @Test
    void addAsset_duplicateAssetCode_returnsFalse() {

        assetService.addAsset(validAsset());

        assertFalse(
                assetService.addAsset(validAsset())
        );
    }

    @Test
    void addAsset_duplicateAssetCodeDifferentCase_returnsFalse() {

        assetService.addAsset(validAsset());

        Asset duplicate = validAsset();
        duplicate.setAssetCode("exc-001");

        assertFalse(assetService.addAsset(duplicate));
    }

    @Test
    void addAsset_statusNormalizedToLowercase() {

        Asset asset = validAsset();
        asset.setStatus("ACTIVE");

        assetService.addAsset(asset);

        assertEquals("active", asset.getStatus());
    }


    // =========================
    // UPDATE ASSET
    // =========================

    @Test
    void updateAsset_sameAssetSameCode_notTreatedAsDuplicate() {

        Asset asset = validAsset();
        assetService.addAsset(asset);

        asset.setAssetName("Excavator Updated");

        boolean result = assetService.updateAsset(asset);

        assertTrue(result);
        assertEquals(
                "Excavator Updated",
                assetService.findById(asset.getId()).getAssetName()
        );
    }


    // =========================
    // DELETE ASSET
    // =========================

    @Test
    void deleteAsset_withoutMaintenanceRecords_returnsTrue() {

        Asset asset = validAsset();
        assetService.addAsset(asset);

        boolean result = assetService.deleteAsset(asset.getId());

        assertTrue(result);
        assertEquals(0, assetService.getTotalAssets());
    }

    @Test
    void deleteAsset_withMaintenanceRecords_returnsFalse() {

        Asset asset = validAsset();
        assetService.addAsset(asset);

        fakeRepository.setMaintenanceCount(asset.getId(), 2);

        boolean result = assetService.deleteAsset(asset.getId());

        assertFalse(result);
        assertEquals(1, assetService.getTotalAssets());
    }


    // =========================
    // isValidStatus
    // =========================

    @Test
    void isValidStatus_validValues_returnsTrue() {

        assertTrue(assetService.isValidStatus("active"));
        assertTrue(assetService.isValidStatus("MAINTENANCE"));
        assertTrue(assetService.isValidStatus(" inactive "));
    }

    @Test
    void isValidStatus_invalidValue_returnsFalse() {

        assertFalse(assetService.isValidStatus("broken"));
        assertFalse(assetService.isValidStatus(null));
    }
}