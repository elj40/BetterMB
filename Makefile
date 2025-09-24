SOURCE_PATH = src/main
TEST_PATH = src/test
LIB = lib
DATA     = data
BUILD_PATH = build
MEAL_CLASSES = $(BUILD_PATH)/Meal.class $(BUILD_PATH)/MealSlot.class

   CLI_CLASS = $(BUILD_PATH)/CLI.class
  USER_CLASS = $(BUILD_PATH)/User.class
CLIENT_CLASS = $(BUILD_PATH)/Client.class
SERVER_CLASS = $(BUILD_PATH)/MockServer.class
COMMON_CLASS = $(BUILD_PATH)/Common.class

DEPENDENCIES = \
$(LIB)/apiguardian-api-1.1.2.jar \
$(LIB)/gson-2.13.1.jar \
$(LIB)/jspecify-1.0.0.jar \
$(LIB)/junit-jupiter-api-5.13.4.jar \
$(LIB)/junit-jupiter-engine-5.13.4.jar \
$(LIB)/junit-platform-commons-1.13.4.jar \
$(LIB)/junit-platform-console-standalone-1.13.4.jar \
$(LIB)/junit-platform-engine-1.13.4.jar \
$(LIB)/opentest4j-1.3.0.jar \
$(LIB)/selenium-java-4.35.0.zip

$(BUILD_PATH)/CustomStubHttpClient.class: $(TEST_PATH)/CustomStubHttpClient.java $(BUILD_PATH)/IHttpClient.class
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(BUILD_PATH)" $(TEST_PATH)/CustomStubHttpClient.java

$(BUILD_PATH)/StubBookHttpClient.class: $(TEST_PATH)/StubBookHttpClient.java $(BUILD_PATH)/IHttpClient.class
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(BUILD_PATH)" $(TEST_PATH)/StubBookHttpClient.java

$(BUILD_PATH)/%Exception.class: $(SOURCE_PATH)/%Exception.java
	javac -d $(BUILD_PATH) $(SOURCE_PATH)/$*Exception.java

