# onyxdb - DBaaS platform in Kubernetes

## Gradle configurations

We have some custom Gradle plugins to simplify build configurations:
1. `onyxdb-java-app-conventions` - basic plugin for java applications.
2. `onyxdb-java-library-conventions` - basic plugin for java libraries.
3. `onyxdb-java-spring-app-conventions` - basic plugin for java spring applications.
4. `onyxdb-jooq-manual-conventions` - plugin allows to create postgres container, apply application migrations
and generate Jooq codegen.

To generate all available codegen like OpenAPI or Jooq codegen you should use Gradle task `onyxdbGenerateAllCodegen`.
```shell
./gradlew onyxdbGenerateAllCodegen
```
