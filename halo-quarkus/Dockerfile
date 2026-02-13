FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
    openjdk-21-jre \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY halo-core/target/quarkus-app/lib/ /deployments/lib/
COPY halo-core/target/quarkus-app/*.jar /deployments/
COPY halo-core/target/quarkus-app/app/ /deployments/app/
COPY halo-core/target/quarkus-app/quarkus/ /deployments/quarkus/

ENV HALO_WORK_DIR=/root/.halo2

RUN mkdir -p ${HALO_WORK_DIR}

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
