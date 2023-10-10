package telebot.service;

import telebot.db.DBConnector;
import telebot.config.BotConfig;
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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Bot extends TelegramLongPollingBot {
    public BotConfig config;
    public Map<Long, UserData> usersData;
    public InlineKeyboardMarkup inlineKB;
    public boolean ready = true;

    public Bot() {
        usersData = new HashMap<>();
    }


    @Override
    public String getBotUsername() {
        return this.config.botName;
    }

    @Override
    public String getBotToken() {
        return this.config.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        //???
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

    public void sendText(Long userId, String what){

        SendMessage sm = SendMessage.builder()
                .chatId(userId.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    private void executeCommand(Long userId, String txt) {

        switch (txt) {
            case "/scream" -> sendText(userId, "ору.");
            case "/whisper" -> sendText(userId, "шепчу.");
            case "/menu" -> sendMenu(userId, "<b>Menu 1</b>", inlineKB);
        }
        return;
    }

    public void copyMessage(Long UserId, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(UserId.toString())  //We copy from the user
                .chatId(UserId.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMenu(Long userId, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(userId.toString())
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
            newKb.setReplyMarkup(inlineKB);
        } else if(data.equals("back")) {
            newTxt.setText("MENU 1");
            newKb.setReplyMarkup(inlineKB);
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
        execute(newKb);
    }

    private void testText(User user, Message message) {
        Long userId = user.getId();
        UserData userData = usersData.get(userId);

        if (message.hasText()) {
            sendText(userId, String.format(
                    "Уважаемый (-ая) %s! Благодарю вас за Ваши сообщения! " +
                            "Вот список всех ваших предыдущих сообщений:", user.getFirstName()));

            //add to user data
            userData.messageList.add(message.getText());
            //add to db
            try {
                DBConnector.writeUserMessage(userId, message.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (String m: userData.messageList) {
                try {
                    sendText(userId, m);
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
