@echo off

:: Elimina parser.java si existe
if exist parser.java del /f parser.java

:: Elimina sym.java si existe
if exist sym.java del /f sym.java

:: Elimina Lexer.java si existe
if exist Lexer.java del /f Lexer.java

:: Elimina todos los .class en la carpeta bin si existen
if exist bin\*.class del /f bin\*.class

:: Elimina la carpeta bin completamente si existe
if exist bin rmdir /s /q bin

echo Limpieza completada.