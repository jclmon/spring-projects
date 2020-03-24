FROM openjdk:8-jre-alpine
ENV APP_FILE product-service-0.0.1-SNAPSHOT.jar
ENV APP_HOME /usr/apps
ENV PROFILE prod
EXPOSE 82
COPY target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$PROFILE -jar $APP_FILE"]