FROM amazoncorretto:21

WORKDIR /onyxdb

COPY ./build/export/platform.jar /onyxdb/onyxdb.jar

ENTRYPOINT ["java", "-jar", "onyxdb.jar"]
