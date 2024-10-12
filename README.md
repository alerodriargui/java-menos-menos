# Compilador (entorno inicial)

* Para inicializar el programa, abrir una terminal de Ubuntu en el directorio del proyecto.
* Tener instalado Java
``sudo apt update``
``sudo apt install default-jdk``
  
* Seguidamente, debemos escribir ``./go.sh``
  
  El archivo "test.txt" es el programa de entrada que el compilador se debe comer.
  El lenguaje usado para las pruebas es muy parecido al de ADA.
  En el Main.java se escriben todas las clases y funciones necesarias que se utilizarán en el .cup
    
* Para limpiar y quitar todos los programas generados por el compilador, ejecutar ``./clean.sh``

* En caso de que recibamos le error de `bash: ./go.sh: Permission denied`, ejecutar `chmod 777 go.sh` para darle permisos
    
# Práctica de Compiladores - Curso Académico 2024-2025

## Objetivo
- Desarrollar un compilador para un lenguaje imperativo.

## Aspectos Generales
- La práctica podrá ser realizada en grupos de como máximo cuatro personas. La conformación de cada grupo se deberá hacer a través del apartado correspondiente en la plataforma online del curso (Aula Digital).
- La práctica consiste en el diseño e implementación de un compilador para un lenguaje de programación. Las tareas que deberá realizar el procesador incluyen:
    - **Tareas de front-end:**
        - Análisis léxico
        - Análisis sintáctico
        - Análisis semántico
    - **Tareas de back-end:**
        - Generación de código intermedio
        - Optimización
        - Generación de código ensamblador
    - La implementación de una tabla de símbolos y un gestor de errores es obligatoria.

## Funcionalidad del Compilador
El compilador que se debe desarrollar debe tener las siguientes funcionalidades:

- Debe procesar el código fuente proporcionado en un archivo de texto suministrado por el programador.
- Debe generar una serie de archivos como resultado de su ejecución:
    - **Front-end:**
        - Archivo de tokens: todos los tokens generados según la secuencia de entrada.
        - Tabla de símbolos: toda la información de los datos introducidos en la tabla de símbolos una vez que se haya procesado completamente el código fuente.
    - **Back-end:**
        - Tablas de variables y procedimientos para comprobar la corrección del código de tres direcciones.
        - Archivo de código intermedio: el código intermedio correspondiente al programa en código fuente.
        - Archivo de código ensamblador, sin optimizar: Para cada instrucción de tres direcciones, un comentario mostrará la instrucción seguida de la traducción correspondiente.
        - Archivo de código ensamblador, optimizado: La idea es que el ejecutable obtenido con el código optimizado y el código sin optimizar realicen lo mismo pero se demuestre la diferencia en rendimiento.
    - **Errores:**
        - Si se detectan errores, se generará un documento con los errores detectados, indicando para cada error la línea donde se detectó, el tipo de error (léxico, sintáctico, semántico) y un mensaje explicativo.

## Características del Lenguaje
A continuación, se presenta una lista de todas las características que debe tener el lenguaje. Algunas están marcadas con un asterisco (*) como opcionales. Otras están marcadas con un número (por ejemplo, 2, 4), indicando el número mínimo de características que deben implementarse.

- Un cuerpo general de programa donde haya subprogramas, declaraciones e instrucciones (similar al `main` de Java/C++ o a una sección de instrucciones como en Python).
- Definición de subprogramas: funciones o procedimientos, con argumentos.
- Tipos:
    - Entero
    - Cadena* 
    - Lógico
    - Otros*
- Tipos definidos por el usuario (debe implementar al menos uno):
    - Tuplas
    - Arreglos multidimensionales
- Valores de cualquiera de los tipos implementados:
    - Declaración y uso de variables
    - Constantes
- Operaciones:
    - Asignación
    - Condicional
    - **Selección múltiple*** (como switch)
    - Bucles (debe implementar al menos dos):
        - Mientras
        - Repetir hasta
        - Para
        - Otros
    - Llamadas a procedimientos y funciones con parámetros
    - Retorno de funciones si se implementan
