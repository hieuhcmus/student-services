package com.karrostech.studentservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karrostech.controller.StudentController;
import com.karrostech.domain.Address;
import com.karrostech.domain.Student;
import com.karrostech.domain.StudentSearchRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.inject.Inject;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = StudentController.class, secure = false)
@ContextConfiguration(classes = {ElasticSearchConfiguration.class, StudentServicesApplication.class})
public class StudentServicesIntegrationTest {

	private static ElasticsearchContainer container;
	@Inject
	private MockMvc mockMvc;

	@Inject
	private ObjectMapper objectMapper;

	@Before
	public void init() throws Exception {
		container = new CustomElasticSearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.4.1");
		container.waitingFor(Wait.forHttp("/"));
		container.start();

		TransportAddress transportAddress = new TransportAddress(container.getTcpHost());
		Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
		TransportClient transportClient = new PreBuiltTransportClient(settings)
				.addTransportAddress(transportAddress);
		ClusterHealthResponse healths = null;

		//make sure that elasticsearch started before running the tests
		while (healths == null || !"GREEN".equals(healths.getStatus().name())) {
			System.out.print("Calling healthcheck...");
			Thread.sleep(5000);
			healths = transportClient.admin().cluster().prepareHealth().get();
		}
	}

	@After
	public void tearDown() {
		container.stop();
	}

	private Student saveOrUpdate(Student student) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/students")
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Student responseStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		Assert.assertEquals(200, result.getResponse().getStatus());
		return responseStudent;
	}

	private Student findStudentById(String studentId) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/students/" + studentId);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Student student = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		Assert.assertEquals(200, result.getResponse().getStatus());
		return student;
	}

	private List<Student> searchFullText(String query) throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/students/searchFullText?query=" + query);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		return objectMapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Student>>() {});
	}

	private List<Student> searchByCriteria(StudentSearchRequest request) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("id=" + request.getId() + "&").append("firstName=" + request.getFirstName() + "&")
				.append("lastName=" + request.getLastName() + "&").append("grade=" + request.getGrade() + "&")
				.append("schoolCode=" + request.getSchoolCode());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/students/search?" + stringBuilder.toString());
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		return objectMapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Student>>() {});
	}

	@Test
	public void testCreateStudent() throws Exception {
		Student s = new Student();
		s.setFirstName("Hieu");
		s.setLastName("Tran");
		s.setSchoolCode("ABC123");
		Address address = new Address();
		address.setCity("Austin");
		address.setZip("78759");
		s.setAddress(address);

		Student responseStudent = saveOrUpdate(s);
		Assert.assertNotNull(responseStudent.getId());
		Assert.assertEquals(s.getFirstName(), responseStudent.getFirstName());
		Assert.assertEquals(s.getFirstName(), responseStudent.getFirstName());
		Assert.assertEquals(s.getSchoolCode(), responseStudent.getSchoolCode());
		Assert.assertEquals(s.getAddress().getCity(), responseStudent.getAddress().getCity());
		Assert.assertEquals(s.getAddress().getZip(), responseStudent.getAddress().getZip());
	}

	@Test
	public void testUpdateStudent() throws Exception {
		Student s = new Student();
		s.setFirstName("Hieu");
		s.setLastName("Tran");
		s.setSchoolCode("Code1");
		Address address = new Address();
		address.setCity("Austin");
		address.setZip("78759");
		s.setAddress(address);

		Student updatedStudent = saveOrUpdate(s);
		Assert.assertNotNull(updatedStudent.getId());
		Assert.assertEquals(s.getFirstName(), updatedStudent.getFirstName());
		Assert.assertEquals(s.getFirstName(), updatedStudent.getFirstName());
		Assert.assertEquals(s.getSchoolCode(), updatedStudent.getSchoolCode());
		Assert.assertEquals(s.getAddress().getCity(), updatedStudent.getAddress().getCity());
		Assert.assertEquals(s.getAddress().getZip(), updatedStudent.getAddress().getZip());

		updatedStudent.setFirstName("Van");
		updatedStudent.setSchoolCode("Code2");
		saveOrUpdate(updatedStudent);

		Student student = findStudentById(updatedStudent.getId());
		Assert.assertEquals(updatedStudent.getId(), student.getId());
		Assert.assertEquals(updatedStudent.getFirstName(), student.getFirstName());
		Assert.assertEquals(updatedStudent.getSchoolCode(), student.getSchoolCode());
		Assert.assertEquals(updatedStudent.getAddress().getCity(), student.getAddress().getCity());
		Assert.assertEquals(updatedStudent.getAddress().getZip(), student.getAddress().getZip());
	}

	@Test
	public void testFullTextSearch() throws Exception {
		Student student1 = new Student("123", "Daniel", "Kolova", "SCHOOL1", "3.5");
		Student student2 = new Student("456", "Mark", "Steward", "SCHOOL1", "4.0");
		Student student3 = new Student("789", "David", "Jone", "SCHOOL2", "3.5");
		Student student4 = new Student("246", "Jyothi", "Kolova", "SCHOOL3", "3.5");
		Student student5 = new Student("135", "Tim", "Sterchi", "SCHOOL4", "3.5");
		Student student6 = new Student("579", "Sabine", "Do", "SCHOOL5", "3.5");
		saveOrUpdate(student1);
		saveOrUpdate(student2);
		saveOrUpdate(student3);
		saveOrUpdate(student4);
		saveOrUpdate(student5);
		saveOrUpdate(student6);

		List<Student> students = searchFullText("SCHOOL1");
		Assert.assertEquals(2, students.size());

		Assert.assertEquals(2, searchFullText("kolova").size());

		Assert.assertEquals(1, searchFullText("123").size());
		Assert.assertEquals(5, searchFullText("3.5").size());
	}

	@Test
	public void testSearchByCriteria() throws Exception {
		Student student1 = new Student("123", "Daniel", "Kolova", "SCHOOL1", "3.5");
		Student student2 = new Student("456", "Mark", "Steward", "SCHOOL1", "4.0");
		Student student3 = new Student("789", "David", "Jone", "SCHOOL2", "3.5");
		Student student4 = new Student("246", "Jyothi", "Kolova", "SCHOOL3", "3.5");
		Student student5 = new Student("135", "Tim", "Sterchi", "SCHOOL4", "3.5");
		Student student6 = new Student("579", "Sabine", "Do", "SCHOOL5", "3.5");
		saveOrUpdate(student1);
		saveOrUpdate(student2);
		saveOrUpdate(student3);
		saveOrUpdate(student4);
		saveOrUpdate(student5);
		saveOrUpdate(student6);

		StudentSearchRequest request = new StudentSearchRequest("", "", "kolova", "", "");

		List<Student> students = searchByCriteria(request);
		Assert.assertEquals(2, students.size());

		request = new StudentSearchRequest("", "Daniel", "kolova", "", "");
		Assert.assertEquals(1, searchByCriteria(request).size());

		request = new StudentSearchRequest("", "", "", "", "3.5");
		Assert.assertEquals(5, searchByCriteria(request).size());

		request = new StudentSearchRequest("", "David", "", "", "3.5");
		Assert.assertEquals(1, searchByCriteria(request).size());

		request = new StudentSearchRequest("123", "Daniel", "", "", "");
		Assert.assertEquals(1, searchByCriteria(request).size());
	}
}
