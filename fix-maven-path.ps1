$mavenHome = "C:\Tools\apache-maven-3.9.16"
$mavenBin  = "$mavenHome\bin"
$javaHome  = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot"
$javaBin   = "$javaHome\bin"

if (-not (Test-Path "$mavenBin\mvn.cmd")) {
    Write-Host "ERROR: Maven not found at $mavenBin" -ForegroundColor Red
    exit 1
}

[Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "User")
[Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "User")

$userPath = [Environment]::GetEnvironmentVariable("Path", "User")
$parts = $userPath -split ';' | Where-Object {
    $_ -and
    $_ -notlike '*%MAVEN_HOME%*' -and
    $_ -notlike '*%JAVA_HOME%*' -and
    $_ -notlike '*apache-maven*' -and
    $_ -ne $javaBin
}
$parts += $mavenBin
$parts += $javaBin
[Environment]::SetEnvironmentVariable("Path", ($parts -join ';'), "User")

$env:JAVA_HOME = $javaHome
$env:MAVEN_HOME = $mavenHome
$env:Path = [Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [Environment]::GetEnvironmentVariable("Path", "User")

Write-Host ""
Write-Host "Testing now:" -ForegroundColor Cyan
& "$mavenBin\mvn.cmd" -v
Write-Host ""
Write-Host "If you see Maven version above, it works." -ForegroundColor Green
Write-Host "Close ALL terminals and Cursor, reopen, then run: mvn -v" -ForegroundColor Yellow
