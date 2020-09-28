package io.github.mikhirurg.discordbot.util;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PicTableBuilderTest {

    @Test
    void buildTable() {
    }

    @Test
    void buildWeekTable() throws IOException {
        ImageIO.write(PicTableBuilder.buildWeekTable(ScheduleGrabber.getSchedule("M3202", ScheduleGrabber.WHOLE_WEEK)), "png", new FileOutputStream("week.png"));
    }

    @Test
    void buildWeekShortTable() {
    }

    @Test
    void buildDayTable() {
    }

    @Test
    void buildDayShortTable() {
    }
}