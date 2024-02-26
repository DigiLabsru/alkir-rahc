
> Сайт компании https://digilabs.ru \
> Чат в телеграмме - https://t.me/alkir_ru \
> Авторы - Никита Федькин и Антон Литвинов

# Alkir-RAHC - утилита для взаимодействия с кластером серверов 1С:Предприятие 8 по REST API/JSON RPC

Alkir-RAHC - это приложение на java, использующее библиотеку от Фирмы 1С для взаимодействия с сервером RAS. С его помощью можно взаимодействовать с любым количеством серверов 1С, выполняя автоматизированно рутинные задачи администрирования, которые раньше требовали консоли Администрирования серверов 1С:Предприятие 8 или rac.
Также на базе этого инструмента можно построить мониторинг кластера серверов 1С:Предприятие 8.

## Какие задачи решает Alkir-RAHC?

1. Получение информации по сеансам и соединениям, в том числе их отключение.
2. Получение информации по лицензиям, используемым клиентскими соединениями или рабочими процессами.
3. Получение состояние работоспособности кластера серверов 1С:Предприятие 8.
4. Получения состояния о работе серверов приложений (rphost, rmngr - pid-ы, время отклика сервера, используемая память и т.д.).

В планах:

1. Администрирование информационных баз на серверах 1С:Предприятие 8 (создание, удаление и изменение).
2. Администрирование рабочих серверов в кластере серверов 1С:Предприятие 8

## Конфигурация

Приложение можно конфигурировать через аргументы запуска или переменные среды. Для аргументов запуска разделителем групп параметров является символ точки, для переменных среды - "_".

Конфигурируемые параметры:

| Параметр | Описание | Значение по умолчанию | версия API | Обязательно |
| --- | --- | --- | --- | --- |
| auth.token | Токен авторизации для вызова методов `/api`. крайне рекомендуем настраивать в боевом окружении|  | v1/v2 | - |
| ras.port | Порт сервиса RAS | 1545 | v1 | + |
| ras.address | Адрес сервиса RAS | localhost | v1 | + |
| ras.timeout | Таймаут вызова сервиса RAS, в мс | 10000 | v1 | - |
| ras.clusterAdminUsername | Имя администратора кластера. В случае отсутствия - параметр не задается вообще |  | v1 | - |
| ras.clusterAdminPassword | Пароль администратора кластера. В случае отсутствия - параметр не задается вообще |  | v1 | - |
| ras.centralServerAdminUsername | Имя администратора центрального сервера (агента). В случае отсутствия - параметр не задается вообще |  | v1 | - |
| ras.centralServerAdminPassword | Пароль администратора центрального сервера (агента). В случае отсутствия - параметр не задается вообще |  | v1 | - |

Параметры группы "ras" необходимо конфигурировать только при использовании api v1. Для api v2 параметры авторизации
задаются в параметрах запроса.

## Запуск

Вариант запуска при использовании api v2
```shell
docker run --rm --name rahc -p 8080:8080 -d -e AUTH_TOKEN=12345 alkir/rahc:20211130
```

Вариант запуска при использовании api v1
```shell
docker run --rm --name rahc -p 8080:8080 -d -e RAS_ADDRESS=192.168.1.1 -e RAS_PORT=1545 -e AUTH_TOKEN=12345 alkir/rahc:20211130
```

## Работа с приложением

Основной порт взаимодействия - 8080.

Служебные эндпоинты:

* `/actuator` - сводные данные по [Spring Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.enabling)
* `/actuator/health` - статус приложения
* `/api-docs` - OpenAPI 3.0 спецификация в формате JSON
* `/api-docs.yaml` - OpenAPI 3.0 спецификация в формате YAML
* `/swagger-ui.html` - OpenAPI/Swagger UI песочница

Эндпоинты для основного взаимодействия с приложением:

* `/api/rest/*` - Взаимодействие по протоколу REST. Параметры и допустимые http-глаголы см. в Swagger UI.
* `/api/jsonrpc/*` - Взаимодействие по протоколу JSON-RPC

Каждый из протоколов взаимодействия поддерживает две версии API:

* `/api/*/v1` - Получение данных из сервера администрирования RAS, параметры подключения к которому настроены через переменные окружения/параметры запуска/`application.properties`.
* `/api/*/v2` - Получение данных из сервера администрирования RAS, параметры подключения к которому передаются в параметрах запроса.

Для обеих версий API доступны следующие ресурсы:
  * `*/cluster`
  * `*/clusterManager`
  * `*/infobase`
  * `*/session`
  * `*/workingProcess`
  * `*/workingServer`

### Взаимодействие по JSON-RPC

#### Общее

