package io.github.mikhirurg.discordbot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    @Test
    void getNextDayName() {
        if (TimeUtil.getCurrentDayName().equals("Сб")) {
            assertEquals(TimeUtil.getNextDayName(), "Вс");
        }
    }
}