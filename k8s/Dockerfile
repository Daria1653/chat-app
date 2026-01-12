FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/chat.war /usr/local/tomcat/webapps/chat.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
