package com.mining.maintenance.repository;

import com.mining.maintenance.config.DatabaseConnection;
import com.mining.maintenance.exception.DatabaseException;
import com.mining.maintenance.model.Asset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssetRepository {

    // =========================
    // SAVE ASSET
    // =========================

    public boolean save(Asset asset) {

        String sql =
                "INSERT INTO assets " +
                        "(asset_code, asset_name, category, type, brand, model, " +
                        "serial_number, purchase_price, status, location, " +
                        "purchase_date, operating_hours, description) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    asset.getAssetCode()
            );

            statement.setString(
                    2,
                    asset.getAssetName()
            );

            statement.setString(
                    3,
                    asset.getCategory()
            );

            statement.setString(
                    4,
                    asset.getType()
            );

            statement.setString(
                    5,
                    asset.getBrand()
            );

            statement.setString(
                    6,
                    asset.getModel()
            );

            statement.setString(
                    7,
                    asset.getSerialNumber()
            );

            statement.setDouble(
                    8,
                    asset.getPurchasePrice()
            );

            statement.setString(
                    9,
                    asset.getStatus()
            );

            statement.setString(
                    10,
                    asset.getLocation()
            );

            statement.setDate(
                    11,
                    Date.valueOf(
                            asset.getPurchaseDate()
                    )
            );

            statement.setDouble(
                    12,
                    asset.getOperatingHours()
            );

            statement.setString(
                    13,
                    asset.getDescription()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to save asset.",
                    e
            );
        }
    }


    // =========================
    // FIND ALL
    // =========================

    public List<Asset> findAll() {

        List<Asset> assets =
                new ArrayList<>();

        String sql =
                "SELECT * FROM assets ORDER BY id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                assets.add(
                        mapRowToAsset(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to retrieve assets.",
                    e
            );
        }

        return assets;
    }


    // =========================
    // SEARCH ASSETS
    // =========================

    public List<Asset> search(
            String keyword
    ) {

        List<Asset> assets =
                new ArrayList<>();

        String sql =
                "SELECT * FROM assets " +
                        "WHERE asset_code ILIKE ? " +
                        "OR asset_name ILIKE ? " +
                        "OR category ILIKE ? " +
                        "OR status ILIKE ? " +
                        "OR location ILIKE ? " +
                        "ORDER BY id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String searchKeyword =
                    "%" + keyword + "%";

            statement.setString(
                    1,
                    searchKeyword
            );

            statement.setString(
                    2,
                    searchKeyword
            );

            statement.setString(
                    3,
                    searchKeyword
            );

            statement.setString(
                    4,
                    searchKeyword
            );

            statement.setString(
                    5,
                    searchKeyword
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    assets.add(
                            mapRowToAsset(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to search assets.",
                    e
            );
        }

        return assets;
    }


    // =========================
    // FIND BY ASSET CODE
    // =========================

    public Asset findByAssetCode(
            String assetCode
    ) {

        String sql =
                "SELECT * FROM assets " +
                        "WHERE LOWER(asset_code) = LOWER(?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    assetCode
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapRowToAsset(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to find asset.",
                    e
            );
        }

        return null;
    }


    // =========================
    // FIND BY ID
    // =========================

    public Asset findById(
            int id
    ) {

        String sql =
                "SELECT * FROM assets " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapRowToAsset(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to find asset.",
                    e
            );
        }

        return null;
    }


    // =========================
    // UPDATE
    // =========================

    public boolean update(
            Asset asset
    ) {

        String sql =
                "UPDATE assets SET " +
                        "asset_code = ?, " +
                        "asset_name = ?, " +
                        "category = ?, " +
                        "type = ?, " +
                        "brand = ?, " +
                        "model = ?, " +
                        "serial_number = ?, " +
                        "purchase_price = ?, " +
                        "status = ?, " +
                        "location = ?, " +
                        "purchase_date = ?, " +
                        "operating_hours = ?, " +
                        "description = ? " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    asset.getAssetCode()
            );

            statement.setString(
                    2,
                    asset.getAssetName()
            );

            statement.setString(
                    3,
                    asset.getCategory()
            );

            statement.setString(
                    4,
                    asset.getType()
            );

            statement.setString(
                    5,
                    asset.getBrand()
            );

            statement.setString(
                    6,
                    asset.getModel()
            );

            statement.setString(
                    7,
                    asset.getSerialNumber()
            );

            statement.setDouble(
                    8,
                    asset.getPurchasePrice()
            );

            statement.setString(
                    9,
                    asset.getStatus()
            );

            statement.setString(
                    10,
                    asset.getLocation()
            );

            statement.setDate(
                    11,
                    Date.valueOf(
                            asset.getPurchaseDate()
                    )
            );

            statement.setDouble(
                    12,
                    asset.getOperatingHours()
            );

            statement.setString(
                    13,
                    asset.getDescription()
            );

            statement.setInt(
                    14,
                    asset.getId()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to update asset.",
                    e
            );
        }
    }


    // =========================
    // DELETE
    // =========================

    public boolean delete(
            int id
    ) {

        String sql =
                "DELETE FROM assets " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    id
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to delete asset.",
                    e
            );
        }
    }


    // =========================
    // COUNT
    // =========================

    public int count() {

        String sql =
                "SELECT COUNT(*) FROM assets";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt(1);
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to count assets.",
                    e
            );
        }

        return 0;
    }


    // =========================
    // COUNT BY STATUS
    // =========================

    public int countByStatus(
            String status
    ) {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM assets " +
                        "WHERE LOWER(status) = LOWER(?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to count assets by status.",
                    e
            );
        }

        return 0;
    }


    // =========================
    // TOTAL OPERATING HOURS
    // =========================

    public double getTotalOperatingHours() {

        String sql =
                "SELECT COALESCE(SUM(operating_hours), 0) " +
                        "FROM assets";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to calculate operating hours.",
                    e
            );
        }

        return 0;
    }


    // =========================
    // COUNT MAINTENANCE BY ASSET
    // =========================

    public int countMaintenanceByAssetId(
            int assetId
    ) {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM maintenance_records " +
                        "WHERE asset_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    assetId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to count maintenance by asset.",
                    e
            );
        }

        return 0;
    }


    // =========================
    // MAP RESULT SET
    // =========================

    private Asset mapRowToAsset(
            ResultSet resultSet
    ) throws SQLException {

        return new Asset(

                resultSet.getInt(
                        "id"
                ),

                resultSet.getString(
                        "asset_code"
                ),

                resultSet.getString(
                        "asset_name"
                ),

                resultSet.getString(
                        "category"
                ),

                resultSet.getString(
                        "type"
                ),

                resultSet.getString(
                        "brand"
                ),

                resultSet.getString(
                        "model"
                ),

                resultSet.getString(
                        "serial_number"
                ),

                resultSet.getDouble(
                        "purchase_price"
                ),

                resultSet.getString(
                        "status"
                ),

                resultSet.getString(
                        "location"
                ),

                resultSet.getDate(
                        "purchase_date"
                ).toString(),

                resultSet.getDouble(
                        "operating_hours"
                ),

                resultSet.getString(
                        "description"
                )
        );
    }
}