EXCEPTION_CLASSES = $(patsubst $(SOURCE_PATH)/%.java,$(BUILD_PATH)/%.class,$(wildcard $(SOURCE_PATH)/*Exception.java))

$(BUILD_PATH)/ClientSignInTest.class: $(TEST_PATH)/ClientSignInTest.java $(CLIENT_CLASS)
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(LIB_PATH)/selenium/*:$(BUILD_PATH)" $(TEST_PATH)/ClientSignInTest.java

$(BUILD_PATH)/%Test.class: $(TEST_PATH)/%Test.java $(BUILD_PATH)/CustomStubHttpClient.class $(BUILD_PATH)/IHttpClient.class $(EXCEPTION_CLASSES) $(CLIENT_CLASS) $(USER_CLASS) $(MEAL_CLASSES)
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(BUILD_PATH)" $(TEST_PATH)/$*Test.java

$(BUILD_PATH)/CLI%Test.class: $(TEST_PATH)/CLI%Test.java $(BUILD_PATH)/CustomStubHttpClient.class $(BUILD_PATH)/IHttpClient.class $(EXCEPTION_CLASSES) $(CLIENT_CLASS) $(USER_CLASS) $(MEAL_CLASSES) $(CLI_CLASS)
	javac -d $(BUILD_PATH) -cp "$(LIB_PATH)/*:$(BUILD_PATH)" $(TEST_PATH)/CLI$*Test.java

TEST_CLASSES = $(patsubst $(TEST_PATH)/%.java,$(BUILD_PATH)/%.class,$(wildcard $(TEST_PATH)/*Test.java))


$(BUILD_PATH)/Manual.class: $(TEST_PATH)/Manual.java $(CLIENT_CLASS) $(USER_CLASS) $(CLI_CLASS) $(BUILD_PATH)/StubBookHttpClient.class
	javac -d build -cp "lib/*:lib/selenium/*:$(BUILD_PATH)" $(TEST_PATH)/Manual.java

testmanual: $(BUILD_PATH)/Manual.class
	java -cp "$(BUILD_PATH):$(LIB_PATH)/*:$(LIB_PATH)/selenium/*" Manual

$(BUILD_PATH)/IHttpClient.class: $(SOURCE_PATH)/IHttpClient.java
	javac -d build $(SOURCE_PATH)/IHttpClient.java

$(BUILD_PATH)/HttpClientImpl.class: $(SOURCE_PATH)/HttpClientImpl.java
	javac -d build -cp "$(BUILD_PATH)" $(SOURCE_PATH)/HttpClientImpl.java

$(BUILD_PATH)/MealSlot.class: $(SOURCE_PATH)/Meal.java
	javac -d build $(SOURCE_PATH)/Meal.java

$(BUILD_PATH)/Meal.class: $(SOURCE_PATH)/Meal.java
	javac -d build $(SOURCE_PATH)/Meal.java

$(BUILD_PATH)/Common.class: $(SOURCE_PATH)/Common.java
	javac -d build $(SOURCE_PATH)/Common.java

$(BUILD_PATH)/Client.class: $(SOURCE_PATH)/Client.java $(BUILD_PATH)/IHttpClient.class $(EXCEPTION_CLASSES) $(MEAL_CLASSES) $(COMMON_CLASS)
	javac -d build -cp "lib/*:lib/selenium/*:$(BUILD_PATH)" $(SOURCE_PATH)/Client.java

$(BUILD_PATH)/User.class: $(SOURCE_PATH)/User.java $(MEAL_CLASSES)
	javac -d build -cp "lib/*:$(BUILD_PATH)" $(SOURCE_PATH)/User.java

$(BUILD_PATH)/MockServer.class: $(SOURCE_PATH)/MockServer.java $(MEAL_CLASSES) $(COMMON_CLASS)
	javac -d build -cp "lib/*:$(BUILD_PATH)" $(SOURCE_PATH)/MockServer.java

$(BUILD_PATH)/ScannerCLI.class: $(SOURCE_PATH)/ScannerCLI.java
	javac -d build $(SOURCE_PATH)/ScannerCLI.java

$(CLI_CLASS): $(SOURCE_PATH)/CLI.java $(CLIENT_CLASS) $(USER_CLASS) $(MEAL_CLASS) $(BUILD_PATH)/ScannerCLI.class
	javac -d build -cp "lib/*:lib/selenium/*:$(BUILD_PATH)" $(SOURCE_PATH)/CLI.java

$(BUILD_PATH)/Main.class: $(SOURCE_PATH)/Main.java $(CLIENT_CLASS) $(MEAL_CLASSES) $(USER_CLASS) $(CLI_CLASS) $(BUILD_PATH)/HttpClientImpl.class
	javac -d build -cp "lib/*:lib/selenium/*:$(BUILD_PATH)" $(SOURCE_PATH)/Main.java

$(LIB)/apiguardian-api-1.1.2.jar:
	curl -o $(LIB)/apiguardian-api-1.1.2.jar https://repo1.maven.org/maven2/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar

$(LIB)/gson-2.13.1.jar:
	curl -o $(LIB)/gson-2.13.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.13.1/gson-2.13.1.jar
	
$(LIB)/jspecify-1.0.0.jar:
	curl -o $(LIB)/jspecify-1.0.0.jar https://repo1.maven.org/maven2/org/jspecify/jspecify/1.0.0/jspecify-1.0.0.jar

$(LIB)/junit-jupiter-api-5.13.4.jar:
	curl -o $(LIB)/junit-jupiter-api-5.13.4.jar https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.13.4/junit-jupiter-api-5.13.4.jar

$(LIB)/junit-jupiter-engine-5.13.4.jar:
	curl -o $(LIB)/junit-jupiter-engine-5.13.4.jar https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/5.13.4/junit-jupiter-engine-5.13.4.jar

$(LIB)/junit-platform-commons-1.13.4.jar:
	curl -o $(LIB)/junit-platform-commons-1.13.4.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-commons/1.13.4/junit-platform-commons-1.13.4.jar

$(LIB)/junit-platform-console-standalone-1.13.4.jar:
	curl -o $(LIB)/junit-platform-console-standalone-1.13.4.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.13.4/junit-platform-console-standalone-1.13.4.jar

$(LIB)/junit-platform-engine-1.13.4.jar:
	curl -o $(LIB)/junit-platform-engine-1.13.4.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-engine/1.13.4/junit-platform-engine-1.13.4.jar

$(LIB)/opentest4j-1.3.0.jar:
	curl -o $(LIB)/opentest4j-1.3.0.jar https://repo1.maven.org/maven2/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar

$(LIB)/selenium-java-4.35.0.zip:
	curl -L -o $(LIB)/selenium-java-4.35.0.zip https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.35.0/selenium-java-4.35.0.zip

dependencies: $(DEPENDENCIES)

test: $(TEST_CLASSES) $(EXCEPTION_CLASSES) $(CLIENT_CLASS) $(USER_CLASS) $(CLI_CLASS)
	java -cp "$(BUILD_PATH):$(LIB_PATH)/*:$(LIB_PATH)/selenium/*" org.junit.platform.console.ConsoleLauncher --scan-classpath

all: $(BUILD_PATH)/Main.class $(BUILD_PATH)/MockServer.class

run: $(BUILD_PATH)/Main.class
	java -cp "lib/*:lib/selenium/*:$(BUILD_PATH)" Main

clean:
	rm -rf $(BUILD_PATH)/
	rm $(DATA)/*.json
	mkdir $(BUILD_PATH)

.PHONY: all
