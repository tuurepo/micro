FROM java:8
EXPOSE 8031

COPY . /opt/payment-server/
WORKDIR /opt/payment-server/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/payment-server/target/payment-service-0.0.1-SNAPSHOT.jar",\
            "--server.port=8031",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=payment-service1"]