Приложение поддерживает две модели работы - REST-интерфейс и JSON-RPC. 

Каждый эндпоинт из REST-интерфейса продублирован в виде метода для JSON-RPC контроллера. 
Имя соответствующего метода для JSON-RPC вызова указано в документации к REST-эндпоинту в секции "json-rpc method".

Например, чтобы вызвать `/api/rest/v1/cluster/all` в виде JSON-RPC вызова необходимо выполнить запрос к эндпоинту `/api/jsonrpc/v1/cluster`, 
передав в теле запроса `"method": "list"`:

```json
{
  "jsonrpc": "2.0",
  "method": "list",
  "params": {},
  "id": 1
}
```

Ответ будет представлен в виде штатного JSON-RPC Response:

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": [
    {
      "clusterId": "81f8db64-f9d5-4cb6-b2f8-c6107f95f4e3",
      "expirationTimeout": 0,
      "hostName": "HOME-PC",
      "lifeTimeLimit": 0,
      "mainPort": 1541,
      "maxMemorySize": 0,
      "maxMemoryTimeLimit": 0,
      "name": "Локальный кластер",
      "securityLevel": 0,
      "sessionFaultToleranceLevel": 0,
      "loadBalancingMode": 0,
      "clusterRecyclingErrorsCountThreshold": 0,
      "clusterRecyclingKillProblemProcesses": false,
      "clusterRecyclingKillByMemoryWithDump": false
    }
  ]
}
```

Полный список методов так же можно посмотреть в Swagger-UI:

* находим нужный метод, например, `/api/jsonrpc/v1/infobase`;
* в секции "Request body" переключаемся на вкладку "Schema";
* раскрываем массив "Enum" у параметра "method".

#### Передача параметров

Большинство методов требует передачи дополнительных параметров. Параметры вызова JSON-RPC метода совпадают 
с параметрами запроса для REST-эндпоинта.

Параметры в JSON-RPC могут быть переданы либо в виде неименованного массива, либо в виде именованной структуры.
Имена параметров для JSON-RPC вызова указаны в описании соответствующего REST-эндпоинта в секции "json-rpc params".

Например, для вызова `/api/rest/v1/infobase/all` (json-rpc method: list) , требуется передать параметр `clusterId`.

В случае неименованного массива такой вызов `/api/jsonrpc/v1/infobase` выглядит так:

```json
{
  "jsonrpc": "2.0",
  "method": "list",
  "params": [
    "81f8db64-f9d5-4cb6-b2f8-c6107f95f4e3"
  ],
  "id": 1
}
```

В случае именованной структуры вызов `/api/jsonrpc/v1/infobase` выглядит так:

```json
{
  "jsonrpc": "2.0",
  "method": "list",
  "params": {
    "clusterId": "81f8db64-f9d5-4cb6-b2f8-c6107f95f4e3"
  },
  "id": 1
}
```

#### Необязательные параметры

Часть параметров может быть не обязательна к передаче. Например, параметры авторизации в информационной базе. 
В Swagger-UI такие параметры не отмечены красной звездочкой и текстом "required".
В секции описания параметров "json-rpc params" такие параметры имеют знак вопроса после имени, 
например `json-rpc params: clusterId, ibId, ibUsername?, ibPassword?`

При вызове метода по REST-интерфейсу такие параметры могут быть опущены, однако для JSON-RPC вызова наличие 
параметров **обязательно**. В качестве значений необязательных параметров необходимо передавать `null`.

Например, вызов `/api/jsonrpc/v1/infobase`, получение полных данных об информационной базе:

```json
{
  "jsonrpc": "2.0",
  "method": "getInfoBaseFull",
  "params": {
    "clusterId": "81f8db64-f9d5-4cb6-b2f8-c6107f95f4e3",
    "ibId": "0059b6fb-19ca-4e0f-973d-d3c360ce66cb",
    "ibUsername": null,
    "ibPassword": null
  },
  "id": 1
}

