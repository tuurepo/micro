FROM java:8
EXPOSE 9600

COPY . /opt/hystrix-dashboard/
WORKDIR /opt/hystrix-dashboard/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/hystrix-dashboard/target/hystrix-dashboard-0.0.1-SNAPSHOT.jar",\
            "--server.port=9600",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=hystrix-dashboard"]
