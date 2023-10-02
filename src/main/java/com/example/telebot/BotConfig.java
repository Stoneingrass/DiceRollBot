package com.example.telebot;

import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider;

import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.telebot.Bot.ready;

public class BotConfig {
    public static String botName;
    public static String botToken;

    public void configureBot() {

        Properties prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(XmlCollectionJaxbProvider.App.class.getClassLoader().getResourceAsStream("config.properties"));

            //get the property value and print it out
            botToken=prop.getProperty("bot_token");
            botName=prop.getProperty("bot_name");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void runQueryTimer() {
        TimerTask task = new TimerTask() {
            public void run() {
                ready = true;
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 500L;
        long period = 500L;
        timer.scheduleAtFixedRate (task, delay, period);
    }
}
