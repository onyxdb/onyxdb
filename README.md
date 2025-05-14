# onyxdb - DBaaS platform in Kubernetes

## Demo

### Create Kubernetes cluster

Master node with preset:
```shell
2 vcpu
8 ram
```

2 Worker nodes with preset:
```shell
4 vcpu
16 ram
```

### Prepare Kubernetes in Yandex Cloud

Apply custom storage class: 
```shell
kubectl apply -f ./deploy/yc/onyxdb-hdd.yaml
```

Reset default storage class:
```shell
 kubectl patch storageclass yc-network-hdd \
  -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"false"}}}'
```

Set new default storage class:
```shell
kubectl patch storageclass onyxdb-hdd \
-p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
```

Check new default storage class:
```shell
kubectl get storageclass
```

### Deploy Postgres

Apply resources:
```shell
kubectl apply -f ./deploy/postgres.yaml -n onyxdb
```

Create Postgres client:
```shell
kubectl -n onyxdb run -i --rm --tty postgres-client --image=postgres:17 --restart=Never -- bash -il
```

Connect to Postgres:
```shell
psql postgresql://onyxdb:qwerty@postgres-onyxdb.onyxdb.svc.cluster.local:5432/onyxdb
```

### Deploy Redis

Apply resources:
```shell
kubectl apply -f ./deploy/redis.yaml -n onyxdb
```

Create Redis client:
```shell
kubectl -n onyxdb run -i --rm --tty redis-client --image=redis:8.0 --restart=Never -- bash -il
```

Connect to Redis:
```shell
redis-cli -u redis://onyxdb:qwerty@redis-onyxdb.onyxdb.svc.cluster.local:6379
```

### Deploy Clickhouse

Apply resources:
```shell
kubectl apply -f ./deploy/clickhouse.yaml -n onyxdb
```

Create Clickhouse client:
```shell
kubectl -n onyxdb run -i --rm --tty clickhouse-client --image=clickhouse/clickhouse-server:25.4 --restart=Never -- bash -il
```

Connect to Clickhouse:
```shell
clickhouse-client --host clickhouse-onyxdb.onyxdb.svc.cluster.local -u onyxdb --password qwerty
```

## Development

This instruction launches the necessary components in namespace "onyxdb".

### Install Minikube

Follow [docs](https://kubernetes.io/ru/docs/tasks/tools/install-minikube/).

### Prepare Minikube

```shell
minikube start --memory=8000 --cpus=4 --disk-size=30g --kubernetes-version=v1.31.0
minikube create ns onyxdb
kubectl config set-context --current --namespace onyxdb
minikube addons enable metrics-server
```

### Deploy Percona MongoDB operator

Deploy Percona MongoDB operator according to [docs](https://docs.percona.com/percona-operator-for-mongodb/minikube.html).

```shell
kubectl apply --server-side -f https://raw.githubusercontent.com/percona/percona-server-mongodb-operator/v1.19.1/deploy/bundle.yaml -n onyxdb
```

### Deploy VictoriaMetrics operator

Deploy VictoriaMetrics according to [docs](https://docs.victoriametrics.com/operator/setup/#installing-by-manifest).

```shell
kubectl apply -f ./deploy/vm-operator.yaml -n onyxdb
```

### Deploy VictoriaMetrics

```shell
kubectl apply -f ./deploy/vm-cluster.yaml -n onyxdb
kubectl apply -f ./deploy/vm-agent.yaml -n onyxdb
```

### Deploy VictoriaLogs

```shell
kubectl apply -f ./deploy/vlogs.yaml -n onyxdb
```

### Deploy Vector agent

```shell
helm repo add vector https://helm.vector.dev
helm repo update

helm install -f ./deploy/vector/values.yaml vector vector/vector --version 0.42.1 -n onyxdb
```

### Deploy Grafana

```shell
kubectl apply -f ./deploy/grafana -n onyxdb
```

Port forward service 3000 port
```shell
kubectl port-forward service/grafana 3000:3000
```

Login with default credentials:
```shell
user: admin
password: admin
```

Add VLogs datasource via UI:
```shell
VLogs address: http://vlogs-onyxdb.onyxdb.svc.cluster.local:9428
```

Add Prometheus datasource via UI:
```shell
VMetrics address: http://vmselect-onyxdb.onyxdb.svc.cluster.local:8481/select/0:0/prometheus
```

Import dashboards from folder ./deploy/dashboards via UI.

### Deploy MinIO

Deploy MinIO:
```shell
kubectl apply -f ./deploy/minio/minio.yaml -n onyxdb
```

Port forward service 9001 port
```shell
kubectl port-forward service/minio 9001:9001
```

Login with default credentials:
```shell
user: minioadmin
password: minioadmin
```

Create bucket with name "onyxdb".

Create access key via UI, place key data to ./deploy/minio/onyxdb-minio.yaml and then apply secret:
```shell
kubectl apply -f ./deploy/minio/onyxdb-secret.yaml
```

### Deploy OnyxDB components

```shell
kubectl apply -f ./deploy/onyxdb
```
