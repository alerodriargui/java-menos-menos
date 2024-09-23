JAVAC = javac
JAVA = java
FLEX = java -jar lib/jflex-full-1.8.2.jar
CUP = java -jar lib/java-cup-11b.jar
BUILD_DIR = build

# Regla por defecto para compilar el compilador
all: $(BUILD_DIR)/parser.java $(BUILD_DIR)/sym.java $(BUILD_DIR)/Yylex.java
	$(JAVAC) -d $(BUILD_DIR) -classpath lib/java-cup-11b-runtime.jar src/Main.java $(BUILD_DIR)/Yylex.java $(BUILD_DIR)/parser.java


# Generar el lexer con Flex
$(BUILD_DIR)/Yylex.java: src/lexer.flex
	$(FLEX) -d $(BUILD_DIR) src/lexer.flex

# Generar el parser con CUP
$(BUILD_DIR)/parser.java $(BUILD_DIR)/sym.java: src/parser.cup
	$(CUP) -destdir $(BUILD_DIR) -parser parser -symbols sym src/parser.cup

# Ejecutar el compilador
run: all
	$(JAVA) -classpath build:lib/java-cup-11b-runtime.jar Main input.txt

# Limpiar archivos generados
clean:
	rm -rf $(BUILD_DIR)/*.class $(BUILD_DIR)/*.java
