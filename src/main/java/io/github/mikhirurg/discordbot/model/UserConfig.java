package io.github.mikhirurg.discordbot.model;

import java.io.Serializable;
import java.util.StringTokenizer;

public class UserConfig implements Serializable {
    private String group = null;
    private boolean picMode = false;
    private boolean weekMode = false;

    public UserConfig(String group, boolean picMode) {
        this.group = group;
        this.picMode = picMode;
    }

    public UserConfig() {

    }

    public void setPicMode(boolean picMode) {
        this.picMode = picMode;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public boolean getPicMode() {
        return picMode;
    }

    public static UserConfig parseUserConfig(String data) {
        StringTokenizer tok = new StringTokenizer(data);
        return new UserConfig(tok.nextToken(), Boolean.parseBoolean(tok.nextToken()));
    }

    public boolean getWeekMode() {
        return weekMode;
    }

    public void setWeekMode(boolean weekMode) {
        this.weekMode = weekMode;
    }
}
