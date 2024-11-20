import core.all.Tuple;
import core.intermediate.Assembly;
import core.intermediate.TresDirCode;
import core.symbol.Symbol;
import core.symbol.SymbolTable;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.SymbolFactory;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


interface Expr {
    Object run(HashMap<String, Object> hm);

    String generateCode(c3Direcciones codeGen);
}

interface Condition {
    boolean test(Expr e1, Expr e2, HashMap<String, Object> hm);
}

interface Operator {
    int count(Expr e1, Expr e2, HashMap<String, Object> hm);
}


interface SimpleInstruction {
    void run(HashMap<String, Object> hm);
}

interface WhileInstructionI extends SimpleInstruction {
    void run(HashMap<String, Object> hm);
}

interface IfInstructionI extends SimpleInstruction {
    void run(HashMap<String, Object> hm);
}


public class Main {

    private HashMap<String, Object> hm = new HashMap<>();
    private MainInstructionList instructionList;

    public Main(MainInstructionList instructionList) {
        this.instructionList = instructionList;
    }

    public void exec() {
        instructionList.run(hm);
    }

    static public void main(String argv[]) {
        try {
            long tInicio = System.currentTimeMillis();
            Lexer l = new Lexer(new FileReader(argv[0]));
            long tFin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución del lexer: " + (tFin - tInicio) + " milisegundos");

            SymbolFactory sf = new ComplexSymbolFactory();

            parser p = new parser(l, sf);
            Object result = p.parse().value;

            // Guardar los tokens en un archivo
            saveTokensFile(l.tokens);

            // Guardar la tabla de símbolos en un archivo
            saveSymbolTableFile(p.getSymbolTable());
            //Back-end
            TresDirCode tresDirCode = new TresDirCode();
            Assembly assembly = new Assembly(tresDirCode, p.getSymbolTable());

            if (result instanceof Main) {
                ((Main) result).exec();
            } else {
                System.err.println("Error: El parser no devolvió una instancia válida de Main.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo para guardar la tabla de simbolos en un archivo
    @SuppressWarnings("unused")
    private static void saveSymbolTableFile(SymbolTable symbolTable) {
        symbolTable.writeToFile("symbolTable.txt");
    }

    // Método para guardar los tokens en un archivo
    private static void saveTokensFile(ArrayList<ComplexSymbol> tokens) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("TokensFitxer.txt"));
            for (int i = 0; i < tokens.size(); i++) {
                writer.write(tokens.get(i).getName() + "\n");
            }
            writer.close();
        } catch (IOException err) {
            System.out.println(err);
        }
    }

}

class FileOutputInstruction implements SimpleInstruction {
    Expr file, content;

    public FileOutputInstruction(Expr s, Expr s2) {
        this.file = s;
        this.content = s2;
    }

    public void run(HashMap<String, Object> hm) {
        Object fileoutput = file.run(hm);
        Object contentoutput = content.run(hm);

        if (fileoutput instanceof String && contentoutput instanceof String) {
            int unused = writeToFile((String) fileoutput, (String) contentoutput);
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
        }
    }

    private int writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine();
            return content.getBytes(StandardCharsets.UTF_8).length + System.lineSeparator().getBytes(StandardCharsets.UTF_8).length;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            System.exit(1);
            return -1;
        }
    }
}


class TupleExpression implements Expr {
    Expr e1, e2;

    public TupleExpression(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        Object value1 = e1.run(hm);
        Object value2 = e2.run(hm);

        return new Tuple(Arrays.asList(value1, value2)); // Crea una lista de los valores
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Genera el código intermedio para las dos expresiones
        String temp1 = e1.generateCode(codeGen);
        String temp2 = e2.generateCode(codeGen);

        // Crear un temporal para la tupla
        String tupleTemp = codeGen.newTemp();

        // Generar la instrucción que define la tupla
        codeGen.addInstruction("TUPLE", temp1, temp2, tupleTemp);

        // Retornar el temporal que contiene la tupla
        return tupleTemp;
    }


}

