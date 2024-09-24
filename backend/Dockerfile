# 베이스 이미지 설정 (Java 11 사용)
FROM openjdk:11-jdk-slim

# 애플리케이션 JAR 파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar

# 애플리케이션 포트 설정
EXPOSE 8080

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app.jar"]
