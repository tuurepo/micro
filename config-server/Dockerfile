FROM java:8
EXPOSE 8888
COPY . /opt/config-server/
WORKDIR /opt/config-server/

VOLUME /config
WORKDIR /
ENTRYPOINT ["java","-jar",\
            "/opt/config-server/target/config-server-0.0.1-SNAPSHOT.jar",\
            "--server.port=8888",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=config-server"]
