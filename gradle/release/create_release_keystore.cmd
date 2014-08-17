@echo off
:: ArtfulBits Inc. (c) 2005-2009
:: 
:: Signing of the APK by certificate 

SET JDK=%JAVA_HOME%\bin
SET COMPANY=ongame
SET KEYSTORE=%COMPANY%.keystore

:: validate script dependences
if not exist "%JDK%\keytool.exe" (
  goto :error keytool.exe
  goto :EOF
)

SET KEY=ongame
SET PASS=ongame
SET ALIAS=ongame

:: generate key
if not exist "%DEST%\%KEYSTORE%" (
  "%JDK%\keytool.exe" -genkey -alias %ALIAS% -keyalg RSA -validity 20000 -storepass %PASS% -keypass %KEY% -keystore %KEYSTORE% -dname "CN=%COMPANY%.se, OU=%COMPANY% security, O=%COMPANY%, L=%COMPANY%, S=Stockholm, C=SW"
)

goto :EOF

:: -------------------------------------------------------------------------------------
:error
echo ERROR:
echo Script for running require proper path to JDK.
echo %1 can not be found. Please fix script variables declaration.
echo Please set correctly JDK script variable.
echo Search path is: "%JDK%\%1"
goto :EOF