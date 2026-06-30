package db;

import org.junit.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCarTest {

    @Test
    public void getCarDB() throws SQLException {
        DbConnection db = new DbConnection();
        db.connect();

        ResultSet rs = db.select("SELECT * FROM car ORDER BY id DESC LIMIT 1");

        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Engine Type ID: " + rs.getInt("engine_type_id"));
            System.out.println("Mark: " + rs.getString("mark"));
            System.out.println("Model: " + rs.getString("model"));
            System.out.println("Price: " + rs.getObject("price"));
            System.out.println("Person ID: " + rs.getInt("person_id"));
            System.out.println("---");
        }

        rs.close();
        db.close();
    }

    @Test
    public void getTables() throws SQLException {
        DbConnection db = new DbConnection();
        db.connect();

        ResultSet rs = db.select("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");

        System.out.println("Список таблиц:");
        while (rs.next()) {
            System.out.println(" - " + rs.getString("table_name"));
        }

        rs.close();
        db.close();
    }

    @Test
    public void getColumnsCar() throws SQLException {
        DbConnection db = new DbConnection();
        db.connect();

        ResultSet rs = db.select(
                "SELECT column_name, data_type, is_nullable " +
                        "FROM information_schema.columns " +
                        "WHERE table_schema = 'public' AND table_name = 'car'"
        );

        System.out.println("Столбцы в таблице car:");
        while (rs.next()) {
            System.out.println(" - " + rs.getString("column_name"));
        }

        rs.close();
        db.close();
    }
}