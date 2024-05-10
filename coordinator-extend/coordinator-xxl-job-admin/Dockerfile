FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Liam

RUN mkdir -p /coordinator/xxljob/logs

WORKDIR /coordinator/xxljob

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 9100

ADD ./target/coordinator-xxl-job-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
