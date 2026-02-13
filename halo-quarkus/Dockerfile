# 使用 JRE 运行 JAR（轻量，不需要编译）
FROM amazoncorretto:21-alpine

WORKDIR /app

# 复制 JAR
COPY halo-core/target/quarkus-app/quarkus-run.jar /app/halo.jar

# 环境变量
ENV HALO_WORK_DIR=/root/.halo2
ENV TZ=Asia/Shanghai

# 设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建目录
RUN mkdir -p ${HALO_WORK_DIR}

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/halo.jar"]
