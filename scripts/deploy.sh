#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=springboot2-webservice

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl springboot2-webservice | grep jar | awk '{print $1}')

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo ">새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1) # repository 내부에 위치한 jar 파일 중 최신 버전을 가져온다.

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한 추가"

chmod +x $JAR_NAME # 실행 권한 추가

echo "> $JAR_NAME 실행"

nohup java -jar \
-Dspring.config.location=classpath:/application.yml, classpath:/application-real.yml, /home/ec2-user/app/application-oauth.yml, /home/ec2-user/app/application-real-db.yml \
-Dspring.profiles.active=real \
$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
# nohup 실행 시 CodeDeploy는 무한 대기 -> 이 문제를 해결하기 위해 nohup.out 파일을 표준 입출력용으로 별도로 사용 (이렇게 하면 nohup.out 파일이 별도로 생성되지 않고, CodeDeploy 로그에 표준 입출력이 출력된다고 한다.)