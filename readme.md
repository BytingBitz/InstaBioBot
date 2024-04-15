# InstaBioBot

***

# About:

> Note: It is your responsibility to read and understand the disclaimer.

This Kotlin application automates an Instagram acount bio, it is a rewrite
of [Python instagram-bot](https://github.com/Jamal135/instagram-bot).

The application creates a [Selenium](https://github.com/SeleniumHQ/docker-selenium) browser session,
logs into Instagram, and navigates to account settings. From here the application calls `buildText` in `utilities.Bio`
every ~2 seconds, updating your Instagram account bio when `buildText` returns a result different to your current bio.

The application has been built to minimise requests, it will exit gracefully if it encounters consecutive errors.

```kt
object Bio {

    // buildText called every ~2s, bio updates when return is not current text
    // NOTE: it is strongly recommended to not update more than once each hour

    fun buildText(): String {
        val currentDateTime = LocalDateTime.now(ZoneId.of("Australia/Queensland"))
        val hourFormatter = DateTimeFormatter.ofPattern("ha", Locale.getDefault())
        val hour = currentDateTime.format(hourFormatter).lowercase(Locale.getDefault())
        val day = currentDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        return "It is $hour on a $day..."
    }

}
```

This project represents my first time using Kotlin and all in all was a great experience - mostly.

Creation Date: 15/04/2024

*** 

# Setup:

```cmd
git clone https://github.com/Jamal135/InstaBioBot
```

w
This application requires a `.env` file containing your Instagram login credentials to function. Create the `.env`
function in the root directory. Ensure values are enclosed in `'`s - this is required as a result of
how `System.getenv()` loads docker environment variables. See
relevant [issue](https://github.com/docker/compose/issues/8607).

```env
USER='username'
PASS='password'
```

After cloning the repository and adding the `.env` file, all you need to do is build and start the selenium and
instaBioBot images using `docker-compose`:

```cmd
docker-compose build
docker-compose up -d
```

Some other useful commands include:

```cmd
docker-compose down
docker system prune -a
docker ps
```

***

# Contribution:

New ideas and contributions are welcome. Please open an issue.

***

# Disclaimer:

WARNING: Automating interactions on Instagram, including updates to your account bio, likely violates Instagram's
terms of service and could result in the temporary or permanent suspension of your Instagram account. This is an
application made for entertainment purposes, though you are using this application at your own risk.

While efforts have been made to ensure this application is safe to use, the developer cannot guarantee using this
application will not lead to potential repercussions.

***

# Future:

I will continue to fix bugs as they develop where possible, though future changes to Instagram may permanently break
this application. Additionally, I may look at implementing a range of options for what kind
of bio texts `utilities.Bio` can generate.

***

# License:

Copyright (c) [@Jamal135](https://github.com/Jamal135)

MIT License
