@echo off
rem UBITE start script - see CONTRACT.md Section 5
rem 1. Go to this script's own folder so "data\menu.csv" works from anywhere
cd /d "%~dp0"

where javac >nul 2>&1
if errorlevel 1 (
    echo [ERROR] javac was not found. Install a JDK and make sure it is on the PATH.
    pause
    exit /b 1
)

rem 2. Create bin if it does not exist
if not exist "bin" mkdir "bin"

rem 3. Compile every .java file in src into bin
javac -encoding UTF-8 -d "bin" src\*.java

rem 4. Stop with a readable message if compilation failed
if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed. Fix the errors above, then run start.bat again.
    pause
    exit /b 1
)

rem 5. Run Main from bin
java -cp "bin" Main

rem 6. Pause so errors can be read
echo.
pause
