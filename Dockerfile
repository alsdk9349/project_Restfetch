# 기본 이미지로 OpenJDK 사용
FROM openjdk:11-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드 결과물(JAR 파일) 복사
COPY build/libs/my-spring-app.jar /app/backend.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]
