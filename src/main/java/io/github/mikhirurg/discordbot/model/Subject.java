package io.github.mikhirurg.discordbot.model;

import java.util.List;

public class Subject {
    private final String day;
    private final String time;
    private final String room;
    private final String address;
    private final String name;
    private final String teacher;
    private final String lessonFormat;
    private final List<String> data;


    public Subject(String day, String time, String room, String address, String name, String teacher, String lessonFormat) {
        this.day = day;
        this.time = time;
        this.room = room;
        this.address = address;
        this.name = name;
        this.teacher = teacher;
        this.lessonFormat = lessonFormat;
        data = List.of(day, time, room, address, name, teacher, lessonFormat);
    }

    public String toString() {
        return "|" + day + "|" +
                time + "|" +
                room + "|" +
                address + "|" +
                name + "|" +
                teacher + "|" +
                lessonFormat + "|";
    }

    public String getAddress() {
        return address;
    }

    public String getRoom() {
        return room;
    }

    public String getTime() {
        return time;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getLessonFormat() {
        return lessonFormat;
    }

    public List<String> getData() {
        return data;
    }
}
