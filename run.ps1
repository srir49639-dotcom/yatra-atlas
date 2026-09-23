# ======================================================================
#   YATRA ATLAS - SMART TRAVEL GUIDE (DSA-3 PROJECT)
# ======================================================================

Write-Host "Compiling pure Java backend (no external dependencies required)..." -ForegroundColor Cyan
if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }

$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d bin $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "`nRunning automated DSA-3 verification suite..." -ForegroundColor Cyan
java -cp bin tests.DSATestSuite

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Test suite failed!" -ForegroundColor Red
    exit 1
}

Write-Host "`nStarting Yatra Atlas Server on http://localhost:8080 ..." -ForegroundColor Green
Write-Host "Press Ctrl+C in this terminal to stop the server.`n" -ForegroundColor Yellow

Start-Process "http://localhost:8080/index.html"
java -cp bin app.TravelGuideApp
