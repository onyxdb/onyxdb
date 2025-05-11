# onyxdb - DBaaS platform in Kubernetes

## Demo

### Deploy PostgreSQL

Apply resources:
```shell
kubectl apply -f ./deploy/postgres.yaml -n onyxdb
```

Create client:
```shell
kubectl -n onyxdb run -i --rm --tty postgres-client --image=postgres:17 --restart=Never -- bash -il
```

Connect to PostgreSQL:
```shell
psql postgresql://onyxdb:qwerty@postgres-onyxdb.onyxdb.svc.cluster.local:5432/onyxdb
```

Check connection:
```shell
\conninfo
```

Expected output:
```shell
You are connected to database "onyxdb" as user "onyxdb" on host "postgres-onyxdb.onyxdb.svc.cluster.local" (address "10.96.233.216") at port "5432".
```

## Development

This instruction launches the necessary components in namespace "onyxdb".

### Install Minikube

Follow [docs](https://kubernetes.io/ru/docs/tasks/tools/install-minikube/).

### Prepare Minikube

```shell
minikube start --memory=8000 --cpus=4 --disk-size=30g
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
kubectl apply -f ./deploy/vm-operator.yaml
```

### Deploy VictoriaMetrics

```shell
kubectl apply -f ./deploy/vm-cluster.yaml
kubectl apply -f ./deploy/vm-agent.yaml
```

### Deploy VictoriaLogs

```shell
kubectl apply -f ./deploy/vlogs.yaml
```

### Deploy Vector agent

```shell
helm repo add vector https://helm.vector.dev
helm repo update

helm install -f ./deploy/vector/values.yaml vector vector/vector
```

### Deploy Grafana

```shell
kubectl apply -f ./deploy/grafana
```

VLogs address: 

```shell
http://vlogs-onyxdb.onyxdb.svc.cluster.local:9428
```

VMetrics address:

```shell
http://vmselect-onyxdb.onyxdb.svc.cluster.local:8481/select/0:0/prometheus
```

### Deploy MinIO

Deploy MinIO:
```shell
kubectl apply -f ./deploy/minio/minio.yaml
```

Port forward service 9001 port
```shell
TODO
```

Login with default credentials:
```shell
user: minioadmin
password: minioadmin
```

Create access key via UI, place key data to ./deploy/minio/onyxdb-minio.yaml and then apply secret:
```shell
kubectl apply -f ./deploy/minio/onyxdb-secret.yaml
```

### Deploy OnyxDB components

```shell
kubectl apply -f ./deploy/onyxdb
```
