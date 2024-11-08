# onyxdb - Managed databases in Kubernetes as a service

## Workflows

We have 2 types of checks:
1. light-check (`light-check.yml`) - runs on any pr and push to master branch.
This check builds monorepo without re-generating codegen.
2. heavy-check (`heavy-check.yml`) - runs by cron task one time per day.
This check re-generates all available codegen and builds monorepo.

## Gradle configurations

We have some custom Gradle plugins to simplify build configurations:
1. `onyxdb-java-app-conventions` - basic plugin for java applications.
2. `onyxdb-java-library-conventions` - basic plugin for java libraries.
3. `onyxdb-java-spring-app-conventions` - basic plugin for java spring applications.
4. `onyxdb-jooq-manual-conventions` - plugin allows to create postgres container, apply application migrations
and generate Jooq codegen.

To generate all available codegen like OpenApi or Jooq codegen you should use Gradle task `onyxdbGenerateAllCodegen`.
```shell
./gradlew onyxdbGenerateAllCodegen
```
