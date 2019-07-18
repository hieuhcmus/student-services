package com.karrostech.controller;

import com.karrostech.domain.Student;
import com.karrostech.domain.StudentSearchRequest;
import com.karrostech.interfaces.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Inject
	private StudentService studentService;

	@GetMapping("/test")
	public String test() {
		return "Hello world!";
	}

	@GetMapping("/{id}")
	public Student findById(@PathVariable("id") String id) {
		return studentService.findById(id);
	}

	@PostMapping
	public Student save(@RequestBody Student student) {
		return studentService.save(student);
	}

	@GetMapping("/search")
	public List<Student> searchByCriteria(StudentSearchRequest request) {
		return studentService.search(request);
	}

	@GetMapping("/searchFullText")
	public List<Student> searchFullText(@PathParam("query") String query) {
		return studentService.searchFullText(query);
	}
}
