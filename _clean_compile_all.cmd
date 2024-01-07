call ./mvnw.cmd clean install %*
cd demo1-mrjar
call ../mvnw.cmd clean install %*
cd ..
pause
