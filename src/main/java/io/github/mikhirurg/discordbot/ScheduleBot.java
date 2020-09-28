package io.github.mikhirurg.discordbot;

import io.github.mikhirurg.discordbot.exceptions.GroupNotSetException;
import io.github.mikhirurg.discordbot.model.UserConfig;
import io.github.mikhirurg.discordbot.util.PicTableBuilder;
import io.github.mikhirurg.discordbot.util.ScheduleGrabber;
import io.github.mikhirurg.discordbot.util.TableBuilder;
import io.github.mikhirurg.discordbot.util.TimeUtil;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class ScheduleBot extends ListenerAdapter {
    private Map<String, UserConfig> map;
    private final Map<String, String> help;
    private final String fileToSave;

    public ScheduleBot(Properties helpFile, Properties properties) throws ClassNotFoundException {
        fileToSave = properties.getProperty("save");
        try {
            map = (Map<String, UserConfig>) new ObjectInputStream(new FileInputStream(fileToSave)).readObject();
        } catch (IOException exception) {
            map = new HashMap<>();
        }
        help = new HashMap<>();
        helpFile.stringPropertyNames()
                .forEach(key -> help.put(key, helpFile.getProperty(key)));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        if (!event.getAuthor().isBot()) {
            try {
                if (message.getContentRaw().equals("!full")) {
                    sendWeek(event, false);
                } else if (message.getContentRaw().equals("!short")) {
                    sendWeek(event, true);
                } else if (message.getContentRaw().equals("!today")) {
                    sendToday(event, true);
                } else if (message.getContentRaw().equals("!tomorrow")) {
                    sendTomorrow(event, true);
                } else if (message.getContentRaw().split(" ")[0].equals("!setgroup")) {
                    processSetGroup(event, message);
                } else if (message.getContentRaw().equals("!picmode")) {
                    processTableMode(event);
                } else if (message.getContentRaw().equals("!weekmode")) {
                    processWeekMode(event);
                } else if (message.getContentRaw().equals("!help")) {
                    sendHelp(event);
                } else if (message.getContentRaw().equals("!today_full")) {
                    sendToday(event, false);
                } else if (message.getContentRaw().equals("!tomorrow_full")) {
                    sendTomorrow(event, false);
                } else {
                    processUnknownCommand(event);
                }
            } catch (GroupNotSetException e) {
                event.getChannel().sendMessage("Send command `!setgroup` to set group for your user!").queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendWeek(MessageReceivedEvent event, boolean isShort) throws IOException {
        UserConfig config = map.get(event.getAuthor().getId());
        if (config == null) {
            throw new GroupNotSetException();
        } else {
            int week = (config.getWeekMode()) ? 0 : TimeUtil.getWeekType();
            if (config.getPicMode()) {
                BufferedImage table;
                if (isShort)
                    table = PicTableBuilder.buildWeekShortTable(ScheduleGrabber.getSchedule(config.getGroup(), week));
                else
                    table = PicTableBuilder.buildWeekTable(ScheduleGrabber.getSchedule(config.getGroup(), week));
                File tmp = File.createTempFile("schedule_bot", ".png");
                ImageIO.write(table, "png", tmp);
                event.getChannel().sendFile(tmp).queue();
            } else {
                String table;
                if (isShort)
                    table = TableBuilder.buildWeekShortTable(ScheduleGrabber.getSchedule(config.getGroup(), week));
                else
                    table = TableBuilder.buildWeekTable(ScheduleGrabber.getSchedule(config.getGroup(), week));
                for (String day : prepareMessages(table)) {
                    event.getChannel().sendMessage("`" + day + "`").queue();
                }
            }
        }
    }

    private void sendToday(MessageReceivedEvent event, boolean isShort) throws IOException {
        UserConfig config = map.get(event.getAuthor().getId());
        if (config == null) {
            throw new GroupNotSetException();
        } else {
            int week = (config.getWeekMode()) ? 0 : TimeUtil.getWeekType();
            if (config.getPicMode()) {
                BufferedImage table;
                if (isShort)
                    table = PicTableBuilder.buildDayShortTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getCurrentDayName());
                else
                    table = PicTableBuilder.buildDayTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getCurrentDayName());
                File tmp = File.createTempFile("schedule_bot", ".png");
                ImageIO.write(table, "png", tmp);
                event.getChannel().sendFile(tmp).queue();
            } else {
                String table;
                if (isShort)
                    table = TableBuilder.buildDayShortTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getCurrentDayName());
                else
                    table = TableBuilder.buildDayTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getCurrentDayName());
                for (String day : prepareMessages(table)) {
                    event.getChannel().sendMessage("`" + day + "`").queue();
                }
            }
        }
    }

    private void sendTomorrow(MessageReceivedEvent event, boolean isShort) throws IOException {
        UserConfig config = map.get(event.getAuthor().getId());
        if (config == null) {
            throw new GroupNotSetException();
        } else {
            int week = (config.getWeekMode()) ? 0 : TimeUtil.getWeekType();
            if (config.getPicMode()) {
                BufferedImage table;
                if (isShort)
                    table = PicTableBuilder.buildDayShortTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getNextDayName());
                else
                    table = PicTableBuilder.buildDayTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getNextDayName());
                File tmp = File.createTempFile("schedule_bot", ".png");
                ImageIO.write(table, "png", tmp);
                event.getChannel().sendFile(tmp).queue();
            } else {
                String table;
                if (isShort)
                    table = TableBuilder.buildDayShortTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getNextDayName());
                else
                    table = TableBuilder.buildDayTable(ScheduleGrabber.getSchedule(config.getGroup(), week), TimeUtil.getNextDayName());
                for (String day : prepareMessages(table)) {
                    event.getChannel().sendMessage("`" + day + "`").queue();
                }
            }
        }
    }

    private void processSetGroup(MessageReceivedEvent event, Message message) {
        String[] params = message.getContentRaw().split(" ");
        if (params.length == 2) {
            String group = message.getContentRaw().split(" ")[1];
            UserConfig config = map.get(event.getAuthor().getId());
            if (config == null) {
                UserConfig userConfig = new UserConfig(group, false);
                map.put(event.getAuthor().getId(), userConfig);
            } else {
                config.setGroup(group);
            }
            event.getChannel().sendMessage("Group set successfully!").queue();
            saveMap();
        } else {
            event.getChannel().sendMessage("Error, try again!").queue();
        }
    }

    private void processTableMode(MessageReceivedEvent event) {
        UserConfig config = map.get(event.getAuthor().getId());
        if (config == null) {
            throw new GroupNotSetException();
        } else {
            boolean picMode = config.getPicMode();
            config.setPicMode(!picMode);
        }
        event.getChannel().sendMessage("Table mode set successfully!").queue();
        saveMap();
    }

    private void processWeekMode(MessageReceivedEvent event) {
        UserConfig config = map.get(event.getAuthor().getId());
        if (config == null) {
            throw new GroupNotSetException();
        } else {
            boolean weekMode = config.getWeekMode();
            config.setWeekMode(!weekMode);
        }
        event.getChannel().sendMessage("Week mode set successfully!").queue();
        saveMap();
    }

    private void processUnknownCommand(MessageReceivedEvent event) {
        event.getChannel().sendMessage("I cant understand this command! Please print `!help` to get information about possible commands.").queue();
    }

    private void sendHelp(MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();
        for (Entry<String, String> e : help.entrySet()) {
            builder.append("`!")
                    .append(e.getKey())
                    .append("` - ")
                    .append(e.getValue())
                    .append("\n");
        }
        event.getChannel().sendMessage(builder.toString()).queue();
    }

    private String[] prepareMessages(String data) {
        String line = data.split("\n")[0];
        return data.split("(?=" + line.replaceAll("\\+", "\\\\+") + ")");
    }

    private void saveMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
            oos.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, LoginException, ClassNotFoundException {
        /*ImageIO.write(PicTableBuilder.buildWeekTable(ScheduleGrabber.getSchedule("M3202", ScheduleGrabber.WHOLE_WEEK)), "png", new FileOutputStream("week.png"));
        ImageIO.write(PicTableBuilder.buildDayTable(ScheduleGrabber.getSchedule("M3202", ScheduleGrabber.WHOLE_WEEK), TimeUtil.getCurrentDayName()), "png", new FileOutputStream("day.png"));
        ImageIO.write(PicTableBuilder.buildDayShortTable(ScheduleGrabber.getSchedule("M3202", ScheduleGrabber.WHOLE_WEEK), TimeUtil.getCurrentDayName()), "png", new FileOutputStream("daySmall.png"));*/

        Properties properties = new Properties();
        properties.load(new FileInputStream(args[0]));

        Properties helpProps = new Properties();
        helpProps.load(ScheduleBot.class.getClassLoader().getResourceAsStream("help.properties"));

        String token = properties.getProperty("token");
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        jdaBuilder.addEventListeners(new ScheduleBot(helpProps, properties));
        jdaBuilder.build();
    }
}
