@echo off
chcp 65001 >nul
setlocal
title FramePilot - сборка мода
echo ==========================================
echo   FramePilot - сборка мода Minecraft
echo ==========================================
echo.

where java >nul 2>nul
if errorlevel 1 (
  echo Java не найдена в PATH.
  echo Для сборки нужна JDK 21, а не только Java внутри Minecraft Launcher.
  echo Открой https://adoptium.net/temurin/releases/?version=21
  echo Выбери Windows x64 ^> JDK ^> .msi и установи её.
  echo Затем закрой это окно и снова запусти BUILD.bat.
  pause
  exit /b 1
)

java -version 2>&1 | findstr /C:"21." >nul
if errorlevel 1 (
  echo Предупреждение: команда java не показывает Java 21.
  echo Возможно, в системе установлена другая версия Java.
  echo.

  where javac >nul 2>nul
  if errorlevel 1 (
    echo Компилятор javac не найден. Нужна именно JDK 21.
    echo Открой https://adoptium.net/temurin/releases/?version=21
    pause
    exit /b 1
  )
)

where gradle >nul 2>nul
if errorlevel 1 (
  echo Gradle не найден.
  echo Этот архив пока не содержит Gradle Wrapper, поэтому Gradle нужно установить отдельно.
  echo Инструкция: https://gradle.org/install/
  echo После установки закрой и заново открой это окно, затем запусти BUILD.bat.
  pause
  exit /b 1
)

echo Запускаю сборку. При первом запуске Gradle скачает необходимые зависимости.
echo Это может занять несколько минут.
echo.
call gradle build
if errorlevel 1 (
  echo.
  echo Сборка завершилась с ошибкой.
  echo Скопируй полный текст ошибки и отправь его в ChatGPT для исправления.
  pause
  exit /b 1
)

echo.
echo УСПЕХ! Ищи готовый JAR в папке build\libs.
explorer "%CD%\build\libs"
pause
