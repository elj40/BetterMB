MEAL_CLASSES = build/Meal.class build/MealSlot.class
CLIENT_CLASS = build/Client.class
SERVER_CLASS = build/MockServer.class

all: build/Main.class build/MockServer.class

run: build/Main.class
	java -cp "lib/*:lib/selenium/*:build" Main

clean:
	rm -rf build/
	mkdir build

build/MealSlot.class: src/Meal.java
	javac -d build src/Meal.java

build/Meal.class: src/Meal.java
	javac -d build src/Meal.java

build/Client.class: src/Client.java $(MEAL_CLASSES)
	javac -d build -cp "lib/*:lib/selenium/*:build" src/Client.java

build/MockServer.class: src/MockServer.java $(MEAL_CLASSES)
	javac -d build -cp "lib/*:build" src/MockServer.java

build/Main.class: src/Main.java $(CLIENT_CLASS) $(SERVER_CLASS) $(MEAL_CLASSES)
	javac -d build -cp "lib/*:lib/selenium/*:build" src/Main.java

.PHONY: all
