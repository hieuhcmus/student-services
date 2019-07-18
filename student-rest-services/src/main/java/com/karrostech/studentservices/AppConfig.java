package com.karrostech.studentservices;

import com.karrostech.impl.StudentServiceImpl;
import com.karrostech.interfaces.StudentRepository;
import com.karrostech.interfaces.StudentService;
import com.karrostech.repository.StudentElasticSearchRepository;
import com.karrostech.repository.StudentRepositoryImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
@ComponentScan(basePackages = {"com.karrostech"})
public class AppConfig {
	@Bean
	public StudentService studentService(StudentRepository studentRepository) {
		return new StudentServiceImpl(studentRepository);
	}

	@Bean
	public StudentRepository studentRepository(StudentElasticSearchRepository repository,
	                                           ModelMapper modelMapper,
	                                           ElasticsearchTemplate elasticsearchTemplate) {
		return new StudentRepositoryImpl(repository, modelMapper, elasticsearchTemplate);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
