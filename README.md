# Student elasticsearch

### Project Overview
The project implements restful services to push/retrieve the data from elasticsearch server.
The project applies Onion architecture, there are 4 modules:
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

### Restful services
#### Find Student by Id: GET /students/{id}
```
 curl -i localhost:8080/students/{id}
```

#### Save or edit a student: POST /students
```
curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" http://localhost:8080/students -d '{"firstName": "Hieu", "lastName" : "Tran", "middleName": "Van", "schoolCode": "UNS01", "address": {"city": "Austin", "zip": "78759"}}'
```
Posting a student with existing id will update the existing record in elasticsearch instead of creating a new one.


#### Search students by combination fields: GET /students/search?firstName=&lastName=&schoolCode=&grade=&id=
Only the students with the fields that match all the given search fields will be returned, the empty query param will be ignored.
```
curl -i localhost:8080//students/search?firstName=Hieu&lastName=Tran
```


#### Full text search: GET /students/searchFullText?query=
Full text search on the fields: firstName, lastName, schoolCode, grade, id
Student is returned if one of the fields matches the given query string.


### Integration test
Maven fail safe plugin will run all the integration tests, here I configured it to run only the test ended with "IntegrationTest"

Before running integration tests, we will start elasticsearch container in docker using testcontainers maven plugin.

Note: Make sure that you already installed docker, and it's up and running.
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

### Start the application
If you want to start the application for manual testing, you have to start elasticsearch container and the application

To start the application, Right click on StudentServicesApplication.java --> run
or in student-rest-services project, run
```
mvn spring-boot:run
```

Make sure that you have installed docker locally

First install and run
```
docker pull docker.elastic.co/elasticsearch/elasticsearch-oss:6.7.0
docker run -p 9200:9200 -p 9300:9300 --name it-elasticsearch -e "discovery.type=single-node"  docker.elastic.co/elasticsearch/elasticsearch-oss:6.7.0
```

Start elasticsearch container
```
docker start it-elasticsearch
```

Stop it after you finish testing
```
docker stop it-elasticsearch
```
