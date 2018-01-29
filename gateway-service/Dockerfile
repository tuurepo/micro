FROM java:8
EXPOSE 8000

COPY . /opt/gateway-service/
WORKDIR /opt/gateway-service/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar",\
            "--server.port=8000",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=gateway-service"]