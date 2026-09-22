package com.mining.maintenance.repository;

import org.springframework.stereotype.Repository;
import com.mining.maintenance.config.DatabaseConnection;
import com.mining.maintenance.exception.DatabaseException;
import com.mining.maintenance.model.MaintenanceRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class MaintenanceRepository {

    // =========================
    // SAVE
    // =========================

    public boolean save(
            MaintenanceRecord maintenance
    ) {

        String sql =
                "INSERT INTO maintenance_records " +
                        "(asset_id, maintenance_type, maintenance_date, " +
                        "description, technician, cost, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    maintenance.getAssetId()
            );

            statement.setString(
                    2,
                    maintenance.getMaintenanceType()
            );

            statement.setDate(
                    3,
                    java.sql.Date.valueOf(
                            maintenance.getMaintenanceDate()
                    )
            );

            statement.setString(
                    4,
                    maintenance.getDescription()
            );

            statement.setString(
                    5,
                    maintenance.getTechnician()
            );

            statement.setDouble(
                    6,
                    maintenance.getCost()
            );

            statement.setString(
                    7,
                    maintenance.getStatus()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to save maintenance.",
                    e
            );
        }
    }


    // =========================
    // FIND ALL
    // =========================

    public List<MaintenanceRecord> findAll() {

        List<MaintenanceRecord> records =
                new ArrayList<>();

        String sql =
                "SELECT id, asset_id, maintenance_type, " +
                        "maintenance_date, description, technician, " +
                        "cost, status " +
                        "FROM maintenance_records " +
                        "ORDER BY id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                records.add(
                        mapRowToMaintenance(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to retrieve maintenance records.",
                    e
            );
        }

        return records;
    }


    // =========================
    // FIND BY ID
    // =========================

    public MaintenanceRecord findById(
            int id
    ) {

        String sql =
                "SELECT id, asset_id, maintenance_type, " +
                        "maintenance_date, description, technician, " +
                        "cost, status " +
                        "FROM maintenance_records " +
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

                    return mapRowToMaintenance(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to find maintenance.",
                    e
            );
        }

        return null;
    }


    // =========================
    // FIND BY ASSET
    // =========================

    public List<MaintenanceRecord> findByAssetId(
            int assetId
    ) {

        List<MaintenanceRecord> records =
                new ArrayList<>();

        String sql =
                "SELECT id, asset_id, maintenance_type, " +
                        "maintenance_date, description, technician, " +
                        "cost, status " +
                        "FROM maintenance_records " +
                        "WHERE asset_id = ? " +
                        "ORDER BY maintenance_date DESC, id DESC";

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

                while (resultSet.next()) {

                    records.add(
                            mapRowToMaintenance(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to retrieve maintenance history.",
                    e
            );
        }

        return records;
    }


    // =========================
    // UPDATE
    // =========================

    public boolean update(
            MaintenanceRecord maintenance
    ) {

        String sql =
                "UPDATE maintenance_records " +
                        "SET maintenance_type = ?, " +
                        "maintenance_date = ?, " +
                        "description = ?, " +
                        "technician = ?, " +
                        "cost = ?, " +
                        "status = ? " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    maintenance.getMaintenanceType()
            );

            statement.setDate(
                    2,
                    java.sql.Date.valueOf(
                            maintenance.getMaintenanceDate()
                    )
            );

            statement.setString(
                    3,
                    maintenance.getDescription()
            );

            statement.setString(
                    4,
                    maintenance.getTechnician()
            );

            statement.setDouble(
                    5,
                    maintenance.getCost()
            );

            statement.setString(
                    6,
                    maintenance.getStatus()
            );

            statement.setInt(
                    7,
                    maintenance.getId()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to update maintenance.",
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
                "DELETE FROM maintenance_records " +
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
                    "Failed to delete maintenance.",
                    e
            );
        }
    }


    // =========================
    // TOTAL MAINTENANCE
    // =========================

    public int countMaintenance() {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM maintenance_records";

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
                    "Failed to count maintenance.",
                    e
            );
        }

        return 0;
    }


    // =========================
    // TOTAL MAINTENANCE COST
    // =========================

    public double getTotalMaintenanceCost() {

        String sql =
                "SELECT COALESCE(SUM(cost), 0) " +
                        "FROM maintenance_records";

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
                    "Failed to calculate maintenance cost.",
                    e
            );
        }

        return 0;
    }


    // =========================
    // COUNT BY STATUS
    // =========================

    public int countMaintenanceByStatus(
            String status
    ) {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM maintenance_records " +
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
                    "Failed to count maintenance by status.",
                    e
            );
        }

        return 0;
    }


    // =========================
    // MAINTENANCE COST BY ASSET
    // =========================

    public Map<String, Double> getMaintenanceCostByAsset(
            String assetCode
    ) {

        Map<String, Double> result =
                new LinkedHashMap<>();

        String sql =
                "SELECT " +
                        "a.asset_code, " +
                        "a.asset_name, " +
                        "COUNT(m.id) AS maintenance_qty, " +
                        "COALESCE(SUM(m.cost), 0) AS total_cost " +
                        "FROM assets a " +
                        "LEFT JOIN maintenance_records m " +
                        "ON a.id = m.asset_id " +
                        "WHERE LOWER(a.asset_code) = LOWER(?) " +
                        "GROUP BY a.asset_code, a.asset_name";

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

                    result.put(
                            resultSet.getString("asset_code"),
                            resultSet.getDouble("total_cost")
                    );
                }
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to calculate maintenance cost by asset.",
                    e
            );
        }

        return result;
    }


    // =========================
    // MAINTENANCE COST BY TYPE
    // =========================

    public Map<String, Double> getMaintenanceCostByType() {

        Map<String, Double> result =
                new LinkedHashMap<>();

        String sql =
                "SELECT " +
                        "maintenance_type, " +
                        "COALESCE(SUM(cost), 0) AS total_cost " +
                        "FROM maintenance_records " +
                        "GROUP BY maintenance_type " +
                        "ORDER BY maintenance_type";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                result.put(
                        resultSet.getString(
                                "maintenance_type"
                        ),
                        resultSet.getDouble(
                                "total_cost"
                        )
                );
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to calculate maintenance cost by type.",
                    e
            );
        }

        return result;
    }


    // =========================
    // COST PER ASSET (terbesar dulu)
    // =========================

    public Map<String, Double> getMaintenanceCostPerAsset() {

        Map<String, Double> result =
                new LinkedHashMap<>();

        String sql =
                "SELECT a.asset_code, a.asset_name, " +
                        "SUM(m.cost) AS total_cost " +
                        "FROM assets a " +
                        "JOIN maintenance_records m " +
                        "ON a.id = m.asset_id " +
                        "GROUP BY a.asset_code, a.asset_name " +
                        "ORDER BY total_cost DESC, a.asset_code";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                result.put(
                        resultSet.getString("asset_code")
                                + " - "
                                + resultSet.getString("asset_name"),
                        resultSet.getDouble("total_cost")
                );
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to calculate maintenance cost per asset.",
                    e
            );
        }

        return result;
    }


    // =========================
    // MAP ROW
    // =========================

    private MaintenanceRecord mapRowToMaintenance(
            ResultSet resultSet
    ) throws SQLException {

        return new MaintenanceRecord(

                resultSet.getInt(
                        "id"
                ),

                resultSet.getInt(
                        "asset_id"
                ),

                resultSet.getString(
                        "maintenance_type"
                ),

                resultSet.getDate(
                        "maintenance_date"
                ).toString(),

                resultSet.getString(
                        "description"
                ),

                resultSet.getString(
                        "technician"
                ),

                resultSet.getDouble(
                        "cost"
                ),

                resultSet.getString(
                        "status"
                )
        );
    }
}