class ConstIntExpression extends IntExpression {
    private final int value;

    public ConstIntExpression(int value) {
        super(value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

class ConstStringExpression extends StringExpression {
    private final String value;

    public ConstStringExpression(String value) {
        super(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}

class ConstBooleanExpression extends BooleanExpression {
    private final boolean value;

    public ConstBooleanExpression(boolean value) {
        super(value, true); // El segundo argumento indica que es una constante
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}


/**
 * VARS
 */
class ID implements Expr {
    String name;

    public ID(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }

    public Object run(HashMap<String, Object> hm) {
        return hm.get(name);
    }

    public String generateCode(c3Direcciones codeGen) {
        return name;  // El nombre de la variable es el código intermedio en este caso.
    }
}

class AssignInstruction implements SimpleInstruction {
    String name;
    Expr val;

    public AssignInstruction(String i, Expr e) {
        name = i;
        val = e;
    }

    public void run(HashMap<String, Object> hm) {
        hm.put(name, val.run(hm));
    }

    public void generateCode(TresDirCode codeGen) {
        String result = name;
    }

}

class FunctionCallInstruction implements SimpleInstruction {
    private String functionName;
    private List<Object> arguments; // Argumentos para la llamada

    public FunctionCallInstruction(String functionName, List<Object> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public void run(HashMap<String, Object> hm) {
        // Acceder a la tabla de símbolos global
        SymbolTable symbolTable = SymbolTable.getInstance();

        if (symbolTable == null) {
            System.err.println("SymbolTable not found.");
            return;
        }

        // Buscar la función en la tabla de símbolos
        Symbol functionSymbol = symbolTable.get(functionName);
        if (functionSymbol == null) {
            System.err.println("Function " + functionName + " not found.");
            return;
        }

        // Obtener la lista de parámetros de la función
        List<String> params = functionSymbol.getParams();
        if (params.size() != arguments.size()) {
            System.err.println("Argument count mismatch for function: " + functionName);
            return;
        }

        // Crear un nuevo contexto de ejecución para esta función
        HashMap<String, Object> localContext = new HashMap<>(hm);

        // Asignar los valores de los parámetros a las variables locales
        for (int i = 0; i < params.size(); i++) {
            localContext.put(params.get(i), arguments.get(i));
        }

        // Ejecutar las instrucciones de la función
        InstructionList functionInstructions = (InstructionList) functionSymbol.getValue();
        functionInstructions.run(localContext);
    }
}


/**
 * OPERATORS
 */
class PlusOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 + (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String toString() {
        return "PLUS";
    }
}

class TimesOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 * (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String toString() {
        return "TIMES";
    }
}

class MinusOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 - (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String toString() {
        return "MINUS";
    }
}

class DivideOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);
        if (v1 instanceof Integer && v2 instanceof Integer) {
            if ((Integer) v2 == 0) {
                System.out.println("Error: division by zero");
                System.exit(1);
            }
            return (Integer) v1 / (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String toString() {
        return "DIVIDE";
    }
}

class ModeOperator implements Operator {

    public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            if ((Integer) v2 == 0) {
                System.out.println("Error: division by zero");
                System.exit(1);
            }
            return (Integer) v1 % (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String toString() {
        return "MODE";
    }
}

class PreIncrementExpression implements Expr {
    Expr e;

    public PreIncrementExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof Integer) {
            return ((Integer) v) + 1;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generamos un nuevo temporal para la expresión
        String temp = codeGen.newTemp();

        // Obtenemos el código intermedio de la expresión e
        String exprCode = e.generateCode(codeGen);

        // Cargamos el valor de la expresión en el temporal
        codeGen.addInstruction("LOAD", exprCode, "", temp);  // Cargamos el valor en un temporal

        // Incrementamos el valor del temporal
        String incrementedTemp = codeGen.newTemp();
        codeGen.addInstruction("ADD", temp, "1", incrementedTemp);  // Incremento de 1

        // Almacenamos el valor incrementado de vuelta en la variable o temporal
        codeGen.addInstruction("STORE", incrementedTemp, "", exprCode);  // Guardamos el nuevo valor

        return exprCode;  // Retornamos el nombre de la variable o temporal actualizado
    }


}


class PreDecrementExpression implements Expr {
    Expr e;

    public PreDecrementExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof Integer) {
            return ((Integer) v) - 1;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generamos un nuevo temporal para la expresión
        String temp = codeGen.newTemp();

        // Obtenemos el código intermedio de la expresión e
        String exprCode = e.generateCode(codeGen);

        // Cargamos el valor de la expresión en el temporal
        codeGen.addInstruction("LOAD", exprCode, "", temp);  // Cargamos el valor en un temporal

        // Decrementamos el valor del temporal
        String decrementedTemp = codeGen.newTemp();
        codeGen.addInstruction("SUB", temp, "1", decrementedTemp);  // Decremento de 1

        // Almacenamos el valor decrementado de vuelta en la variable o temporal
        codeGen.addInstruction("STORE", decrementedTemp, "", exprCode);  // Guardamos el nuevo valor

        return exprCode;  // Retornamos el nombre de la variable o temporal actualizado
    }

}

class OperatorExpression implements Expr {

    Expr e, e2;
    Operator o;

    public OperatorExpression(Expr e, Operator o, Expr e2) {
        this.e = e;
        this.e2 = e2;
        this.o = o;
    }

    public Object run(HashMap<String, Object> hm) {
        return o.count(e, e2, hm);
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar código para la primera expresión
        String temp1 = e.generateCode(codeGen);  // Código para la primera expresión
        // Generar código para la segunda expresión
        String temp2 = e2.generateCode(codeGen);  // Código para la segunda expresión

        // Crear un nuevo temporal para almacenar el resultado de la operación
        String resultTemp = codeGen.newTemp();

        // Dependiendo del operador, generar el código intermedio
        // Aquí, usamos el operador que tiene el objeto 'o'. Puede ser suma, resta, multiplicación, etc.
        String operatorCode = o.toString();

        // Añadir la instrucción correspondiente para realizar la operación
        codeGen.addInstruction(operatorCode, temp1, temp2, resultTemp);

        // Retornar el temporal que contiene el resultado
        return resultTemp;
    }

}

/**
 * INT OPERATIONS
 */
class IntExpression implements Expr {
    int value;

    public IntExpression(int e) {
        value = e;
    }

    public int getValue() {
        return value;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Crear un nuevo temporal para almacenar el valor de la expresión
        String resultTemp = codeGen.newTemp();

        // Generar la instrucción de asignación
        // Asignar el valor entero directamente al temporal
        codeGen.addInstruction("ASSIGN", Integer.toString(value), "", resultTemp);

        // Retornar el temporal que contiene el valor del entero
        return resultTemp;
    }

}

class IntEnterExpression implements Expr {
    public Object run(HashMap<String, Object> hm) {
        java.util.Scanner in = new java.util.Scanner(System.in);
        return in.nextInt();
    }

    public String generateCode(c3Direcciones codeGen) {
        // Crear un nuevo temporal para almacenar el valor leído
        String resultTemp = codeGen.newTemp();

        // Generar la instrucción que simula leer un entero desde la entrada
        // Supongamos que la operación de entrada se representa como "READ" en el código intermedio
        codeGen.addInstruction("READ", "", "", resultTemp);

        // Retornar el temporal que contiene el valor leído
        return resultTemp;
    }

}

class PIntExpression implements Expr {
    Expr expr;

    public PIntExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return expr.run(hm);
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Llamamos al generateCode de la expresión interna (expr) para generar el código intermedio
        return expr.generateCode(codeGen);
    }
}

class UMinusExpression implements Expr {
    Expr e;

    public UMinusExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof Integer) {
            return -((Integer) v);
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Primero generamos el código para la expresión interna (e)
        String temp = e.generateCode(codeGen); // Aquí obtenemos el código intermedio de e

        // Crear un nuevo nombre para el temporal donde se almacenará el resultado de la operación unaria
        String tempResult = codeGen.newTemp();

        // Agregar la instrucción de código intermedio para la operación de negación
        codeGen.addInstruction("-", temp, null, tempResult);

        return tempResult;  // Retornamos el nombre del temporal donde se almacena el resultado
    }

}

class STRLengthExpression implements Expr {
    Expr e;

    public STRLengthExpression(Expr e) {
        this.e = e;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v = e.run(hm);
        if (v instanceof String) {
            return ((String) v).length();
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }

    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generamos el código para la expresión interna
        String temp = e.generateCode(codeGen);

        // Creamos un nuevo temporal para almacenar el resultado
        String tempResult = codeGen.newTemp();

        // Agregamos la instrucción para obtener la longitud de la cadena
        codeGen.addInstruction("strlen", temp, null, tempResult);

        // Retornamos el temporal con el resultado
        return tempResult;
    }

}

class STRPositionExpression implements Expr {
    Expr e, e2;

    public STRPositionExpression(Expr e, Expr e2) {
        this.e = e;
        this.e2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v1 = e.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            String s = (String) v1;
            String s2 = (String) v2;

            int pos = s.indexOf(s2);
            return (pos != -1) ? pos + 1 : 0;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return 0;
        }
    }


    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generamos el código para las expresiones internas
        String temp1 = e.generateCode(codeGen);  // Temporal para la cadena principal
        String temp2 = e2.generateCode(codeGen); // Temporal para la subcadena

        // Creamos un temporal para almacenar el resultado
        String tempResult = codeGen.newTemp();

        // Agregamos la instrucción para buscar la posición de la subcadena
        codeGen.addInstruction("strpos", temp1, temp2, tempResult);

        // Retornamos el temporal con el resultado
        return tempResult;
    }

}

/**
 * CONDITIONS
 */
class EqCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        // Comparar enteros
        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 == (Integer) v2;
        }
        // Comparar booleanos
        else if (v1 instanceof Boolean && v2 instanceof Boolean) {
            return (Boolean) v1 == (Boolean) v2;
        }
        // Manejo de error para tipos incorrectos
        else {
            System.out.println("Error: wrong objects type for equality comparison");
            System.exit(1);
            return false;
        }

    }
}

class LtCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 < (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return false;
        }
    }
}

class LeCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 <= (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return false;
        }
    }
}

class GtCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 > (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return false;
        }
    }
}

class GeCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 >= (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return false;
        }
    }
}

class NeCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof Integer && v2 instanceof Integer) {
            return (Integer) v1 != (Integer) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return false;
        }
    }
}

class StrEqCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return ((String) v1).equals((String) v2);
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return false;
        }
    }
}

class StrNotEqCond implements Condition {
    public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

        Object v1 = e1.run(hm);
        Object v2 = e2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return !((String) v1).equals((String) v2);
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return false;
        }
    }
}


class BoolExpression2 implements Expr {
    Boolean value;

    public BoolExpression2(Boolean e) {
        value = e;
    }

    public boolean getValue() {
        return value;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Creamos un temporal para almacenar el valor booleano
        String tempResult = codeGen.newTemp();

        // Agregamos la instrucción para asignar el valor booleano al temporal
        codeGen.addInstruction("assign", value.toString(), null, tempResult);

        // Retornamos el temporal que contiene el valor booleano
        return tempResult;
    }
}


/**
 * BOOLEAN OPERATIONS
 */
class BooleanExpression implements Expr {
    Boolean value;
    Boolean isConst = false;

    // Constructor with both parameters
    public BooleanExpression(Boolean e, Boolean isConst) {

        if (isConst && e == null) {
            System.out.println("Error: null value for constant");
            System.exit(1);
        }
        this.value = e;
        this.isConst = isConst;
    }

