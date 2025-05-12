FROM --platform=linux/amd64 amazoncorretto:21.0.7-alpine

WORKDIR /onyxdb

COPY ./build/export/platform.jar /onyxdb/onyxdb.jar

ENTRYPOINT ["java", "-jar", "onyxdb.jar"]
