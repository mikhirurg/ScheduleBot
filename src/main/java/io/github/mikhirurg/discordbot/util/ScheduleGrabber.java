package io.github.mikhirurg.discordbot.util;

import io.github.mikhirurg.discordbot.exceptions.BadResponseCodeException;
import io.github.mikhirurg.discordbot.exceptions.ReadDataException;
import io.github.mikhirurg.discordbot.model.Day;
import io.github.mikhirurg.discordbot.model.Subject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class ScheduleGrabber {
    private final static String tableBorderOpen = "<article class=\"content_block\" style=\"position:relative;\">";
    private final static String tableBorderClose = "</article>";

    public static final int WHOLE_WEEK = 0;
    public static final int ODD_WEEK = 1;
    public static final int EVEN_WEEK = 2;

    public static List<Day> getSchedule(String groupName, int week) throws IOException {
        String weekStr = "";
        switch (week) {
            case 1:
                weekStr = "1/";
                break;
            case 2:
                weekStr = "2/";
                break;
        }

        URL url = new URL("https://itmo.ru/ru/schedule/0/" + groupName + "/" + weekStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        if (status == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder content = new StringBuilder();
                content.append("<html>\n");
                content.append("<body>");

                boolean start = false;
                while ((line = in.readLine()) != null) {
                    if (line.contains(tableBorderOpen)) {
                        start = true;
                        continue;
                    } else if (line.contains(tableBorderClose) && start)
                        break;
                    if (start) {
                        content.append(line).append("\n");
                    }
                }
                content.append("</body>");
                content.append("</html>");

                Document document = Jsoup.parse(content.toString());

                Element schedule = document.child(0).child(1);

                String currentDay = "";
                List<Day> workingWeek = new LinkedList<>();
                List<Subject> workingDay;

                for (Element node : schedule.children()) {
                    if (node.className().equals("rasp_tabl_day")) {
                        Element subjects = node.child(0).child(0);
                        workingDay = new LinkedList<>();

                        for (Element subject : subjects.children()) {
                            if (subject.tagName().equals("tr") && subject.children().size() > 0) {
                                String day = subject
                                        .getElementsByClass("day")
                                        .first()
                                        .getElementsByTag("span")
                                        .stream()
                                        .findFirst()
                                        .map(Element::ownText)
                                        .orElse(null);

                                if (day != null)
                                    currentDay = day;

                                String time = subject
                                        .getElementsByClass("time")
                                        .first()
                                        .getElementsByTag("span")
                                        .first()
                                        .ownText();

                                String room = subject
                                        .getElementsByClass("room")
                                        .first()
                                        .getElementsByTag("dd")
                                        .first()
                                        .ownText();

                                String lesson = subject
                                        .getElementsByClass("lesson")
                                        .first()
                                        .getElementsByTag("dd")
                                        .first()
                                        .ownText();

                                String teacher = subject
                                        .getElementsByClass("lesson")
                                        .first()
                                        .getElementsByTag("dt")
                                        .first()
                                        .getElementsByTag("b")
                                        .first()
                                        .children()
                                        .stream()
                                        .findAny()
                                        .map(Element::ownText)
                                        .orElse(subject.getElementsByClass("lesson")
                                                .first()
                                                .getElementsByTag("dt")
                                                .first()
                                                .getElementsByTag("b").first().ownText());

                                String lessonFormat = subject
                                        .getElementsByClass("lesson-format")
                                        .first()
                                        .ownText();

                                String address = subject
                                        .getElementsByClass("room")
                                        .first()
                                        .getElementsByTag("dt")
                                        .first()
                                        .children()
                                        .stream()
                                        .skip(1)
                                        .findAny()
                                        .map(Element::ownText)
                                        .orElse(null);

                                workingDay.add(new Subject(currentDay, time, room, address, lesson, teacher, lessonFormat));
                            }
                        }

                        workingWeek.add(new Day(workingDay));
                    }
                }
                return workingWeek;
            } catch (IOException e) {
                throw new ReadDataException();
            }
        } else
            throw new BadResponseCodeException(status);
    }
}
