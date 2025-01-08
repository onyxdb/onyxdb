./gradlew :onyxdb-mongodb-operator:clean :onyxdb-mongodb-operator:build

docker build -t foxleren/onyxdb-mongodb-operator:testing -f mongodb-operator/docker/Dockerfile .
#docker push foxleren/onyxdb-mongodb-operator:testing
