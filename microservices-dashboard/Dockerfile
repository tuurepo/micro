FROM java:8
EXPOSE 9700

COPY . /opt/microservices-dashboard/
WORKDIR /opt/microservices-dashboard/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/microservices-dashboard/target/microservices-dashboard-0.0.1-SNAPSHOT.jar",\
            "--server.port=9700",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=microservices-dashboard"]
