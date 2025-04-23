# MDB

## Development

### Create namespace in Minikube

```shell
minikube create ns onyxdb
kubectl config set-context --current --namespace onyxdb
```

### Deploy Percona MongoDB Operator

[Install Percona Server for MongoDB on Minikube](https://docs.percona.com/percona-operator-for-mongodb/minikube.html)

```shell
kubectl apply --server-side -f https://raw.githubusercontent.com/percona/percona-server-mongodb-operator/v1.19.1/deploy/bundle.yaml -n onyxdb
```

### Deploy Victoria Metrics

[Гайд](https://docs.victoriametrics.com/guides/getting-started-with-vm-operator/#) 
```shell
helm repo add vm https://victoriametrics.github.io/helm-charts/
helm install vmoperator vm/victoria-metrics-operator
kubectl apply -f ./mdb/src/deploy/vm-cluster.yaml
```

Не забыть поставить агент по гайду!!!