    // Constructor with only the value parameter, defaults isConst to false
    public BooleanExpression(Boolean e) {
        if (isConst && e == null) {
            System.out.println("Error: null value for constant");
            System.exit(1);
        }
        this.value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Crear un temporal para almacenar el valor booleano
        String tempResult = codeGen.newTemp();

        // Determinar si el valor es una constante
        String boolValue = value != null ? value.toString() : "null";

        if (isConst) {
            // Agregar instrucción de asignación como constante
            codeGen.addInstruction("assign_const", boolValue, null, tempResult);
        } else {
            // Agregar instrucción de asignación regular
            codeGen.addInstruction("assign", boolValue, null, tempResult);
        }

        // Retornar el temporal que contiene el valor booleano
        return tempResult;
    }

}

class ConditionBooleanExpression implements Expr {

    Expr e, e2;
    Condition c;

    public ConditionBooleanExpression(Expr e, Condition c, Expr e2) {
        this.e = e;
        this.c = c;
        this.e2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return c.test(e, e2, hm);
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar código para las expresiones e y e2
        String temp1 = e.generateCode(codeGen);
        String temp2 = e2.generateCode(codeGen);

        // Crear un temporal para almacenar el resultado de la condición
        String tempResult = codeGen.newTemp();

        // Obtener el operador de la condición
        String operator = c.toString(); // Se asume que existe un método getOperator en Condition

        // Agregar la instrucción para evaluar la condición
        codeGen.addInstruction(operator, temp1, temp2, tempResult);

        // Retornar el temporal que contiene el resultado de la condición
        return tempResult;
    }

}

