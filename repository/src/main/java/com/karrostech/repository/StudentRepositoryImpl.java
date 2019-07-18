package com.karrostech.repository;

import com.karrostech.domain.Student;
import com.karrostech.domain.StudentSearchRequest;
import com.karrostech.interfaces.StudentRepository;
import com.karrostech.model.StudentModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class StudentRepositoryImpl implements StudentRepository {

	private StudentElasticSearchRepository repository;
	private ModelMapper modelMapper;
	private ElasticsearchTemplate elasticsearchTemplate;
	public StudentRepositoryImpl(StudentElasticSearchRepository repository, ModelMapper modelMapper,
	                             ElasticsearchTemplate elasticsearchTemplate) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	@Override
	public Student save(Student student) {
		return modelMapper.map(repository.save(modelMapper.map(student, StudentModel.class)), Student.class);
	}

	@Override
	public Student findById(String id) {
		Optional<StudentModel> optional = repository.findById(id);
		return optional.isPresent() ? modelMapper.map(optional.get(), Student.class) : null;
	}

	@Override
	public List<Student> search(StudentSearchRequest searchRequest) {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (searchRequest.getFirstName() != null && !searchRequest.getFirstName().isEmpty()) {
			queryBuilder = queryBuilder.must(QueryBuilders.matchQuery("firstName",
					searchRequest.getFirstName()));
		}
		if (searchRequest.getLastName() != null && !searchRequest.getLastName().isEmpty()) {
			queryBuilder = queryBuilder.must(QueryBuilders.matchQuery("lastName",
					searchRequest.getLastName()));
		}
		if (searchRequest.getId() != null && !searchRequest.getId().isEmpty()) {
			queryBuilder = queryBuilder.must(QueryBuilders.matchQuery("id",
					searchRequest.getId()));
		}
		if (searchRequest.getSchoolCode() != null && !searchRequest.getSchoolCode().isEmpty()) {
			queryBuilder = queryBuilder.must(QueryBuilders.matchQuery("schoolCode",
					searchRequest.getSchoolCode()));
		}
		if (searchRequest.getGrade() != null && !searchRequest.getGrade().isEmpty()) {
			queryBuilder = queryBuilder.must(QueryBuilders.matchQuery("grade",
					searchRequest.getGrade()));
		}
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
		List<StudentModel> students = elasticsearchTemplate.queryForList(searchQuery, StudentModel.class);
		Type type = new TypeToken<List<Student>>() {}.getType();
		return modelMapper.map(students, type);
	}

	@Override
	public List<Student> searchFullText(String query) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(query)
				.field("firstName").field("lastName").field("id").field("schoolCode").field("grade"));
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
		List<StudentModel> studentModels = elasticsearchTemplate.queryForList(searchQuery, StudentModel.class);

		Type type = new TypeToken<List<Student>>() {}.getType();
		return modelMapper.map(studentModels, type);
	}
}
