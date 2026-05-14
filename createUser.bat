@echo off

echo Creating database...
mysql -u root -pm!n8W74bLc#Iwt -e "CREATE DATABASE rahul;"
if errorlevel 1 goto error

echo Creating user...
mysql -u root -pm!n8W74bLc#Iwt -e "CREATE USER 'rahul'@'%%' IDENTIFIED BY 'rahul';"
if errorlevel 1 goto error

echo Granting privileges...
mysql -u root -pm!n8W74bLc#Iwt -e "GRANT ALL PRIVILEGES ON rahul.* TO 'rahul'@'%%';"
if errorlevel 1 goto error

echo Importing structure...
mysql --binary-mode=1 -u root -pm!n8W74bLc#Iwt rahul < structure.sql
if errorlevel 1 goto error

echo SUCCESS
exit /b 0

:error
echo FAILED
exit /b 1