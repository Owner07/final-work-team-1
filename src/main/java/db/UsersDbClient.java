package db;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import models.users.db.UserDbEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
public class UsersDbClient {

    @Step("Получить пользователя из БД по id={userId}")
    public UserDbEntity getUserById(Long userId) {
        String sql = """
            SELECT id, age, first_name, money, second_name, sex, house_id
            FROM person
            WHERE id = ?
            """;

        log.info("Execute SQL: {} userId={}", sql, userId);

        DbConnection dbConnection = new DbConnection();
        dbConnection.connect();

        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Long houseId = rs.getObject("house_id") == null
                            ? null
                            : rs.getLong("house_id");

                    return UserDbEntity.builder()
                            .id(rs.getLong("id"))
                            .age(rs.getInt("age"))
                            .firstName(rs.getString("first_name"))
                            .money(rs.getBigDecimal("money"))
                            .secondName(rs.getString("second_name"))
                            .sex(rs.getBoolean("sex"))
                            .houseId(houseId)
                            .build();
                }
            }

            return null;

        } catch (Exception e) {
            log.error("Failed to get user from DB by id={}", userId, e);
            throw new RuntimeException("Failed to get user from DB by id=" + userId, e);
        } finally {
            dbConnection.close();
        }
    }

    @Step("Получить количество пользователей из БД")
    public int getUsersCount() {
        String sql = """
            SELECT COUNT(*)
            FROM person
            """;

        log.info("Execute SQL: {}", sql);

        DbConnection dbConnection = new DbConnection();
        dbConnection.connect();

        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (Exception e) {
            log.error("Failed to get users count from DB", e);
            throw new RuntimeException("Failed to get users count from DB", e);
        } finally {
            dbConnection.close();
        }
    }
}