```

#### Передача авторизации в RAS

В API v2 необходимо передавать параметры авторизации в конкретном сервере администрирования RAS.
Все параметры кроме `address` и `port` являются необязательными, но могут требоваться конкретными эндпоинтами.

Для REST-протокола параметры авторизации передаются как query-params.

Например, для получения списка информационных баз в кластере необходимо вызвать
`/api/rest/v2/infobase/all?address=localhost&port=1545&clusterAdminUsername=admin&clusterAdminPassword=password&clusterId=81f8db64-f9d5-4cb6-b2f8-c6107f95f4e3` 

Для JSON-RPC протокола параметры авторизации передаются как отдельный параметр `сonnection`. 

Например, вызов `/api/jsonrpc/v2/infobase`, получение списка информационных баз в кластере:

```json
{
  "jsonrpc": "2.0",
  "method": "list",
  "params": {
    "connection": {
      "address": "localhost",
      "port": 1545,
      "clusterAdminUsername": "admin",
      "clusterAdminPassword": "password"
    },
    "clusterId": "81f8db64-f9d5-4cb6-b2f8-c6107f95f4e3"
  },
  "id": 1
}
```

Вместо параметров `clusterId` и `ibId` можно передавать параметры `clusterName` и `ibName` - в таком варианте
идентификаторы кластера и/или информационной базы будут рассчитаны автоматически.

Полный список полей параметра `connection` и их типы:

```json
{
  "address": "string",
  "port": 0,
  "clusterAdminUsername": "string",
  "clusterAdminPassword": "string",
  "centralServerAdminUsername": "string",
  "centralServerAdminPassword": "string",
  "clusterId": "string",
  "clusterName": "string",
  "ibId": "string",
  "ibName": "string",
  "ibUsername": "string",
  "ibPassword": "string"
}
```

### Примеры (на базе JSONRPC v2)

Получение списка кластеров:

```shell
curl --request POST \
  --url http://192.168.1.1:8080/api/jsonrpc/v2/cluster \
  --header 'authorization: Bearer bQ2Zd53Z' \
  --header 'content-type: application/json' \
  --header 'user-agent: vscode-restclient' \
  --data '{"jsonrpc": "2.0","method": "list","params": {"connection": {"address": "localhost","port": 1545}},"id": 1}'
```

Получение списка информационных баз кластера:

```shell
curl --request POST \
  --url http://192.168.1.1:8080/api/jsonrpc/v2/infobase \
  --header 'authorization: Bearer bQ2Zd53Z' \
  --header 'content-type: application/json' \
  --header 'user-agent: vscode-restclient' \
  --data '{"jsonrpc": "2.0","method": "list","params": {"connection": {"address": "localhost","port": 1545,"clusterName": "Локальный кластер"}},"id": 1}'
```

Получение списка сеансов информационной базы:

```shell
curl --request POST \
  --url http://192.168.1.1:8080/api/jsonrpc/v2/infobase \
  --header 'authorization: Bearer bQ2Zd53Z' \
  --header 'content-type: application/json' \
  --header 'user-agent: vscode-restclient' \
  --data '{"jsonrpc": "2.0","method": "sessions","params": {"connection": {"address": "localhost,"port": 1545,"clusterName": "Локальный кластер","ibName": "база бухгалтерии"}},"id": 1}'
```

Удаление сеанса (sid берется из данных о сеанса из запроса выше):

```shell
curl --request POST \
  --url http://192.168.1.1:8080/api/jsonrpc/v2/session \
  --header 'authorization: Bearer bQ2Zd53Z' \
  --header 'content-type: application/json' \
  --header 'user-agent: vscode-restclient' \
  --data '{"jsonrpc": "2.0","method": "delete","params": {"connection": {"address": "localhost","port": 1545,"clusterAdminUsername": "","clusterAdminPassword": "","clusterName": "Локальный кластер","ibName": "база бухгалтерии"},"sid": "1dd54758-749d-415b-a95d-778a0abf18ea","message": "тут ваше сообщение"},"id": 1}'
```

### Сборка из исходников

## Пререквизиты

Для локальной разработки или сборки контейнера через `bootBuildImage` используется openjdk17.

Для сборки и запуска приложения необходимо скачать и установить в локальный репозиторий
библиотеки программного java-интерфейса для административного сервера из состава : 
[1С:Исполнителя U-версии](https://releases.1c.ru/project/Executor).

Создайте в корне проекта каталог `ibis\lib` и скопируйте в него следующие файлы из архива 1С:Исполнителя:

* com._1c.v8.core-1.0.2.jar
* com._1c.v8.ibis.admin-1.6.8.jar
* com._1c.v8.ibis.swp-1.1.1.jar
* com._1c.v8.ibis-1.1.1.jar
* com._1c.v8.swp.netty-1.0.2.jar
* com._1c.v8.swp-1.0.2.jar

Затем в терминале выполнить команду:

```shell
./gradlew publishIbisToMavenLocal --offline
```

## Сборка

Приложение:

```shell
./gradlew bootJar
```

Артефакты сборки появятся в каталоге `build/libs`.

Docker-образ:

```shell
docker build -t alkir/rahc:20211130 . 
```

Docker-образ через `bootBuildImage`:

```shell
./gradlew bootBuildImage
```

Можно переопределить имя образа и тэг, передав аргумент `--imageName`, например:

```shell
./gradlew bootBuildImage --imageName=alkir/rahc:20211130
```
