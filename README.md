# Student elasticsearch

### Project Overview
The project use Spring boot, spring elasticsearch data and restful web services to post/update/search the data in ElasticSearch 

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
Maven fail safe plugin will run all the integration tests, here I configured it to run only the test ended with "IT"

```
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-failsafe-plugin</artifactId>
  <configuration>
    <includes>
      <include>**/*IT.java</include>
    </includes>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>integration-test</goal>
        <goal>verify</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

Before running integration tests, we will start elasticsearch container in docker using docker-maven-plugin
```
<plugin>
  <groupId>io.fabric8</groupId>
  <artifactId>docker-maven-plugin</artifactId>
  <version>0.30.0</version>
  <executions>
    <execution>
      <id>prepare-it-database</id>
      <phase>pre-integration-test</phase>
      <goals>
        <goal>start</goal>
      </goals>
      <configuration>
        <images>
          <image>
            <name>docker.elastic.co/elasticsearch/elasticsearch-oss:6.7.0</name>
            <alias>it-elasticsearch</alias>
            <run>
              <ports>
                <port>9200:9200</port>
                <port>9300:9300</port>
              </ports>
              <wait>
                <http>
                  <method>GET</method>
                  <status>200</status>
                  <url>http://localhost:9200/_cat/health</url>
                </http>
                <time>60000</time>
              </wait>
            </run>
          </image>
        </images>
      </configuration>
    </execution>
    <execution>
      <id>remove-it-database</id>
      <phase>post-integration-test</phase>
      <goals>
        <goal>stop</goal>
      </goals>
      <configuration>
        <removeVolumes>true</removeVolumes>
      </configuration>
    </execution>
  </executions>
</plugin>
```
We stop the elasticsearch container after running the tests in post-integration-test phase.

Run the integration test using maven
```
mvn clean verify
```
### Start elasticsearch container manually
If you want to run integration test on IDE or if you want to manually test the services, you have to start and stop elasticsearch yourself

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
