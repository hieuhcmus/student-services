package com.karrostech.studentservices;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

public class CustomElasticSearchContainer extends ElasticsearchContainer {
	private static final String CLUSTER_NAME = "cluster.name";
	private static final String ELASTIC_SEARCH = "docker-cluster";

	public CustomElasticSearchContainer(String dockerImageName) {
		super(dockerImageName);
		this.addFixedExposedPort(9200, 9200);
		this.addFixedExposedPort(9300, 9300);
		this.waitingFor(Wait.forHttp("/"));
		this.waitingFor(Wait.forListeningPort());
		this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
	}
}
