@echo off
cd C:\Projekte\se-carrental\CloudDeployment
echo ----------------------------------------------------
echo  Herzlich Willkommen zur Automation der Automation!
echo ----------------------------------------------------
echo.
@echo on
set DIRECTORY=C:\Projekte\se-carrental\CloudDeployment
docker run -d --network host --name cur_conv shipitplz/currency-webservice
docker run -d --network host --name car_rental shipitplz/car-rental-service
pause