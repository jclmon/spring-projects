FROM openjdk:8-jre-alpine
ENV APP_FILE api-gateway-0.0.1-SNAPSHOT.jar
ENV APP_HOME /usr/apps
ENV PROFILE prod
EXPOSE 8080
COPY target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -Dspring.profiles.active=$PROFILE -jar $APP_FILE"]