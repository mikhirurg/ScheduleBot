package io.github.mikhirurg.discordbot.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day {
    private final List<Subject> subjects;
    private final String name;

    public Day(List<Subject> subjects) {
        this.subjects = new LinkedList<>();
        this.subjects.addAll(subjects);
        name = subjects
                .stream()
                .findFirst()
                .map(Subject::getDay)
                .orElse(null);
    }

    public String toString() {
        return subjects
                .stream()
                .map(Subject::toString)
                .collect(Collectors.joining("\n"));
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public String getName() {
        return name;
    }
}
