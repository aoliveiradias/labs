#!/bin/sh

./gradlew build

docker rmi -f labs
docker rm  -f labs
docker build -t labs .
docker run --name labs -p 8080:8080 -t labs
