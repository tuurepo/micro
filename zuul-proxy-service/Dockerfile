FROM java:8
EXPOSE 8100

COPY . /opt/zuul-proxy-service/
WORKDIR /opt/zuul-proxy-service/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/zuul-proxy-service/target/zuul-proxy-service-0.0.1-SNAPSHOT.jar",\
            "--server.port=8100",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=zuul-proxy-service"]