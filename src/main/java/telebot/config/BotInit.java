package telebot.config;

import telebot.service.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BotInit {
    public Bot bot;

    public void init() throws TelegramApiException {
        bot = new Bot();
        bot.bc = new BotConfig();
        bot.bc.config();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);

        initKeyboard();
        initQueryTimer();
    }

    public void initKeyboard() {
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

        bot.keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next))
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();
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