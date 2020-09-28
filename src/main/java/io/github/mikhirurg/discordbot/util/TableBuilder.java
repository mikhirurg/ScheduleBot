package io.github.mikhirurg.discordbot.util;

import io.github.mikhirurg.discordbot.model.Day;
import io.github.mikhirurg.discordbot.model.Subject;

import java.util.Arrays;
import java.util.List;

public class TableBuilder {
    public static String buildTable(List<Day> week, List<Integer> cols, List<String> days) {
        StringBuilder builder = new StringBuilder();
        int maxWidth;

        int[] maxLen = new int[cols.size()];

        List<String> header = List.of("День", "Время", "Кабинет", "Адрес", "Предмет", "Преподаватель", "Формат занятия");

        for (int i = 0; i < cols.size(); i++) {
            maxLen[i] = Math.max(maxLen[i], header.get(cols.get(i)).length());
        }

        for (Day day : week) {
            for (Subject subject : day.getSubjects()) {
                for (int i = 0; i < cols.size(); i++) {
                    maxLen[i] = Math.max(maxLen[i], subject.getData().get(cols.get(i)).length());
                }
            }
        }

        int[] prefSum = Arrays.copyOf(maxLen, maxLen.length);
        Arrays.parallelPrefix(prefSum, Integer::sum);

        for (int i = 0; i < prefSum.length; i++) {
            prefSum[i] += i;
        }

        maxWidth = Arrays.stream(maxLen).sum() + 8;

        StringBuilder line = new StringBuilder("-".repeat(maxWidth - 8));

        Arrays.stream(prefSum)
                .forEach(value -> line.insert(value, "+"));

        builder.append("+").append(line).append("\n");
        builder.append("|");
        for (int i = 0; i < cols.size(); i++) {
            builder.append(header.get(cols.get(i)));
            builder.append(" ".repeat(Math.max(0, maxLen[i] - header.get(cols.get(i)).length())));
            builder.append("|");
        }
        builder.append("\n");
        buildLine(builder, maxWidth, prefSum, line);

        for (Day day : week) {
            if (days.contains(day.getName())) {
                for (Subject subject : day.getSubjects()) {
                    builder.append("|");
                    for (int i = 0; i < cols.size(); i++) {
                        builder.append(subject.getData().get(cols.get(i)));
                        builder.append(" ".repeat(Math.max(0, maxLen[i] - subject.getData().get(cols.get(i)).length())));
                        builder.append("|");
                    }
                    builder.append("\n");
                }
                buildLine(builder, maxWidth, prefSum, line);
            }
        }

        return builder.toString();
    }

    public static String buildWeekTable(List<Day> week) {
        return buildTable(week, List.of(0, 1, 2, 3, 4, 5, 6), List.of("Пн", "Вт", "Ср", "Чт", "Пт", "Сб"));
    }

    public static String buildWeekShortTable(List<Day> week) {
        return buildTable(week, List.of(0, 1, 4), List.of("Пн", "Вт", "Ср", "Чт", "Пт", "Сб"));
    }

    public static String buildDayTable(List<Day> week, String day) {
        return buildTable(week, List.of(0, 1, 2, 3, 4, 5, 6), List.of(day));
    }

    public static String buildDayShortTable(List<Day> week, String day) {
        return buildTable(week, List.of(0, 1, 4), List.of(day));
    }

    private static void buildLine(StringBuilder builder, int maxWidth, int[] prefSum, StringBuilder line) {
        line.delete(0, line.length());
        line.append("+").append("-".repeat(maxWidth - 8));
        Arrays.stream(prefSum)
                .forEach(value -> line.insert(value + 1, "+"));
        builder.append(line);
        builder.append("\n");
    }
}
