#!/bin/sh -e

# [ -f ${KEYSTORE_FILE} ] && export JAVA_TOOL_OPTIONS="-Dserver.ssl.key-store=${KEYSTORE_FILE} ${JAVA_TOOL_OPTIONS}"

exec java org.springframework.boot.loader.launch.JarLauncher $@
