@echo off
set DB_USER=root
set DB_NAME=sample
set DB_PASSWORD=tv%%f*!
set BACKUP_FILE=%CD%\src\resources\backup.sql

mysqldump -u %DB_USER% -p%DB_PASSWORD% %DB_NAME% > "%BACKUP_FILE%"

