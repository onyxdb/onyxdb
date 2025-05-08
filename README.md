# onyxdb - DBaaS platform in Kubernetes

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

TODO

### Deploy OnyxDB components

```shell
kubectl apply -f ./deploy/onyxdb
```
