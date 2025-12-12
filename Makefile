# Makefile
SRC_DIR := src
BIN_DIR := bin
LIB_DIR := lib
GSON_JAR := $(LIB_DIR)/gson-2.10.1.jar
CLASSPATH := $(BIN_DIR):$(GSON_JAR)

SOURCES := $(shell find $(SRC_DIR) -name '*.java')

run: $(BIN_DIR) compile
	java -cp "$(CLASSPATH)" App

$(BIN_DIR):
	$(MKDIR_P) $(BIN_DIR)

compile: $(BIN_DIR)
	javac -cp "$(GSON_JAR)" -d $(BIN_DIR) $(SOURCES)