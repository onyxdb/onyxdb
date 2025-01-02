FROM amazoncorretto:21

WORKDIR /mongodb-k8s-operator

COPY ./mongodb-k8s-operator/build/libs/*.jar /mongodb-k8s-operator/mongodb-k8s-operator.jar

ENTRYPOINT ["java", "-jar", "mongodb-k8s-operator.jar"]
