FROM openjdk:11-jdk-slim

WORKDIR /app

# Gradle Wrapper와 설정 파일을 복사
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle

# 의존성 설치 및 빌드
RUN ./gradlew build --no-daemon

# 애플리케이션 JAR 파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar

# 애플리케이션 포트 설정
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
