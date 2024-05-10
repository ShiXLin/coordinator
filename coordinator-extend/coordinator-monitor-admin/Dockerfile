FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Liam

RUN mkdir -p /coordinator/monitor/logs

WORKDIR /coordinator/monitor

EXPOSE 9090

ADD ./target/coordinator-monitor-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
