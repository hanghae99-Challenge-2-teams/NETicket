FROM openjdk:17
ENV TZ=Asia/Seoul

# 도커 허브에서 이미지를 가져와서 이미지를 작업한다
# FROM (이미지 이름:버전)

# 컨테이너 실행 전 작동할 명령
# RUN (명령)
# 타임존 설정 (설정을 하지 않으면 시간 저장시 다른 시간대로 저장됨)
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN echo Asia/Seoul > /etc/timezone

# 작업 파일을 변수화 하기
# ARG (변수명)=(파일명)
ARG JAR_FILE=build/libs/app.jar

# 작업 파일을 컨테이너로 복사
# COPY (파일명 또는 ${변수명}) (복사할 파일명)
COPY ${JAR_FILE} ./app.jar
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

# 컨테이너 시작 시 내릴 명령 (CMD와 ENTRYPOINT 차이 확인)
# ENTRYPOINT [(명령),(매개변수),(매개변수),(...)]
ENTRYPOINT ["java", "-jar", "./app.jar"]

