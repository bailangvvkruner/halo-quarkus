# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 复制所有源代码
COPY . .

# 构建项目
RUN mvn clean package -DskipTests -B

# 运行阶段
FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
    openjdk-21-jre-headless \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 从构建阶段复制 JAR
COPY --from=builder /app/halo-core/target/quarkus-app/quarkus-run.jar /app/quarkus-run.jar

ENV HALO_WORK_DIR=/root/.halo2

RUN mkdir -p ${HALO_WORK_DIR}

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]
