#!/bin/bash

# Configura el proxy con credenciales
git config --global http.proxy http://usuario:contraseña@proxy.example.com:8080
git config --global https.proxy http://usuario:contraseña@proxy.example.com:8080

# Bucle para realizar commits y pushes automáticos
while true; do
    git add .
    git commit -m "Auto commit: cambios guardados"
    git push origin main
    sleep 10 # Espera 10 segundos antes de verificar cambios nuevamente
done