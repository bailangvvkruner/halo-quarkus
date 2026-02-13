#!/bin/bash

set -e

if [ -z "$POSTGRES_HOST" ]; then
  POSTGRES_HOST="postgres"
fi

if [ -z "$POSTGRES_PORT" ]; then
  POSTGRES_PORT="5432"
fi

if [ -z "$POSTGRES_DB" ]; then
  POSTGRES_DB="halo"
fi

if [ -z "$POSTGRES_USER" ]; then
  POSTGRES_USER="halo"
fi

if [ -z "$POSTGRES_PASSWORD" ]; then
  POSTGRES_PASSWORD="halo"
fi

echo "Waiting for PostgreSQL to be ready..."
until pg_isready -h $POSTGRES_HOST -p $POSTGRES_PORT; do
  echo "PostgreSQL is not ready yet, sleeping..."
  sleep 2
done

echo "PostgreSQL is ready!"

exec java -jar /deployments/quarkus-run.jar
