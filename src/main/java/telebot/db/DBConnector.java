package telebot.db;

import telebot.service.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DBConnector {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;


    public static void connect(String dbFile) throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(dbFile);

        statement = connection.createStatement();
    }

    public static Map<Long, UserData> getUsersMessages() throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM user_messages");

        Map<Long, UserData> usersData = new HashMap<>();
        Long userId;

        while (resultSet.next()) {
            userId = resultSet.getLong("user_id");

            usersData.putIfAbsent(userId, new UserData());
            usersData.get(userId).messageList.add(resultSet.getString("message_text"));
        }

        return usersData;
    }

    public static void writeUserMessage(Long userId, String messageText) throws SQLException {
        statement.execute(String.format("INSERT INTO user_messages ('user_id', 'message_text') VALUES (%d, \"%s\");", userId, messageText));

    }

    public static void closeDB() throws SQLException {
        connection.close();
        statement.close();
        resultSet.close();
    }
}