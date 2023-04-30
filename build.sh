if command -v gradle >/dev/null 2>&1; then
    echo "Gradle is installed."
    gradle clean shadowJar --exclude-task test
else
    echo "Gradle is not installed."
    echo "Gradle wrapper will be used."
    ./gradlew clean shadowJar --exclude-task test
fi
