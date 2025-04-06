:loop
git add .
git commit -m "Auto commit: cambios guardados"
git push origin main
timeout /t 10 >nul
goto loop