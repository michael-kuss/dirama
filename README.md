# Digital Radio Manager 0.5.0

## QuickStart

Clone git:
```bash
git clone https://github.com/michael-kuss/dirama.git
```

Just call

```bash
gradlew clean build
java -jar server/build/libs/dirama-service-0.5.0.jar
```

## REST Endpoints

### nowplaying

---

POST  (protected)               | ``/nowplaying``
:--                             | --:
``?ignoreNow``   (optional)     | do not check date if it is now (default true)
Add a list of titles as JSON to the database

---

POST  (protected)               | ``/nowplaying/{station}``
:--                             | --:
``{station}``                   | Name of the station (free to choose)
``?artist``                     | name of the artist
``?title``                      | name of the title
``?dabImage``                   | name of the dab image
``?webImage``                   | name of the web image
``?time``                       | start time of the title in yyyyMMddHHmmss
``?additional1`` (optional)     | free to use field
``?additional2`` (optional)     | free to use field
``?additional3`` (optional)     | free to use field
``?additional4`` (optional)     | free to use field
``?additional5`` (optional)     | free to use field
``?ignoreNow``   (optional)     | do not check date if it is now (default true)
Add title to the database

---

GET                             | ``/nowplaying/{station}``
:--                             | --:
``{station}``                   | Name of the station (free to choose)
``?page`` (optional)            | the page to get (default 0)
``?size`` (optional)            | the number of titles to get (default 50)
``?full`` (optional)            | get full record (default false)
Get the history of played titles ordered by time descending
Get the current title only with size=1

### history

---

GET                             | ``/history/artist/{artist}``
:--                             | --:
``{artist}``                    | Name of the artist or part of it
``?page`` (optional)            | the page to get (default 0)
``?size`` (optional)            | the number of titles to get (default 50)
``?full`` (optional)            | get full record (default false)
Get the history of played titles by artist ordered by time descending
Get the last played title by the artist only with size=1

---

GET                             | ``/history/title/{title}``
:--                             | --:
``{title}``                     | Name of the title or part of it
``?page`` (optional)            | the page to get (default 0)
``?size`` (optional)            | the number of titles to get (default 50)
``?full`` (optional)            | get full record (default false)
Get the history of played titles by title ordered by time descending
Get the last played title only with size=1

---

GET                             | ``/stations``
:--                             | --:
Get the stations known to the system

---

DELETE  (protected)             | ``/{id}``
:--                             | --:
``{id}``                        | Internal id of the title (ask with ``full=true``)
Delete the title

### trigger

---

POST  (protected)               | ``/trigger``
:--                             | --:
Add a list of triggers as JSON to the database

---

DELETE  (protected)             | ``/trigger/{id}``
:--                             | --:
``{id}``                        | Internal id of the trigger
Delete the trigger

---

GET  (protected)                | ``/trigger``
:--                             | --:
``?page`` (optional)            | the page to get (default 0)
``?size`` (optional)            | the number of triggers to get (default 50)
Get a list of triggers as JSON from the database

---

POST  (protected)               | ``/trigger/{id}/{station}``
:--                             | --:
``{id}``                        | Internal id of the trigger
``{station}``                   | Name of the station (free to choose)
Execute the trigger with the last played title from the station

### config

---

POST  (protected)               | ``/config``
:--                             | --:
Add a list of configs as JSON to the database

---

DELETE  (protected)             | ``/config/{id}``
:--                             | --:
``{id}``                        | Internal id of the config
Delete the config

---

GET  (protected)                | ``/config``
:--                             | --:
``?page`` (optional)            | the page to get (default 0)
``?size`` (optional)            | the number of configs to get (default 50)
Get a list of configs as JSON from the database

---

POST  (protected)               | ``/config/stored/{token}``
:--                             | --:
``{token}``                     | secred token defined in app config (``read.config.token``)
Read the internal configs defined in the application configuration as JSON.
Protect wisely or disable with ``-``.
The internal config is for passwords, the REST config for dynamic changes.

## Exampledata
```json
{
"cause": "#title.station=='bayern'",
"action":"actionSendFile('doAction', '005.jpg', 'http://www.berlinsdanceradio.de/wp-content/uploads/' + #title.dabImage, 'http://www.berlinsdanceradio.de/wp-content/uploads/news-dab.jpg')"
}
```

## Buildsystem
We are using [gradle](http://www.gradle.org) to build our software.
The project is using the [gradle wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html)
so there is no need to download any additional software.
The only prerequisite is an installed JDK >= 1.7.

## Eclipse
To get a eclipse project configuration type the following and import the project into eclipse:

```bash
gradlew eclipse
```
