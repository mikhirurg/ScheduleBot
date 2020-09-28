package io.github.mikhirurg.discordbot.exceptions;

public class ReadDataException extends ScheduleBotException {
    public ReadDataException() {
        super("Error while reading data from site.");
    }
}
