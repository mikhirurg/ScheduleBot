![Docker Image CI](https://github.com/mikhirurg/ScheduleBot/tree/master/.github/workflows/docker-build.yml/badge.svg)
![Build Jar](https://github.com/mikhirurg/ScheduleBot/blob/master/.github/workflows/maven.yml/badge.svg)
# ITMO University schedule bot for Discord

![](img/logo.png)

This project is a simple bot that parses the ITMO website and sends a schedule.

Possible commands:
- _!full_ - Sends a full schedule of the week.
- _!short_ - Sends a short schedule of the week.
- _!today_ - Sends a short schedule for today.
- _!tomorrow_ - Sends a short schedule for tomorrow.
- _!today_full_ - Sends a full schedule for today.
- _!tomorrow_full_ - Sends a full schedule for tomorrow.
- _!setgroup <group_number>_ = Set group for the current user.
- _!weekmode_ - Change schedule week style: full schedule or odd-even week schedule.
- _!picmode_ - Change style of schedule: text or image.

# Docker

### Build
```sh
docker build -t schedule_itmo_bot .
```

### Run
```sh
docker run -e TOKEN="<Your Token>" schedule_itmo_bot
```

# Building manually 
Run in command prompt: 
```sh
mvn clean package assembly:single
```

Or easily execute:
```sh
./build.sh
```
