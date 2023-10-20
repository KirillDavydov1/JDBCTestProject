package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {


    private final static String TABLE_NAME = "user";
    private final static String TABLE_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "id INT AUTO_INCREMENT," +
            "name VARCHAR(256)," +
            "lastName VARCHAR(256)," +
            "age INT," +
            "PRIMARY KEY (id))";
    private final static String TABLE_DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private final static String TABLE_SAVE_USER_QUERY = "INSERT " + TABLE_NAME + "(name, lastName, age) VALUES (?, ?, ?) ";
    private final static String TABLE_CLEAN_QUERY = "TRUNCATE TABLE " + TABLE_NAME;
    private final static String TABLE_GET_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private final static String TABLE_DELETE_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(TABLE_CREATE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(TABLE_DROP_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TABLE_SAVE_USER_QUERY)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User с именем - %s добавлен в базу данных\n", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TABLE_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(TABLE_GET_ALL_QUERY);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        (byte) resultSet.getInt("age"));
                user.setId(resultSet.getLong("id"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(TABLE_CLEAN_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
