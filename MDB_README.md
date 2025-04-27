# MDB

## Development

### Create namespace in Minikube

```shell
minikube start --memory=8000 --cpus=4 --disk-size=30g
minikube create ns onyxdb
kubectl config set-context --current --namespace onyxdb
minikube addons enable metrics-server
```

### Deploy Percona MongoDB Operator

[Install Percona Server for MongoDB on Minikube](https://docs.percona.com/percona-operator-for-mongodb/minikube.html)

```shell
kubectl apply --server-side -f https://raw.githubusercontent.com/percona/percona-server-mongodb-operator/v1.19.1/deploy/bundle.yaml -n onyxdb
```

### Deploy VictoriaMetrics operator

https://docs.victoriametrics.com/operator/setup/#installing-by-manifest 
```shell
# Get latest release version from https://github.com/VictoriaMetrics/operator/releases/latest
export VM_VERSION=`basename $(curl -fs -o/dev/null -w %{redirect_url} https://github.com/VictoriaMetrics/operator/releases/latest)`

# Download manifest with webhook (requires CertManager to be preinstalled)
wget -O install.yaml https://github.com/VictoriaMetrics/operator/releases/download/$VM_VERSION/install-with-webhook.yaml

# Or download manifest without webhook
wget -O install.yaml https://github.com/VictoriaMetrics/operator/releases/download/$VM_VERSION/install-no-webhook.yaml

sed -i '' "s/namespace: vm/namespace: onyxdb/g" install.yaml

kubectl apply -f install.yaml
```

### Deploy VictoriaLogs

```shell
kubectl apply -f  vlogs.yaml
```

### Deploy VictoriaMetrics
```shell
k apply -f vm-cluster.yaml
k apply -f vm-agent.yaml
```

### Deploy Grafana

[Гайд](https://docs.victoriametrics.com/guides/getting-started-with-vm-operator/#)
```shell
kubectl apply -f ./src/deploy/grafana.yaml
kubectl apply -f ./src/deploy/grafana-config.yaml
```
