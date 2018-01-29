FROM java:8
EXPOSE 9400

COPY . /opt/zipkin-server/
WORKDIR /opt/zipkin-server/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/zipkin-server/target/zipkin-server-0.0.1-SNAPSHOT.jar",\
            "--server.port=9400",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=zipkin-server"]

			