# 기본 이미지로 OpenJDK 17 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 설정 파일을 복사
COPY gradlew ./gradlew
COPY gradle ./gradle
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

# gradlew 파일에 실행 권한 부여
RUN chmod +x gradlew

# 의존성 설치
RUN ./gradlew dependencies

# 나머지 소스 코드 복사
COPY . ./  # 현재 컨텍스트에서 모든 파일 복사

# 애플리케이션 포트 설정
EXPOSE 8080

# Gradle 빌드 후 생성된 JAR 파일 복사
RUN ./gradlew build --no-daemon

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/build/libs/backend-0.0.1-SNAPSHOT.jar"]
