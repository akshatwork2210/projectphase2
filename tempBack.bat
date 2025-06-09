@echo off
mysqldump -u root -ptv%%f*^! sample > "D:\CODING\src\resources\09_06_25___15_47_54_sample_backup.sql"
