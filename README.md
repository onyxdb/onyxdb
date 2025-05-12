# OnyxDB - DBaaS платформа в Kubernetes

**Примечания:**
- Метрики OnyxDB доступны по пути /actuator/prometheus. Пример конфигурации для сбора метрик лежит в файле ./deploy/onyxdb-service-scrape.yaml.
- В Deployment OnyxDB в файле ./deploy/onyxdb/onyxdb.yaml указаны требования к ресурсам для тестового окружения. Рекомендуется использовать минимум 4 vcpu, 8 ram для каждого инстанса.
- Следует принимать во внимание, что ресурсы Minikube расходуются и на запущенные базы данных, поэтому необходимо оценивать степень загрузки системы и при необходимости выделать Minikube больше ресурсов.
- При использовании разных Kubernetes Namespace учитывайте, что для корректной работы системы необходимо проверять выполняют ли компоненты обработку других неймспейсов. Например, операторы могут поддерживать один неймспейс или более. Перед реализацией необходимо ознакомиться с документацией компонентов системы. Также проверьте, что OnyxDB будет иметь права в указанном неймспейсе.

## API

Для работы с API доступен swagger ui по пути /swagger-ui.

API платформы закрыто авторизацией. При первом запуске платформы создается пользовать по умолчанию login=admin, password=admin.

Авторизуйтесь с помощью метода по пути /api/v1/auth/login. Пример тела запроса:
```json
{
  "username": "admin",
  "password": "admin"
}
```

В ответ вы получите токен для авторизации:
```json
{
  "accessToken": "accessToken",
  "refreshToken": "refreshToken"
}
```

Авторизуйтесь в swagger-ui через полученный accessToken и вы сможете пользоваться API платформы.

## Необходимые компоненты

Для запуска системы необходимы следующие компоненты:

- Percona MongoDB operator - версия 1.19.1
- PostgreSQL - версия 17
- Clickhouse - версия 25.4
- Redis - версия 8.0
- MinIO - версия RELEASE.2025-04-08T15-41-24Z
- VictoriaMetrics - версия 1.93.4
- VictoriaMetrics operator - версия 0.56.0
- VictoriaMetrics agent - версия 1.93.4
- VictoriaLogs - версия 1.21.0
- Vector agent - версия 0.46.1
- Grafana - версия 12.0.0
- Kubernetes - версия 1.31

Ниже приведены инструкции для развертывания системы в продуктовом и тестовом окружении.

## Продуктовое окружение

Для продуктового окружения нужны такие же компоненты, как и для тестового, однако подготовленная для тестирования
инструкция не является инструкцией по подготовке продуктового окружения с нуля. Это обусловлено тем, что многие
системы в продуктовой среде требуют более глубокой настройки, а так же дополнительную инфраструктуру, например,
для развертывания всех компонентов в отказоустойчивой конфигурации. В связи с этим мы пропустим развертывание
необходимых компонентов и перейдем к подготовке к развертыванию самой платформы. 

### Подготовка Grafana

- Добавьте VLogs datasource через UI.
- Добавьте Prometheus datasource через UI.
- Импортируйте дашборды из папки ./deploy/dashboards через UI.

### Подготовка MinIO

Создайте бакет с названием "onyxdb".

Создайте access key через UI, поместите данные ключа в секрет ./deploy/minio/onyxdb-minio.yaml и затем примените:
```shell
kubectl apply -f ./deploy/minio/onyxdb-secret.yaml
```

### Разверните компоненты OnyxDB

Необходимо прописать данные для секрета платформы в файле ./deploy/onyxdb/onyxdb.yaml. Рассмотрим параметры секрета:

```text
ONYXDB_SELF_URL - адрес OnyxDB в Kubernetes
ONYXDB_SELF_NAMESPACE - пространство имён, в котором запущен OnyxDB
ONYXDB_NAMESPACES - пространства имён, в которых OnyxDB может разворачивать кластеры баз данных
ONYXDB_STORAGE_CLASSES - Перечисление через запятую Kubernetes storage classes, которые можно использовать при развертывании кластеров баз данных
ONYXDB_POSTGRES_URL - URL подключения к PostgreSQL в формате JDBC
ONYXDB_POSTGRES_USER - пользователь PostgreSQL
ONYXDB_POSTGRES_PASSWORD - пароль PostgreSQL
ONYXDB_REDIS_HOST - хост Redis
ONYXDB_REDIS_PORT - порт Redis
ONYXDB_REDIS_USER - пользователь Redis
ONYXDB_REDIS_PASSWORD - пароль Redis
ONYXDB_CLICKHOUSE_URL - URL подключения к ClickHouse в формате JDBC
ONYXDB_CLICKHOUSE_USER - пользователь ClickHouse
ONYXDB_CLICKHOUSE_PASSWORD - пароль ClickHouse
ONYXDB_MINIO_URL - URL MinIO API
ONYXDB_MINIO_SECRET - название Kubernetes Secret, в котором хранится access-ключ для взаимодействия OnyxDB с MinIO
ONYXDB_MINIO_BUCKET - бакет MinIO, в который будут сохраняться резервные копии кластеров баз данных
ONYXDB_VLOGS_URL - URL VictoriaLogs
```

