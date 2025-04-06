#!/bin/bash

# Configura el proxy con credenciales
git config --global http.proxy http://raudelg.heredia:Tito2025*03@proxy.etecsa.cu:3128
git config --global https.proxy http://raudelg.heredia:Tito2025*03@proxy.etecsa.cu:3128

# Bucle para realizar commits y pushes automáticos
while true; do
    git add .
    git commit -m "Auto commit: cambios guardados"
    git push origin main
    sleep 10 # Espera 10 segundos antes de verificar cambios nuevamente
done