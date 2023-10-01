package com.example.telebot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;



import static com.example.telebot.MainApplication.keyboardM1;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "SvirillyaBot";
    }

    @Override
    public String getBotToken() {
        return "6377867338:AAH1infebiTqpYN6Q4slznKQOFF771TYpQw";
    }

    static public List<Message> list = new LinkedList<>();
    static public boolean ready = true;


    @Override
    public void onUpdateReceived(Update update) {
        if (!isReadyToGetQuery()) {
            return;
        }


        if(update.hasCallbackQuery()) {
            try {
                buttonTap(update.getCallbackQuery().getFrom().getId(),
                        update.getCallbackQuery().getId(),
                        update.getCallbackQuery().getData(),
                        update.getCallbackQuery().getMessage().getMessageId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (update.hasMessage()) {

            Message message = update.getMessage();
            User user = message.getFrom();
            Long userId = user.getId();


            if (message.isCommand()) {
                executeCommand(userId, message.getText());
                return;
            }
            if (message.isUserMessage()) {
                testText(user, message);
                return;
            }
            return;
        }
    }

    private boolean isReadyToGetQuery() {
        if (ready) {
            ready = false;
            return true;
        }
        else {
            return false;
        }
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    private void executeCommand(Long userId, String txt) {
        boolean screaming = true;
        if (txt.equals("/scream"))
            screaming = true;
        else if (txt.equals("/whisper"))
            screaming = false;
        else if (txt.equals("/menu"))
            sendMenu(userId, "<b>Menu 1</b>", keyboardM1);
        return;
    }

    public void copyMessage(Long who, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())  //We copy from the user
                .chatId(who.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString()).messageId(msgId).build();

        if(data.equals("next")) {
            newTxt.setText("MENU 2");
            newKb.setReplyMarkup(keyboardM1);
        } else if(data.equals("back")) {
            newTxt.setText("MENU 1");
            newKb.setReplyMarkup(keyboardM1);
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
        execute(newKb);
    }

    private void testText(User user, Message message) {
        Long userId = user.getId();

        if (message.hasText()) {
            sendText(userId, "ты лох, " + user.getFirstName() +
                    ", зачем ты это написал: \n" + message.getText() + "\nа?");

            list.add(message);
            for (Message m: list) {
                try {
                    sendText(userId, m.getText());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }
        else {
            copyMessage(userId, message.getMessageId());
        }
    }
}
