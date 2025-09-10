set -xe

javac -d build src/Meal.java
javac -d build -cp "lib/*:build" src/Client.java
javac -d build -cp "lib/*:build" src/Main.java
javac -d build -cp "lib/*:build" src/MockServer.java

#For testing
javac -d build -cp "lib/*:build" src/Tester.java
java -cp "lib/*:build" org.junit.platform.console.ConsoleLauncher --scan-classpath

java -cp "lib/*:build" Main
