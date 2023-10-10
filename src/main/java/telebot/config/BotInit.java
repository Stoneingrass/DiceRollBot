package telebot.config;

import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import telebot.db.DBConnector;
import telebot.service.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BotInit {
    public Bot bot;

    public void init() throws TelegramApiException, SQLException, ClassNotFoundException {
        bot = new Bot();
        bot.config = new BotConfig();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);

        DBConnector.connect(bot.config.dbFile);

        //load user data
        bot.usersData = DBConnector.getUsersMessages();


        initInlineKeyboard();
        initQueryTimer();

        initKeyboardButton();
    }

    public void initInlineKeyboard() {
        var next = InlineKeyboardButton.builder()
                .text("Next").callbackData("next")
                .build();

        var back = InlineKeyboardButton.builder()
                .text("Back").callbackData("back")
                .build();

        var url = InlineKeyboardButton.builder()
                .text("Tutorial")
                .url("https://core.telegram.org/bots/api")
                .build();

        bot.inlineKB = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next))
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();
    }

    public void initKeyboardButton() throws TelegramApiException {
        WebAppInfo webAppInfo = new WebAppInfo(bot.config.appLink);

        SetChatMenuButton setChatMenuButton = new SetChatMenuButton();
        setChatMenuButton.setMenuButton(MenuButtonWebApp.builder()
                .text(bot.config.appName)
                .webAppInfo(webAppInfo)
                .build());

        bot.execute(setChatMenuButton);
    }

    public void initQueryTimer() {
        TimerTask task = new TimerTask() {
            public void run() {
                bot.ready = true;
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 500L;
        long period = 500L;
        timer.scheduleAtFixedRate (task, delay, period);
    }
}