@REM Atalho para executar Maven (ja instalado em %USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6)
@echo off
setlocal
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.9.10-hotspot
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
"%MAVEN_HOME%\bin\mvn.cmd" %*
endlocal
