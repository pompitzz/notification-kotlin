#!/bin/bash
ROOT=/home/ec2-user/app
nendPull=0

### Git pull 확인###
printf "Are you need to pull? [Y/N]"
tty_state=$(stty -g)
stty raw

char=$(dd bs=1 count=1 2>/dev/null)

stty "$tty_state"

echo

case "$char" in
[yYㅛ])
	needPull=1
	;;
[nNㅜ])
	needPull=0
	;;
*)
	echo "### Please enter Y or N"
	exit
	;;
esac
###################################


cd /home/ec2-user/app/notification-kotlin

### Git pull ###
if [ $needPull -eq 1 ]; then
	echo "### git pull ###"
	git pull origin master
	echo "### gradle build ###"
	./gradlew clean build
fi

pid=$(lsof -i TCP:8081 | tail -n 1 | awk '{ print $2 }')
if [ -n "${pid}" ]; then
	echo "### kill ${pid} ###"
	kill -9 "$pid"
fi

echo "### run application ###"
nohup java -jar \
-Dspring.config.location=classpath:/application-prod.yml,$ROOT/application-token.yml,$ROOT/application-prod-db.yml \
-Dspring.profiles.active=prod \
$ROOT/notification-kotlin/build/libs/notification-service-0.0.1-SNAPSHOT.jar > $ROOT/nohup.out 2>&1 &
