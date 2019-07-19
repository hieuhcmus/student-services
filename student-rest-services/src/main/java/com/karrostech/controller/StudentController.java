package com.karrostech.controller;

import com.karrostech.domain.Student;
import com.karrostech.domain.StudentSearchRequest;
import com.karrostech.dto.StudentDTO;
import com.karrostech.interfaces.StudentService;
import jdk.nashorn.internal.parser.TokenType;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Inject
	private StudentService studentService;

	@Inject
	private ModelMapper modelMapper;

	@GetMapping("/test")
	public String test() {
		return "Hello world!";
	}

	@GetMapping("/{id}")
	public StudentDTO findById(@PathVariable("id") String id) {
		return modelMapper.map(studentService.findById(id), StudentDTO.class);
	}

	@PostMapping
	public @Valid StudentDTO save(@RequestBody @Valid StudentDTO student) {
		return modelMapper.map(studentService.save(
				modelMapper.map(student, Student.class)), StudentDTO.class);
	}

	@GetMapping("/search")
	public @Valid List<StudentDTO> searchByCriteria(StudentSearchRequest request) {
		List<Student> students = studentService.search(request);
		Type type = new TypeToken<List<StudentDTO>>(){}.getType();
		return modelMapper.map(students, type);
	}

	@GetMapping("/searchFullText")
	public @Valid List<StudentDTO> searchFullText(@PathParam("query") String query) {
		Type type = new TypeToken<List<StudentDTO>>(){}.getType();
		List<Student> students = studentService.searchFullText(query);
		return modelMapper.map(students, type);
	}
}
