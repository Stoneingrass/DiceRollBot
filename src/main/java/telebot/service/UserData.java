package telebot.service;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.LinkedList;
import java.util.List;

public class UserData {
    public List<Message> messageList;

    public UserData() {
        messageList = new LinkedList<>();
    }
}
