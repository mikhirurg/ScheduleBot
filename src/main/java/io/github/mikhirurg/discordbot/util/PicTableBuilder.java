package io.github.mikhirurg.discordbot.util;

import io.github.mikhirurg.discordbot.model.Day;
import io.github.mikhirurg.discordbot.model.Subject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class PicTableBuilder {

    private static class Cursor {
        int x = 0, y = 0;
    }

    private static final double scaleX = GraphicsEnvironment.isHeadless() ? 1 : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
            .getDefaultConfiguration().getDefaultTransform().getScaleX();
    private static final double scaleY = GraphicsEnvironment.isHeadless() ? 1 :GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
            .getDefaultConfiguration().getDefaultTransform().getScaleY();

    private static final int logoWidth = 150;
    private static final int fontSize = 20;

    public static BufferedImage buildTable(List<Day> week, List<Integer> cols, List<String> days) throws IOException {
        int maxWidth;
        BufferedImage dummy = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        BufferedImage logo = ImageIO.read(PicTableBuilder.class.getClassLoader().getResourceAsStream("logo.png"));
        int width = logoWidth;
        int height = (int) (logo.getHeight() * ((double) width / logo.getWidth()));
        BufferedImage logo2 = logo.getSubimage(0,0, width, height);
        logo2.getGraphics().setColor(Color.white);
        logo2.getGraphics().fillRect(0, 0, width, height);
        logo2.getGraphics().drawImage(logo.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        logo = logo2;

        Font boldFont = new Font(Font.MONOSPACED, Font.BOLD, fontSize);
        Font ordinaryFont = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
        dummy.getGraphics().setFont(boldFont);

        int[] maxLenPixel = new int[cols.size()];
        int[] maxLenChar = new int[cols.size()];

        List<String> header = List.of("День", "Время", "Кабинет", "Адрес", "Предмет", "Преподаватель", "Формат занятия");
        for (int i = 0; i < cols.size(); i++) {
            maxLenPixel[i] = (int) (Math.max(maxLenPixel[i], dummy.getGraphics()
                                .getFontMetrics().stringWidth(header.get(cols.get(i))) * scaleX));
            maxLenChar[i] = Math.max(maxLenChar[i], header.get(cols.get(i)).length());
        }

        for (Day day : week) {
            for (Subject subject : day.getSubjects()) {
                for (int i = 0; i < cols.size(); i++) {
                    maxLenPixel[i] = (int) (Math.max(maxLenPixel[i], dummy.getGraphics()
                                                .getFontMetrics().stringWidth(subject.getData().get(cols.get(i))) * scaleX));
                    maxLenChar[i] = Math.max(maxLenChar[i], subject.getData().get(cols.get(i)).length());
                }
            }
        }

        maxWidth = Arrays.stream(maxLenPixel).sum() * 2;
        int maxHeight = (int) ((week.stream()
                                .mapToInt(day -> day.getSubjects()
                                        .size())
                                .sum() * 2) * dummy
                                .getGraphics()
                                .getFontMetrics()
                                .getHeight() * scaleY);

        BufferedImage image = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

        Cursor cursor = new Cursor();
        cursor.y += g2d.getFontMetrics().getHeight();

        g2d.setColor(new Color(25, 70, 186));
        g2d.fillRect(0, 0, image.getWidth(), (int) (g2d.getFontMetrics().getHeight() * scaleY));

        g2d.setColor(Color.BLACK);
        g2d.setFont(boldFont);
        for (int i = 0; i < cols.size(); i++) {
            g2d.drawString(header.get(cols.get(i)), cursor.x, cursor.y);
            cursor.x += g2d.getFontMetrics().stringWidth(header.get(cols.get(i)) +
                    " ".repeat(Math.max(0, maxLenChar[i] - header.get(cols.get(i)).length())));
            cursor.x += 10;
        }
        cursor.y += g2d.getFontMetrics().getHeight() * scaleY;

        g2d.setFont(ordinaryFont);
        for (Day day : week) {
            if (days.contains(day.getName())) {
                for (Subject subject : day.getSubjects()) {
                    cursor.x = 0;
                    for (int i = 0; i < cols.size(); i++) {
                        g2d.drawString(subject.getData().get(cols.get(i)), cursor.x, cursor.y);
                        cursor.x += g2d.getFontMetrics().stringWidth(subject.getData().get(cols.get(i)) +
                                        " ".repeat(Math.max(0, maxLenChar[i] - subject.getData().get(cols.get(i)).length())));
                        cursor.x += 10;
                    }
                    cursor.y += g2d.getFontMetrics().getHeight() * scaleY;
                }
            }
        }

        image = image.getSubimage(0,0, cursor.x, cursor.y);
        g2d = (Graphics2D) image.getGraphics();
        g2d.drawImage(logo, image.getWidth() - logo.getWidth(), image.getHeight() - height, null);

        return image;
    }

    public static BufferedImage buildWeekTable(List<Day> week) throws IOException {
        return buildTable(week, List.of(0, 1, 2, 3, 4, 5, 6), List.of("Пн", "Вт", "Ср", "Чт", "Пт", "Сб"));
    }

    public static BufferedImage buildWeekShortTable(List<Day> week) throws IOException {
        return buildTable(week, List.of(0, 1, 4), List.of("Пн", "Вт", "Ср", "Чт", "Пт", "Сб"));
    }

    public static BufferedImage buildDayTable(List<Day> week, String day) throws IOException {
        return buildTable(week, List.of(0, 1, 2, 3, 4, 5, 6), List.of(day));
    }

    public static BufferedImage buildDayShortTable(List<Day> week, String day) throws IOException {
        return buildTable(week, List.of(0, 1, 4), List.of(day));
    }
}
