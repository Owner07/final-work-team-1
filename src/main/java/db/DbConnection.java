package db;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import utils.PropertyReader;

import java.sql.*;

@Log4j2
public class DbConnection {

    private final String USER = System.getProperty("db_user", PropertyReader.getProperty("db_user"));
    private final String PASSWORD = System.getProperty("db_password", PropertyReader.getProperty("db_password"));
    private final String URL = System.getProperty("db_url", PropertyReader.getProperty("db_url"));

    @Getter
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();
            log.info("Connected to database successfully");
        } catch (SQLException e) {
            log.error("Failed to connect to database", e);
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public ResultSet select (String query){
            try {
                return statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void insert (String query){
            try {
                statement.execute(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void insertPrepared (){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO () VALUES (?)"
                );
//                preparedStatement.setInt(1, );
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void delete (String query){
            try {
                statement.execute(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void close () {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
                log.info("Connected to database closed");
            } catch (SQLException e) {
                log.info("Error to database closed");
                throw new RuntimeException("Error to database closed", e);
            }
        }
    }