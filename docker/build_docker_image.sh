#!/bin/bash

set -e

BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)
COMMIT_HASH=$(git rev-parse --short HEAD)
IMAGE_VERSION="$BRANCH_NAME-$COMMIT_HASH"

./gradlew clean build

DOCKER_REPOSITORY="foxleren/onyxdb"
#DOCKER_IMAGE="$DOCKER_REPOSITORY:$IMAGE_VERSION"
DOCKER_IMAGE="$DOCKER_REPOSITORY:debugging"

docker build \
-t $DOCKER_IMAGE \
-f ./docker/onyxdb.dockerfile \
--network host \
.

docker push $DOCKER_IMAGE
