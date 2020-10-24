#!/bin/sh
echo "token=$TOKEN" > bot-settings.properties
echo "save=save.txt" >> bot-settings.properties
java -jar ScheduleBot.jar bot-settings.properties
