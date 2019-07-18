# Student elasticsearch

### Project Overview
The project implements restful services to push/retrieve the data from elasticsearch server.
The project applies Onion architecture, currently there are 4 modules:
  - domain: contains the domain model and business logic of the project, interfaces. It interacts with the outer layers by the interfaces. This is essentially the dependency inversion principle. The domain module only uses the builtin java library, it's independent and very easy to test. At the moment, as the project is simple, there are not much business logic here.
  - Application services: here application specific logic i.e. our use cases reside, currently there is nothing
  - repository: the implementation of repository interface in domain module, elasticsearch is used in this module. We can easily switch over to the other database without changing the domain module.
  - student-rest-services: The restful services to post/retrieve the data in to elasticsearch server.

### Data model

| Key        | Type           | Required field  | Searchable|
| ------------- |:-------------:| -----:| ------:|
| id      | String | **Y** |**Y**|
| firstName      | String      |   **Y** | **Y**|
| lastName | String      |    **Y** | **Y** |
| middleName | String      |    N | N |
| schoolCode | String      |    **Y** | **Y** |
| grade | String      |    **Y** | **Y** |
| address | Json Object      |    N | N |
| address.address1 | String      |    N | N |
| address.city | String      |    N | N |
| address.state | String      |    N | N |
| address.zip | String      |    N | N |
| specialEd | boolean      |    N | N |
| medicalNotes | String      |    N | N |

### Integration test
Maven fail safe plugin will run all the integration tests, here I configured it to run only the test ended with "IntegrationTest"


Before running integration tests, we will start elasticsearch container in docker using testcontainers maven plugin
```
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>testcontainers</artifactId>
  <version>1.11.4</version>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>elasticsearch</artifactId>
  <version>1.11.4</version>
  <scope>test</scope>
</dependency>
```

in StudentServicesIntegrationTest, start elasticsearch container:
```
ElasticsearchContainer container = new CustomElasticSearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.7.0");
```

Start elasticsearch before running the tests:
```
container.start();
```

Stop it after the tests:
```
container.stop();
```

Run the integration test using maven
```
mvn clean verify
```

### Manual test
If you want to test manually, you have to start elasticsearch container yourself

Make sure that you have installed docker locally

First install and run
```
docker pull docker.elastic.co/elasticsearch/elasticsearch-oss:6.7.0
docker run -p 9200:9200 -p 9300:9300 --name it-elasticsearch -e "discovery.type=single-node"  docker.elastic.co/elasticsearch/elasticsearch-oss:6.7.0
```

Start and stop elasticsearch container
```
docker start it-elasticsearch
docker stop it-elasticsearch
```

Run the application: Right click on StudentServicesApplication.java --> run
