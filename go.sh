mkdir -p bin

#compile
java -jar lib/jflex-full-1.8.2.jar lcalc.flex  
java -cp .:lib/java-cup-11b.jar java_cup.Main < ycalc.cup
javac -d bin -cp .:lib/java-cup-11b.jar Main.java

#run
java -cp bin:lib/java-cup-11b-runtime.jar Main tests/pruebaAlex.txt
