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

To configure a bot, you need to run the bot app and pass the "bot-settings.properties" file with config data as a command-line argument.

"bot-settings.properties" file format:
````
token=BOT_TOKEN
save=FILE_FOR_LOCAL_STORAGE
````