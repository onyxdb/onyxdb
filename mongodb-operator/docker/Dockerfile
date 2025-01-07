FROM amazoncorretto:21

WORKDIR /onyxdb-mongodb-operator

COPY ./onyxdb-mongodb-operator/build/libs/*.jar /onyxdb-mongodb-operator/onyxdb-mongodb-operator.jar

ENTRYPOINT ["java", "-jar", "onyxdb-mongodb-operator.jar"]
