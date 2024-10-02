# Variables for paths and files
JAR_PATH = lib/jflex-full-1.8.2.jar
CUP_JAR = lib/java-cup-11b.jar
CUP_RUNTIME = lib/java-cup-11b-runtime.jar
SOURCE = Main.java
FLEX_FILE = lcalc.flex
CUP_FILE = ycalc.cup
BIN_DIR = bin
TEST_FILE = tests/test.txt

# Default target to run the application
all: go

# Run the go script tasks
go:
	@mkdir -p $(BIN_DIR)
	@java -jar $(JAR_PATH) $(FLEX_FILE)
	@java -cp .:$(CUP_JAR) java_cup.Main < $(CUP_FILE)
	@javac -d $(BIN_DIR) -cp .:$(CUP_JAR) $(SOURCE)
	@java -cp $(BIN_DIR):$(CUP_RUNTIME) Main $(TEST_FILE)

# Clean target to remove generated files
clean:
	@rm -f Lexer.java
	@rm -f Lexer.java~
	@rm -f parser.java
	@rm -f sym.java
	@rm -f $(BIN_DIR)/*.class
	@rm -rf $(BIN_DIR)

.PHONY: all go clean