class PBooleanExpression implements Expr {
    Expr expr;

    public PBooleanExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return expr.run(hm);
    }

    public String generateCode(c3Direcciones codeGen) {
        // Delegar la generación de código a la expresión envuelta
        return expr.generateCode(codeGen);
    }
}

class NegationBooleanExpression implements Expr {
    Expr expr;

    public NegationBooleanExpression(Expr e) {
        expr = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return !((Boolean) expr.run(hm));
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar el código para la expresión contenida
        String tempExpr = expr.generateCode(codeGen);

        // Crear un nuevo temporal para almacenar el resultado de la negación
        String tempResult = codeGen.newTemp();

        // Añadir la instrucción de negación lógica
        codeGen.addInstruction("NOT", tempExpr, null, tempResult);

        // Retornar el temporal que contiene el resultado
        return tempResult;
    }
}

class AndBooleanExpression implements Expr {
    Expr expr, expr2;

    public AndBooleanExpression(Expr e, Expr e2) {
        expr = e;
        expr2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return (Boolean) expr.run(hm) && (Boolean) expr2.run(hm);
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar código para las expresiones individuales
        String tempExpr1 = expr.generateCode(codeGen);
        String tempExpr2 = expr2.generateCode(codeGen);

        // Crear un nuevo temporal para almacenar el resultado del AND
        String tempResult = codeGen.newTemp();

        // Añadir la instrucción AND al generador de código
        codeGen.addInstruction("AND", tempExpr1, tempExpr2, tempResult);

        // Retornar el temporal que contiene el resultado
        return tempResult;
    }
}

class OrBooleanExpression implements Expr {
    Expr expr, expr2;

    public OrBooleanExpression(Expr e, Expr e2) {
        expr = e;
        expr2 = e2;
    }

    public Object run(HashMap<String, Object> hm) {
        return (Boolean) expr.run(hm) || (Boolean) expr2.run(hm);
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar código para las expresiones individuales
        String tempExpr1 = expr.generateCode(codeGen);
        String tempExpr2 = expr2.generateCode(codeGen);

        // Crear un nuevo temporal para almacenar el resultado del OR
        String tempResult = codeGen.newTemp();

        // Añadir la instrucción OR al generador de código
        codeGen.addInstruction("OR", tempExpr1, tempExpr2, tempResult);

        // Retornar el temporal que contiene el resultado
        return tempResult;
    }
}

