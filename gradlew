#!/bin/sh
# Gradle Wrapper 启动脚本（用于 Linux/Mac/GitHub Actions）
# Windows 用户用 gradlew.bat 或直接用 Android Studio 打开

DIR="$(cd "$(dirname "$0")" && pwd)"
APP_HOME="$DIR"

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

exec java -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
