package io.github.mikhirurg.discordbot.util;

import java.time.LocalDate;
import java.util.Calendar;

public class TimeUtil {
    public static String getCurrentDayName() {
        switch (LocalDate.now().getDayOfWeek().getValue()) {
            case 1: return "Пн";
            case 2: return "Вт";
            case 3: return "Ср";
            case 4: return "Чт";
            case 5: return "Пт";
            case 6: return "Сб";
            case 7: return "Вс";
        }
        return null;
    }

    public static String getNextDayName(String name) {
        switch (name) {
            case "Вс": return "Пн";
            case "Пн": return "Вт";
            case "Вт": return "Ср";
            case "Ср": return "Чт";
            case "Чт": return "Пт";
            case "Пт": return "Сб";
            case "Сб": return "Вс";
        }
        return null;
    }

    public static int getWeekType() {
        return 2 - Calendar.getInstance().getWeekYear() % 2;
    }

    public static String getNextDayName() {
        return getNextDayName(getCurrentDayName());
    }
}
