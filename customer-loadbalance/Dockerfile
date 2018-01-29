FROM java:8
EXPOSE 8200

COPY . /opt/customer-loadbalance/
WORKDIR /opt/customer-loadbalance/

VOLUME /logs
WORKDIR /

ENTRYPOINT ["java", "-jar",\
            "/opt/customer-loadbalance/target/customer-loadbalance-0.0.1-SNAPSHOT.jar",\
            "--server.port=8200",\
			"-Dspring.profiles.active=container",\
            "--spring.config.name=customer-loadbalance"]