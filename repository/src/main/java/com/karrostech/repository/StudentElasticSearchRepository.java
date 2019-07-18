package com.karrostech.repository;

import com.karrostech.model.StudentModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StudentElasticSearchRepository extends ElasticsearchRepository<StudentModel, String> {
}