В качестве примера заполнения секрета значениями можно посмотреть дефолтный секрет в файле ./deploy/onyxdb/onyxdb.yaml.

После перезаписи тестовых данных секрета своими можно применять необходимые ресурсы:

```shell
kubectl apply -f ./deploy/onyxdb -n onyxdb
```

Поздравляю, теперь платформа готова к работе!

## Тестовое окружение

Данная инструкция подготавливает необходимые для тестирования компоненты в namespace "onyxdb".

### Установка Minikube

Установите Minikube согласно [документации](https://kubernetes.io/ru/docs/tasks/tools/install-minikube/).

### Подготовка Minikube

```shell
minikube start --driver=docker --memory=8000 --cpus=4 --disk-size=30g --kubernetes-version=v1.31.0
minikube create ns onyxdb
kubectl config set-context --current --namespace onyxdb
minikube addons enable metrics-server
```

### Подготовка Postgres

Примените ресурс:
```shell
kubectl apply -f ./deploy/postgres.yaml -n onyxdb
```

Создайте клиент Postgres:
```shell
kubectl -n onyxdb run -i --rm --tty postgres-client --image=postgres:17 --restart=Never -- bash -il
```

Попробуйте подключиться к Postgres:
```shell
psql postgresql://onyxdb:qwerty@postgres-onyxdb.onyxdb.svc.cluster.local:5432/onyxdb
```

### Подготовка Redis

Примените ресурс:
```shell
kubectl apply -f ./deploy/redis.yaml -n onyxdb
```

Создайте клиент Redis:
```shell
kubectl -n onyxdb run -i --rm --tty redis-client --image=redis:8.0 --restart=Never -- bash -il
```

Проверьте подключение к Redis:
```shell
redis-cli -u redis://onyxdb:qwerty@redis-onyxdb.onyxdb.svc.cluster.local:6379
```

### Подготовка Clickhouse

Примените ресурс:
```shell
kubectl apply -f ./deploy/clickhouse.yaml -n onyxdb
```

Создайте клиент Clickhouse:
```shell
kubectl -n onyxdb run -i --rm --tty clickhouse-client --image=clickhouse/clickhouse-server:25.4 --restart=Never -- bash -il
```

Проверьте подключение к Clickhouse:
```shell
clickhouse-client --host clickhouse-onyxdb.onyxdb.svc.cluster.local -u onyxdb --password qwerty
```

### Подготовка Percona MongoDB operator

Разверните Percona MongoDB operator согласно [документации](https://docs.percona.com/percona-operator-for-mongodb/minikube.html).

```shell
kubectl apply --server-side -f https://raw.githubusercontent.com/percona/percona-server-mongodb-operator/v1.19.1/deploy/bundle.yaml -n onyxdb
```

### Подготовка VictoriaMetrics operator

Разверните VictoriaMetrics согласно [документации](https://docs.victoriametrics.com/operator/setup/#installing-by-manifest).

```shell
kubectl apply -f ./deploy/vm-operator.yaml -n onyxdb
```

### Подготовка VictoriaMetrics

```shell
kubectl apply -f ./deploy/vm-cluster.yaml -n onyxdb
kubectl apply -f ./deploy/vm-agent.yaml -n onyxdb
```

### Подготовка VictoriaLogs

```shell
kubectl apply -f ./deploy/vlogs.yaml -n onyxdb
```

### Подготовка Vector agent

```shell
helm repo add vector https://helm.vector.dev
helm repo update

helm install -f ./deploy/vector/values.yaml vector vector/vector --version 0.42.1 -n onyxdb
```

### Подготовка Grafana

```shell
kubectl apply -f ./deploy/grafana -n onyxdb
```

Пробросьте 3000 порт:
```shell
kubectl port-forward service/grafana 3000:3000
```

Авторизуйтесь с данными по умолчанию:
```shell
user: admin
password: admin
```

Добавьте VLogs datasource через UI:
```shell
VLogs address: http://vlogs-onyxdb.onyxdb.svc.cluster.local:9428
```

Добавьте Prometheus datasource через UI:
```shell
VMetrics address: http://vmselect-onyxdb.onyxdb.svc.cluster.local:8481/select/0:0/prometheus
```

Импортируйте дашборды из папки ./deploy/dashboards через UI.

### Подготовка MinIO

Разверните MinIO:
```shell
kubectl apply -f ./deploy/minio/minio.yaml -n onyxdb
```

Пробросьте 9001 порт:
```shell
kubectl port-forward service/minio 9001:9001
```

Авторизуйтесь с данными по умолчанию:
```shell
user: minioadmin
password: minioadmin
```

Создайте бакет с названием "onyxdb".

Создайте access key через UI, поместите данные ключа в секрет ./deploy/minio/onyxdb-minio.yaml и затем примените:
```shell
kubectl apply -f ./deploy/minio/onyxdb-secret.yaml -n onyxdb
```

### Разверните компоненты OnyxDB

```shell
kubectl apply -f ./deploy/onyxdb -n onyxdb
```

Поздравляю, теперь платформа готова к работе!
