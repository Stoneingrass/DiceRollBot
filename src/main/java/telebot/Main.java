package telebot;

import telebot.db.DBConnector;
import telebot.config.BotInit;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;


public class Main {
    //как ограничивать количество запросов?
    //потом переконструировать под спринг
    //инкапсуляция
    //добавить название бд в конфиг?

    public static void main(String[] args) throws TelegramApiException, SQLException, ClassNotFoundException {
        BotInit bi = new BotInit();
        bi.init();

        DBConnector.connect();

        //DBConnector.closeDB();
    }
}