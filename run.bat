@echo off
echo ======================================================================
echo   YATRA ATLAS - SMART TRAVEL GUIDE (DSA-3 PROJECT)
echo ======================================================================
echo Compiling pure Java backend (no external dependencies required)...
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin src/model/*.java src/algorithms/string/*.java src/algorithms/dp/*.java src/algorithms/graph/*.java src/algorithms/flow/*.java src/algorithms/approximation/*.java src/algorithms/randomized/*.java src/algorithms/analytics/*.java src/utils/*.java src/data/*.java src/services/*.java src/app/*.java src/tests/*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Running automated DSA-3 verification suite...
java -cp bin tests.DSATestSuite
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Test suite failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Starting Yatra Atlas Server on http://localhost:8080 ...
echo Press Ctrl+C in this terminal to stop the server.
echo.
start http://localhost:8080/index.html
java -cp bin app.TravelGuideApp
