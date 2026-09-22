package com.mining.maintenance.controller;

import com.mining.maintenance.model.Asset;
import com.mining.maintenance.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetRestController {

    private final AssetService assetService;

    public AssetRestController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{assetCode}")
    public ResponseEntity<Asset> getAssetByCode(@PathVariable String assetCode) {

        Asset asset = assetService.findByAssetCode(assetCode);

        if (asset == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(asset);
    }

    @PostMapping
    public ResponseEntity<?> createAsset(@RequestBody Asset asset) {

        String validationError = assetService.validateAsset(asset);

        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }

        boolean saved = assetService.addAsset(asset);

        if (!saved) {
            return ResponseEntity.badRequest().body("Failed to save asset.");
        }

        return ResponseEntity.status(201).body(asset);
    }
}