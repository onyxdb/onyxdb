## Деплой Victoria Metrics Operator

Для работы с Victoria Metrics и Victoria Logs необходим оператор.

Деплоим оператор с помощью манифеста по [гайду из доки](https://docs.victoriametrics.com/operator/setup/#installing-by-manifest)

```shell
k apply -f ./deploy/vm-operator.yaml
```

## Деплой Victoria Logs

```shell
k apply -f ./deploy/vlogs.yaml
```
