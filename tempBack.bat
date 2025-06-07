@echo off
mysqldump -u root -ptv%%f*^! SAMPLE > "D:\CODING\src\resources\07_06_25___15_04_02_SAMPLE_backup.sql"
