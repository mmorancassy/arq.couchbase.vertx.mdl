FROM java:8
MAINTAINER MMCassy <danzig6661@gmail.com>
EXPOSE 8080

RUN echo 'Building arq.couchbase.vertx.mdl image...'

CMD ["java","-jar","/platform/target/platform-1.0-SNAPSHOT.jar"]