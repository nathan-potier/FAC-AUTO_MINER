@echo off

rem Compile the Java source code
javac -d bin -cp src src/Modele/*.java src/Vue/*.java src/Main.java

if ERRORLEVEL 1 (
    echo Compilation failed! Exiting...
    exit /b 1
)

rem Execute the compiled program
java -cp "bin;.;assets" Main

pause