/**
 * STRING OPERATIONS
 */

class StringExpression implements Expr {
    String value;

    public StringExpression(String e) {
        value = e;
    }

    public Object run(HashMap<String, Object> hm) {
        return value;
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Crear un nuevo temporal para almacenar el valor de la cadena
        String tempResult = codeGen.newTemp();

        // Instrucción para asignar la cadena al temporal
        // Dado que addInstruction solo acepta 4 parámetros, se usa el temporal para almacenar la cadena directamente
        codeGen.addInstruction("ASSIGN", "\"" + value + "\"", "", tempResult);

        // Retornar el temporal que contiene el valor de la cadena
        return tempResult;
    }
}

class StrEnterExpression implements Expr {
    public Object run(HashMap<String, Object> hm) {
        java.util.Scanner in = new java.util.Scanner(System.in);
        String s = in.nextLine();
        return s;
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar un nuevo temporal para almacenar la entrada del usuario
        String tempResult = codeGen.newTemp();

        // Generar el código para la instrucción que asigna la entrada de texto al temporal
        // En este caso, el valor de la entrada no puede ser directamente pasado, así que se coloca un marcador.
        codeGen.addInstruction("READ", "STRING", "", tempResult);

        // Retornar el temporal donde se almacenará el valor de la cadena ingresada
        return tempResult;
    }
}

class ConcatStringExpression implements Expr {
    Expr s, s2;

    public ConcatStringExpression(Expr s, Expr s2) {
        this.s = s;
        this.s2 = s2;
    }

    public Object run(HashMap<String, Object> hm) {
        Object v1 = s.run(hm);
        Object v2 = s2.run(hm);

        if (v1 instanceof String && v2 instanceof String) {
            return (String) v1 + (String) v2;
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return null;
        }
    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar los códigos para las subexpresiones s y s2
        String temp1 = s.generateCode(codeGen);
        String temp2 = s2.generateCode(codeGen);

        // Generar un nuevo temporal para almacenar el resultado de la concatenación
        String tempResult = codeGen.newTemp();

        // Instrucción para concatenar las cadenas
        codeGen.addInstruction("CONCAT", temp1, temp2, tempResult);

        // Retornar el temporal donde se almacena el resultado de la concatenación
        return tempResult;
    }
}

class SubStringExpression implements Expr {
    Expr sExpr, posExpr, lengthExpr;

    public SubStringExpression(Expr s, Expr pos, Expr length) {
        sExpr = s;
        posExpr = pos;
        lengthExpr = length;
    }

    public Object run(HashMap<String, Object> hm) {

        Object v1 = sExpr.run(hm);
        Object v2 = posExpr.run(hm);
        Object v3 = lengthExpr.run(hm);

        if (v1 instanceof String && v2 instanceof Integer && v3 instanceof Integer) {
            String s = (String) v1;
            int pos = (Integer) v2;
            int length = (Integer) v3;

            if (pos + length - 1 > s.length()) {
                length = s.length() - pos + 1;
            }
            if (pos < 1 || pos > s.length() || length < 1) {
                return "";
            } else {
                return new String(s.substring(pos - 1, pos + length - 1));
            }
        } else {
            System.out.println("Error: wrong objects type");
            System.exit(1);
            return null;
        }

    }

    @Override
    public String generateCode(c3Direcciones codeGen) {
        // Generar código para las subexpresiones
        String temp1 = sExpr.generateCode(codeGen);  // Obtiene el temporal para la cadena
        String temp2 = posExpr.generateCode(codeGen); // Obtiene el temporal para la posición
        String temp3 = lengthExpr.generateCode(codeGen); // Obtiene el temporal para la longitud

        // Generar un nuevo temporal para el resultado
        String tempResult = codeGen.newTemp();

        // Generar código intermedio para obtener la subcadena
        // Este es un ejemplo de cómo puede ser la instrucción
        codeGen.addInstruction("SUBSTRING", temp1, temp2 + ", " + temp3, tempResult);

        // Retornar el nombre del temporal que contiene el resultado de la subcadena
        return tempResult;
    }
}


