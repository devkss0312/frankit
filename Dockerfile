# Java 23 베이스 이미지 사용 (예: eclipse-temurin 23)
FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

# 빌드 후 생성된 task-1.0.0.jar 파일을 컨테이너로 복사
COPY build/libs/task-1.0.0.jar /app/task-1.0.0.jar

# 애플리케이션 포트 개방
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "task-1.0.0.jar"]