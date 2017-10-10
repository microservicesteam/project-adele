FROM microservicesteam/nutaxi-java:v0.1
VOLUME /tmp
ADD @artifact@ app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]