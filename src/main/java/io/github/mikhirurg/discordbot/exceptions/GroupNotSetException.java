package io.github.mikhirurg.discordbot.exceptions;

public class GroupNotSetException extends ScheduleBotException {
    public GroupNotSetException() {
        super("Group not set for user!");
    }
}
