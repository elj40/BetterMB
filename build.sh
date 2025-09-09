set -xe

javac -d build src/Meal.java
javac -d build -cp "lib/*:build" src/Client.java
#javac -d build -cp "lib:build" src/MockServer.java

javac -d build -cp "lib/*:build" src/Tester.java
java -cp "lib/*:build" org.junit.platform.console.ConsoleLauncher --scan-classpath
