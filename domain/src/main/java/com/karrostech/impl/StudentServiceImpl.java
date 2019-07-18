package com.karrostech.impl;

import com.karrostech.domain.Student;
import com.karrostech.domain.StudentSearchRequest;
import com.karrostech.interfaces.StudentRepository;
import com.karrostech.interfaces.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {

	private StudentRepository studentRepository;

	public StudentServiceImpl(StudentRepository repository) {
		this.studentRepository = repository;
	}

	public Student save(Student student) {
		return studentRepository.save(student);
	}

	public Student findById(String id) {
		return studentRepository.findById(id);
	}

	public List<Student> search(StudentSearchRequest searchRequest) {
		return studentRepository.search(searchRequest);
	}

	public List<Student> searchFullText(String query) {
		return studentRepository.searchFullText(query);
	}
}