class OutputInstruction implements SimpleInstruction {
    Expr expr;

    public OutputInstruction(Expr e) {
        expr = e;
    }

    public void run(HashMap<String, Object> hm) {
        System.out.println(expr.run(hm));
    }
}


/**
 * FLOW OPERATIONS
 */
class InstructionList {
    private List<SimpleInstruction> simpleInstructions;

    public InstructionList(SimpleInstruction s) {
        simpleInstructions = new ArrayList<SimpleInstruction>();
        simpleInstructions.add(s);
    }

    public void add(SimpleInstruction s) {
        simpleInstructions.add(s);
    }

    public void run(HashMap<String, Object> hm) {
        for (SimpleInstruction si : simpleInstructions) {
            si.run(hm);
        }
    }
}

class MainInstructionList {
    private List<InstructionList> instructionLists;

    public MainInstructionList(InstructionList il) {
        instructionLists = new ArrayList<InstructionList>();
        instructionLists.add(il);
    }

    public void add(InstructionList il) {
        instructionLists.add(il);
    }

    public void run(HashMap<String, Object> hm) {
        for (InstructionList il : instructionLists) {
            il.run(hm);
        }
    }

}

class ConstInstruction implements SimpleInstruction {
    private Expr expr;

    public ConstInstruction(Expr e) {
        expr = e;
    }

    public void run(HashMap<String, Object> hm) {
        expr.run(hm);
    }
}

class WhileInstruction implements WhileInstructionI {
    Expr cond;
    InstructionList il;

    public WhileInstruction(Expr c, InstructionList il) {
        cond = c;
        this.il = il;
    }

    public void run(HashMap<String, Object> hm) {
        while ((Boolean) cond.run(hm)) {
            il.run(hm);
        }
    }
}

class DoWhileInstruction implements WhileInstructionI {
    Expr cond;
    InstructionList il;

    public DoWhileInstruction(Expr c, InstructionList s) {
        cond = c;
        il = s;
    }

    public void run(HashMap<String, Object> hm) {
        do
            il.run(hm);
        while ((Boolean) cond.run(hm));
    }
}

class IfInstruction implements IfInstructionI {

    Expr condition;
    InstructionList il;

    public IfInstruction(Expr condition, InstructionList il) {
        this.condition = condition;
        this.il = il;
    }

    public void run(HashMap<String, Object> hm) {
        if ((Boolean) condition.run(hm)) {
            il.run(hm);
        }
    }
}

class IfElseInstruction implements IfInstructionI {

    Expr condition;
    InstructionList instructionList;
    InstructionList instructionList2;

    public IfElseInstruction(Expr condition, InstructionList simpleInstruction, InstructionList simpleInstruction2) {
        this.condition = condition;
        this.instructionList = simpleInstruction;
        this.instructionList2 = simpleInstruction2;
    }

    public void run(HashMap<String, Object> hm) {
        if ((Boolean) condition.run(hm)) {
            instructionList.run(hm);
        } else {
            instructionList2.run(hm);
        }
    }
}

class BeginEndInstruction implements SimpleInstruction {
    private InstructionList instructions;

    public BeginEndInstruction(InstructionList instructions) {
        this.instructions = instructions;
    }

    public void run(HashMap<String, Object> hm) {
        instructions.run(hm);
    }
}

class BeginEndInstructionId implements SimpleInstruction {
    private InstructionList instructions;
    private String id;

    public BeginEndInstructionId(InstructionList instructions, String id) {
        this.instructions = instructions;
        this.id = id;
    }

    public void run(HashMap<String, Object> hm) {
        instructions.run(hm);
    }
}