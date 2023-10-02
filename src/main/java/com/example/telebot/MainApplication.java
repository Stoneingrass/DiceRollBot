package com.example.telebot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.telebot.Bot.ready;


public class MainApplication{
    public static InlineKeyboardMarkup keyboardM1;

    //как ограничивать количество запросов?
    public static void main(String[] args) throws TelegramApiException {
        BotConfig bc = new BotConfig();
        bc.configureBot();
        bc.runQueryTimer();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());




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

        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next))
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();
    }
}