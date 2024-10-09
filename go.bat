@echo off
setlocal

REM Crear el directorio bin si no existe
if not exist bin mkdir bin

REM Compilar con JFlex
java -jar lib\jflex-full-1.8.2.jar lcalc.flex
if %errorlevel% neq 0 (
    echo Error al ejecutar JFlex
    exit /b %errorlevel%
)

REM Ejecutar el parser con CUP
java -cp .;lib\java-cup-11b.jar java_cup.Main < ycalc.cup
if %errorlevel% neq 0 (
    echo Error al ejecutar CUP
    exit /b %errorlevel%
)

REM Compilar el código Java
javac -d bin -cp .;lib\java-cup-11b.jar Main.java
if %errorlevel% neq 0 (
    echo Error al compilar el código Java
    exit /b %errorlevel%
)

REM Ejecutar el programa con el archivo de prueba
java -cp bin;lib\java-cup-11b-runtime.jar Main tests\test.txt
if %errorlevel% neq 0 (
    echo Error al ejecutar el programa
    exit /b %errorlevel%
)

echo Programa ejecutado con éxito.
pause