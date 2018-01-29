FROM java:8
EXPOSE 8900

COPY . /opt/admin-server/
WORKDIR /opt/admin-server/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/admin-server/target/admin-server-0.0.1-SNAPSHOT.jar",\
            "--server.port=8900",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=admin-server"]
			