- Expresiones aritméticas y lógicas:
    - Usando literales del tipo apropiado
    - Usando constantes y variables
- Operaciones de entrada/salida:
    - Entrada por teclado
    - Salida por pantalla
    - Entrada/salida desde archivo*
- Operadores (debe implementar al menos dos de cada tipo):
    - **Aritméticos**: suma, resta, multiplicación, división, módulo.
    - **Relacionales**: igual, diferente, mayor que, menor que, mayor o igual, menor o igual.
    - **Lógicos**: y, o, no, o-excluyente.
    - **Especiales**:
        - Pre incremento/decremento*
        - Post incremento/decremento*
        - Asignación combinada con operación (+=, etc.)*
        - Operación condicional (? :)*
        - Otros*

## Presentación de la Práctica
- **Documentación:** Claramente escrita, describiendo técnicas, diseño y cualquier aspecto que se desee destacar. La documentación debe contener:
    - Para el analizador léxico: tokens, patrones y rutinas (esto puede ser el mismo archivo que se utilizará para generar el escáner).
    - Para el analizador sintáctico: gramática y métodos de análisis. Justificación del método de análisis elegido.
    - Para el analizador semántico: traducción dirigida por la sintaxis, con rutinas semánticas.
    - Para la tabla de símbolos: descripción de la estructura y organización.
    - Para optimizaciones, si es aplicable, describir las optimizaciones implementadas.
    - Para la generación de código ensamblador: describir la arquitectura elegida y cualquier biblioteca externa utilizada.
- **Código fuente completo** con instrucciones para la correcta ejecución. El código debe compilar sin errores ni advertencias, y la ejecución debe estar libre de excepciones no controladas o restricciones.
- **Casos de prueba**: tanto correctos como erróneos. Para cada caso de prueba, proporcionar una descripción de su funcionalidad e indicar los tipos de errores (si los hay).
- **Presentación de la práctica:** Cada miembro del grupo debe presentar la práctica, describiendo todos los aspectos relevantes. La evaluación puede variar entre los miembros según sus contribuciones a la presentación.


## Características del Lenguaje - Checklist

- [x] Cuerpo general del programa definido.
- [ ] Subprogramas definidos (funciones o procedimientos).
- [x] Tipos implementados:
    - [x] Entero
    - [x] Cadena*
    - [x] Lógico
    - [ ] Otros*
- [x] Tipos definidos por el usuario implementados:
    - [x] Tuplas
    - [ ] Arreglos multidimensionales
- [x] Declaración y uso de variables.
- [x] Constantes.
- [x] Operaciones implementadas:
    - [x] Asignación
    - [x] Condicional
    - [ ] Selección múltiple*
    - [x] Bucles (solo necesario 2):
        - [x] Mientras
        - [x] Repetir hasta
        - [ ] Para
        - [ ] Otros
    - [ ] Llamadas a procedimientos y funciones con parámetros.
    - [ ] Retorno de funciones (si se implementan).
- [x] Expresiones aritméticas y lógicas:
    - [x] Usando literales.
    - [x] Usando constantes y variables.
- [x] Operaciones de entrada/salida:
    - [x] Entrada por teclado
    - [x] Salida por pantalla
    - [x] Entrada/salida desde archivo*
- [x] Operadores implementados:
    - [x] Aritméticos:
        - [x] Suma
        - [x] Resta
        - [x] Multiplicación
        - [x] División
        - [x] Módulo
    - [x] Relacionales:
        - [x] Igual
        - [x] Diferente
        - [x] Mayor que
        - [x] Menor que
        - [ ] Mayor o igual
        - [ ] Menor o igual
    - [x] Lógicos:
        - [x] Y
        - [x] O
        - [x] No
        - [ ] O-excluyente
    - [x] Especiales:
        - [x] Pre incremento/decremento*
        - [x] Post incremento/decremento*
        - [ ] Asignación combinada con operación (+=, etc.)*
        - [ ] Operación condicional (? :)*
        - [ ] Otros*

