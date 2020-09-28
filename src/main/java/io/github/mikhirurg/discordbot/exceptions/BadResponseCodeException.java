package io.github.mikhirurg.discordbot.exceptions;

public class BadResponseCodeException extends ScheduleBotException {
    public BadResponseCodeException(int code) {
        super("Cannot connect to itmo.ru: [CODE: " + code + "].");
    }
}
