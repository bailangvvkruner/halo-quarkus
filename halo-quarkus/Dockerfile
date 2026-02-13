FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
    openjdk-21-jre \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY halo-core/target/quarkus-app/lib/ /deployments/lib/
COPY halo-core/target/quarkus-app/*.jar /deployments/
COPY halo-core/target/quarkus-app/app/ /deployments/app/
COPY halo-core/target/quarkus-app/quarkus/ /deployments/quarkus/

ENV HALO_WORK_DIR=/root/.halo2
ENV HALO_UPLOAD_DIR=/root/.halo2/uploads
ENV HALO_THEME_DIR=/root/.halo2/themes
ENV HALO_PLUGIN_DIR=/root/.halo2/plugins
ENV HALO_INDEX_DIR=/root/.halo2/index

RUN mkdir -p ${HALO_WORK_DIR} ${HALO_UPLOAD_DIR} ${HALO_THEME_DIR} ${HALO_PLUGIN_DIR} ${HALO_INDEX_DIR}

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
