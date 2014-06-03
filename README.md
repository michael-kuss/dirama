# Digital Radio Manager

## QuickStart

Clone git:
```bash
git clone https://github.com/michael-kuss/dirama.git
```

Just call

```bash
gradlew clean build
java -jar server/build/libs/dirama-service-0.1.0.jar
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
