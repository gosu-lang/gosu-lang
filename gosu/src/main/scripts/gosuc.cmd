@echo off
setLocal EnableDelayedExpansion

set _G_ROOT_DIR=%~dp0

set _G_CLASSPATH=%_G_ROOT_DIR%..\src;%_G_ROOT_DIR%..\lib\*;%JAVA_HOME%\lib\tools.jar
set _CMD_LINE_ARGS=

set _DEBUG=
if ""%1""==""debug"" set _DEBUG=-Xdebug -Xrunjdwp:transport=dt_shmem,address=gosuc,server=y,suspend=y
if ""%1""==""debug"" shift

REM Slurp the command line arguments. This loop allows for an unlimited number
REM of arguments (up to the command line limit, anyway).

if ""%1""=="""" goto doneStart

:setupArgs
  if ""%1""=="""" goto doneStart
  set _CMD_LINE_ARGS=%_CMD_LINE_ARGS% %1
  shift
  goto setupArgs

REM This label provides a place for the argument list loop to break out
REM and for NT handling to skip to.
:doneStart

:checkJava
set _JAVACMD=%JAVACMD%

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe

:noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=java.exe

:runGosuc
"%_JAVACMD%" %_DEBUG% %GOSU_OPTS% -classpath "%_G_CLASSPATH%" gw.lang.gosuc.cli.CommandLineCompiler %_CMD_LINE_ARGS%
goto end

:end

rem Check the error code of the execution
set GOSU_ERROR=%ERRORLEVEL%

set _JAVACMD=
set _CMD_LINE_ARGS=
set _DEBUG=
set _G_CLASSPATH=
set _G_ROOT_DIR=
