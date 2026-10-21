@echo off
rem ConsoleUI test script
rem Compiles ConsoleUI + the test files into "bin", then runs TestFile.
cd /d "%~dp0"

where javac >nul 2>&1
if errorlevel 1 (
    echo [ERROR] javac was not found. Install a JDK and make sure it is on the PATH.
    pause
    exit /b 1
)

if not exist "bin" mkdir "bin"

rem Compile: the class under test (ConsoleUI), its two helper classes
rem (test-double MenuItem and Order), and the test itself (TestFile).
javac -encoding UTF-8 -d "bin" ConsoleUI.java MenuItem.java Order.java TestFile.java
if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed. Fix the errors above, then run test.bat again.
    pause
    exit /b 1
)

echo.
java -cp "bin" TestFile

echo.
pause
