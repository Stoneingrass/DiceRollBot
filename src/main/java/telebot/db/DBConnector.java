package telebot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;


    public static void connect() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/assets/BotDB.sqlite");

        statement = connection.createStatement();
    }


    // --------Заполнение таблицы--------
    public static void writeDB() throws SQLException {
        statement.execute("INSERT INTO users ('id', 'name', 'phone') VALUES (1, 'Petya', 125453); ");
        statement.execute("INSERT INTO users ('id', 'name', 'phone') VALUES (2, 'Vasya', 321789); ");
        statement.execute("INSERT INTO users ('id', 'name', 'phone') VALUES (3, 'Masha', 456123); ");

        System.out.println("Таблица заполнена");
    }

    // -------- Вывод таблицы--------
    public static void readDB() throws ClassNotFoundException, SQLException {
        resultSet = statement.executeQuery("SELECT * FROM users");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String phone = resultSet.getString("phone");
            System.out.println("ID = " + id);
            System.out.println("name = " + name);
            System.out.println("phone = " + phone);
            System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    // --------Закрытие--------
    public static void closeDB() throws SQLException {
        connection.close();
        statement.close();
        resultSet.close();

        System.out.println("Соединения закрыты");
    }

}