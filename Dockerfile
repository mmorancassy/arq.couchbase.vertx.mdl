FROM java:8
MAINTAINER MMCassy <danzig6661@gmail.com>

WORKDIR /tmp/arq-example
ADD /platform/target/platform-1.0-SNAPSHOT.jar /tmp/arq-example

EXPOSE 8080

CMD exec java -jar platform-1.0-SNAPSHOT.jar