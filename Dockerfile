FROM java:8u111-jdk

VOLUME /tmp

EXPOSE 8080

COPY ./build/libs/labs-0.0.1-SNAPSHOT.jar app.jar

CMD ["java","-Djava.security.egd=file:/dev/./urandom -Xdebug -Xrunjdwp:server=y,transport=dt_socket,suspend=n","-jar","/app.jar"]
