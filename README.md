
# Application description
Application is built as a command-line Spring Boot application.<br>
Application expects 'application.properties' file located in the same folder.<br>
Two modes were implemented:
* produce: read all records from the given table and send them to kafka topic
* consume: read messages from the kafka topic and write to another table

Application could be start in one mode only.<br>
The application has "--mode" argument to provide required mode.


# Test environment
Test environment could be setup by docker-compose.<br>
It consist of kafka, postgres, pgadmin, amhq.<br>
AMHQ: http://localhost:8080 UI to browse kafka.<br>
pgAdmin: http://localhost:8081 UI for postgres<br>
(need manually to add a server using settings from application.properties)


# Demo
## Preliminary steps:
1. Install docker - https://docs.docker.com/engine/install/
2. Install docker-compose - https://docs.docker.com/compose/install/


## Steps:
1. Get project
```
git clone https://github.com/dmitrii-bogdanov/kafkadbtransferer.git
cd kafkadbtransferer
```

2. Run test environment
```
docker-compose up
```

3. Build application image
```
docker build -t app .
```

4. Execute produce task
```
docker run -v $(pwd)/application.properties:/application.properties --network=test_env -e mode=produce -it app
```

5. Consume task
```
docker run -v $(pwd)/application.properties:/application.properties --network=test_env -e mode=consume -it app
```


