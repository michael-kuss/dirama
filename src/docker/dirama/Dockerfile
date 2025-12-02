# https://hub.docker.com/_/amazoncorretto
FROM amazoncorretto:17-alpine as build

MAINTAINER dirama@michael-kuss.de
ENV TINI_VERSION v0.19.0

# required for strip-debug to work

RUN apk add --no-cache binutils

RUN architecture=""; \
        case $(uname -m) in \
            x86_64) architecture="-amd64" ;; \
            aarch64) architecture="-armel" ;; \
            arm64) architecture="-arm64" ;; \
        esac ;\
    apk add -u curl; curl -Lsf https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini-static${architecture} -o /tini ; \
    chmod +x /tini

RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /customjre

FROM alpine:latest as prepare

ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV APP_HOME_DIR=/app

# copy JRE from the base image
COPY --from=build /customjre $JAVA_HOME

# Add app user
ENV APPLICATION_USER=dirama
RUN adduser -H -D $APPLICATION_USER
RUN sed -r 's/^$APPLICATION_USER:!:/$APPLICATION_USER:x:/' /etc/shadow

RUN apk add --no-cache -u bash curl bc libxml2-utils msttcorefonts-installer fontconfig
RUN update-ms-fonts

# Configure working directory
RUN mkdir ${APP_HOME_DIR} && chown -R $APPLICATION_USER:$APPLICATION_USER ${APP_HOME_DIR}
COPY harden.sh /usr/bin/harden
RUN /usr/bin/harden

WORKDIR ${APP_HOME_DIR}
COPY --chown=$APPLICATION_USER:$APPLICATION_USER --from=build  /tini ./tini
RUN chmod 777 /tmp

FROM scratch as jdk_template

MAINTAINER dirama@michael-kuss.de

COPY --from=prepare / /

ENV APPLICATION_USER=dirama
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV APP_HOME_DIR=/app

USER $APPLICATION_USER
WORKDIR ${APP_HOME_DIR}

# https://hub.docker.com/_/amazoncorretto
FROM jdk_template AS app_builder
#
# LAS REST Server (spring-boot application) as docker container
#
# Note: the JAR file that will get added to the container must be created first using gradle tasks.
#

# add executable JAR (spring-boot)
# Note: the JAR file must be created first using gradle tasks.
ADD ["*.jar", "application.jar"]

RUN java -Djarmode=layertools -jar *.jar extract --destination ${APP_HOME_DIR}


FROM app_builder

ARG JAVA_TOOL_OPTIONS

ENV LOG_DIR ${APP_HOME_DIR}/data/logs
ENV KEYSTORE_FILE ${APP_HOME_DIR}/keystore.jks
ENV JAVA_TOOL_OPTIONS ${JAVA_TOOL_OPTIONS}

VOLUME ${APP_HOME_DIR}/data/logs

COPY --chown=root:root entrypoint.sh /
RUN mkdir -p ${APP_HOME_DIR}/data/logs
COPY --chown=$APPLICATION_USER:$APPLICATION_USER --from=app_builder ${APP_HOME_DIR}/dependencies/ ./
COPY --chown=$APPLICATION_USER:$APPLICATION_USER --from=app_builder ${APP_HOME_DIR}/spring-boot-loader/ ./
COPY --chown=$APPLICATION_USER:$APPLICATION_USER --from=app_builder ${APP_HOME_DIR}/snapshot-dependencies/ ./
COPY --chown=$APPLICATION_USER:$APPLICATION_USER --from=app_builder ${APP_HOME_DIR}/application/ ./

EXPOSE 8080

ENTRYPOINT [ "./tini", "--", "/entrypoint.sh"]
