package telebot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telebot.config.BotInit;

import java.sql.SQLException;


public class Main {
    //как ограничивать количество запросов?
    //потом переконструировать под спринг
    //инкапсуляция, геттеры сеттеры
    //ссылку на приложение в конфиг
    //ошибка при запуске .jar через консоль

    public static void main(String[] args) throws SQLException, ClassNotFoundException, TelegramApiException {
        BotInit botInit = new BotInit();
        botInit.init();
    }
}