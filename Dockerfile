#
# Build stage
#
FROM maven:3.6.3-jdk-14 AS build
COPY src home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package assembly:single

#
# Run stage
#
FROM openjdk:14
COPY --from=build /home/app/target/SimpleDiscordBot-1.0-jar-with-dependencies.jar /home/app/target/ScheduleBot.jar
COPY start.sh /home/app/target/start.sh
WORKDIR /home/app/target
RUN chmod +x start.sh
ENTRYPOINT ["./start.sh"]

