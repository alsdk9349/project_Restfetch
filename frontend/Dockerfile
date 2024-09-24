# 베이스 이미지 설정 (안드로이드의 경우, 일반적으로 React Native를 사용하는 경우에 따라 다릅니다.)
FROM openjdk:11-jdk-slim

# 프론트엔드 앱 소스 코드 복사
COPY . /app

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 또는 필요한 의존성을 설치
RUN ./gradlew build

# 애플리케이션 실행 명령 (적절한 실행 명령으로 변경해야 함)
ENTRYPOINT ["java", "-jar", "/app/build/libs/my-frontend-app.jar"]
