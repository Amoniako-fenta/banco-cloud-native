@echo off
title Banco Cloud - Inicio General

echo ============================================
echo   Levantando Banco Cloud (todo en un click)
echo ============================================

echo.
echo [1/8] Levantando Keycloak (Docker)...
cd /d "%~dp0keycloak"
docker compose up -d

echo.
echo [2/8] Levantando base de datos PostgreSQL (Docker)...
cd /d "%~dp0db"
docker compose up -d

echo.
echo [3/8] Levantando RabbitMQ (Docker)...
cd /d "%~dp0rabbitmq"
docker compose up -d

echo.
echo Esperando 25 segundos para que PostgreSQL,
echo RabbitMQ y Keycloak terminen de iniciar...
timeout /t 25 /nobreak >nul

echo.
echo [4/8] Levantando banco-cloud-usuarios...
start "usuarios" cmd /k "cd /d "%~dp0banco-cloud-usuarios" && mvnw.cmd spring-boot:run"

echo.
echo [5/8] Levantando banco-cloud-sucursales...
start "sucursales" cmd /k "cd /d "%~dp0banco-cloud-sucursales" && mvnw.cmd spring-boot:run"

echo.
echo [6/8] Levantando banco-cloud-cajeros...
start "cajeros" cmd /k "cd /d "%~dp0banco-cloud-cajeros" && mvnw.cmd spring-boot:run"

echo.
echo Esperando 5 segundos antes de iniciar el BFF...
timeout /t 5 /nobreak >nul

echo.
echo [7/8] Levantando banco-cloud-bff...
start "bff" cmd /k "cd /d "%~dp0banco-cloud-bff" && set KEYCLOAK_AUDIENCE=banco-cloud-frontend && mvnw.cmd spring-boot:run"

echo.
echo [8/8] Levantando banco-cloud-rabbit-admin...
start "rabbit-admin" cmd /k "cd /d "%~dp0banco-cloud-rabbit-admin" && mvnw.cmd spring-boot:run"

echo.
echo Levantando frontend Angular...

if not exist "%~dp0banco-cloud-frontend\node_modules" (
    echo.
    echo node_modules no existe.
    echo Instalando dependencias Angular...
    cd /d "%~dp0banco-cloud-frontend"
    call npm install
)

start "frontend" cmd /k "cd /d "%~dp0banco-cloud-frontend" && npm start"

echo.
echo ============================================
echo Banco Cloud iniciado.
echo ============================================
echo.
echo Servicios esperados:
echo.
echo Usuarios:        http://localhost:8081
echo Sucursales:      http://localhost:8082
echo Cajeros:         http://localhost:8083
echo BFF:             http://localhost:8084
echo Rabbit Admin:    http://localhost:8085
echo Frontend:        http://localhost:4200
echo RabbitMQ Admin:  http://localhost:15672
echo Keycloak:        http://localhost:8180
echo.
echo ============================================
echo Verificaciones:
echo.
echo Usuarios debe mostrar:
echo Started BancoCloudUsuariosApplication
echo.
echo Sucursales debe mostrar:
echo Started BancoCloudSucursalesApplication
echo.
echo Cajeros debe mostrar:
echo Started BancoCloudCajerosApplication
echo.
echo BFF debe mostrar:
echo Started BancoCloudBffApplication
echo.
echo Rabbit Admin debe mostrar:
echo Started RabbitAdminApplication
echo.
echo Angular debe estar disponible en:
echo http://localhost:4200
echo.
echo RabbitMQ:
echo http://localhost:15672
echo.
echo Keycloak:
echo http://localhost:8180
echo ============================================

pause