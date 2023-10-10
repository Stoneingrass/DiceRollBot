package telebot.config;

import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider;

import java.io.IOException;
import java.util.Properties;

public class BotConfig {
    public String botName;
    public String botToken;
    public String appName;
    public String appLink;
    public String dbFile;

    public BotConfig() {
        Properties prop = new Properties();
        try {
            prop.load(XmlCollectionJaxbProvider.App.class.getClassLoader().getResourceAsStream("config.properties"));

            botToken=prop.getProperty("bot_token");
            botName=prop.getProperty("bot_name");
            appName=prop.getProperty("app_name");
            appLink =prop.getProperty("app_link");
            dbFile=prop.getProperty("db_file");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
