MEAL_CLASSES = build/Meal.class build/MealSlot.class
SOURCE_PATH = src/main
TEST_PATH = src/test
LIB_PATH = lib
BUILD_PATH = build

CLIENT_CLASS = build/Client.class
SERVER_CLASS = build/MockServer.class
COMMON_CLASS = build/Common.class

all: build/Main.class build/MockServer.class

run: build/Main.class
	java -cp "lib/*:lib/selenium/*:build" Main

clean:
	rm -rf build/
	mkdir build

$(BUILD_PATH)/CustomStubHttpClient.class: $(TEST_PATH)/CustomStubHttpClient.java $(BUILD_PATH)/IHttpClient.class
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(BUILD_PATH)" $(TEST_PATH)/CustomStubHttpClient.java

$(BUILD_PATH)/%Exception.class: $(SOURCE_PATH)/%Exception.java
	javac -d $(BUILD_PATH) $(SOURCE_PATH)/$*Exception.java

EXCEPTION_CLASSES = $(patsubst $(SOURCE_PATH)/%.java,$(BUILD_PATH)/%.class,$(wildcard $(SOURCE_PATH)/*Exception.java))

$(BUILD_PATH)/ClientSignInTest.class: $(TEST_PATH)/ClientSignInTest.java $(CLIENT_CLASS)
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(LIB_PATH)/selenium/*:$(BUILD_PATH)" $(TEST_PATH)/ClientSignInTest.java

$(BUILD_PATH)/%Test.class: $(TEST_PATH)/%Test.java $(BUILD_PATH)/CustomStubHttpClient.class $(BUILD_PATH)/IHttpClient.class $(EXCEPTION_CLASSES) $(CLIENT_CLASS) $(MEAL_CLASSES)
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(BUILD_PATH)" $(TEST_PATH)/$*Test.java

TEST_CLASSES = $(patsubst $(TEST_PATH)/%.java,$(BUILD_PATH)/%.class,$(wildcard $(TEST_PATH)/*Test.java))

test: $(TEST_CLASSES) $(EXCEPTION_CLASSES) $(CLIENT_CLASS)
	java -cp "$(BUILD_PATH):$(LIB_PATH)/*:$(LIB_PATH)/selenium/*" org.junit.platform.console.ConsoleLauncher --scan-classpath

build/IHttpClient.class: $(SOURCE_PATH)/IHttpClient.java
	javac -d build $(SOURCE_PATH)/IHttpClient.java

build/MealSlot.class: $(SOURCE_PATH)/Meal.java
	javac -d build $(SOURCE_PATH)/Meal.java

build/Meal.class: $(SOURCE_PATH)/Meal.java
	javac -d build $(SOURCE_PATH)/Meal.java

build/Common.class: $(SOURCE_PATH)/Common.java
	javac -d build $(SOURCE_PATH)/Common.java

build/Client.class: $(SOURCE_PATH)/Client.java $(BUILD_PATH)/IHttpClient.class $(EXCEPTION_CLASSES) $(MEAL_CLASSES) $(COMMON_CLASS)
	javac -d build -cp "lib/*:lib/selenium/*:build" $(SOURCE_PATH)/Client.java

build/MockServer.class: $(SOURCE_PATH)/MockServer.java $(MEAL_CLASSES) $(COMMON_CLASS)
	javac -d build -cp "lib/*:build" $(SOURCE_PATH)/MockServer.java

build/Main.class: $(SOURCE_PATH)/Main.java $(CLIENT_CLASS) $(MEAL_CLASSES)
	javac -d build -cp "lib/*:lib/selenium/*:build" $(SOURCE_PATH)/Main.java

.PHONY: all
