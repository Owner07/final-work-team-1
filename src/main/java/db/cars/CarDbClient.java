package db.cars;

import api.models.cars.db.CarDbEntity;
import db.house.HouseDbConnection;
import io.qameta.allure.Step;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarDbClient {
    @Step("Получить авто из БД по id={carId}")
    public CarDbEntity getCarById(Integer carId) {
        String sql = """
            SELECT id, mark, model, price, engine_type_id, person_id
            FROM car
            WHERE id = ?
            """;

        HouseDbConnection dbConnection = new HouseDbConnection();
        dbConnection.connect();

        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, carId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Integer personId = rs.getObject("person_id") == null
                            ? null
                            : rs.getInt("person_id");
                    return CarDbEntity.builder()
                            .id(rs.getInt("id"))
                            .mark(rs.getString("mark"))
                            .model(rs.getString("model"))
                            .price(rs.getBigDecimal("price"))
                            .engineTypeId(rs.getInt("engine_type_id"))
                            .personId(personId)
                            .build();
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get car from DB by id=" + carId, e);
        } finally {
            dbConnection.close();
        }
    }

    @Step("Получить engine_type_id по имени типа: {typeName}")
    public int getEngineTypeIdByName(String typeName) {
        String sql = "SELECT id FROM engine_type WHERE type_name = ?";

        HouseDbConnection dbConnection = new HouseDbConnection();
        dbConnection.connect();

        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, typeName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось найти engine_type по имени: " + typeName, e);
        } finally {
            dbConnection.close();
        }
        throw new IllegalArgumentException("Тип двигателя не найден: " + typeName);
    }
}