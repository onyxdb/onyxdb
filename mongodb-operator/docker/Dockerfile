FROM amazoncorretto:21

WORKDIR /mongodb-operator

COPY ./mongodb-operator/build/export/mongodb-operator.jar /mongodb-operator/mongodb-operator.jar

ENTRYPOINT ["java", "-jar", "mongodb-operator.jar"]
