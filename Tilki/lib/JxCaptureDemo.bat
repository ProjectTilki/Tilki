@echo off

REM Copyright (c) 2002-2016 TeamDev Ltd. All rights reserved.

set CUSTOM_CLASSES=jxcapturedemo.jar;
set CORE_CLASSES=jniwrap-3.11.jar;jxcapture-3.4.jar;slf4j-api-1.5.8.jar;slf4j-simple-1.5.8.jar;license.jar

set SAMPLE_CLASSPATH=%CORE_CLASSES%;%CUSTOM_CLASSES%
set SAMPLE_MAINCLASS=com.teamdev.jxcapture.demo.JxCaptureApplication

echo %SAMPLE_CLASSPATH%
echo %SAMPLE_MAINCLASS%

start "" javaw.exe -Dsun.java2d.noddraw=true -Xmx256M -classpath %SAMPLE_CLASSPATH% %SAMPLE_MAINCLASS%
