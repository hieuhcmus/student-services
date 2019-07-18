package com.karrostech.interfaces;

import com.karrostech.domain.Student;
import com.karrostech.domain.StudentSearchRequest;

import java.util.List;

public interface StudentRepository {
	Student save(Student student);

	Student findById(String id);

	/**
	 * combination search on the fields: firstName, lastname, id, schoolCode, grade
	 * @param searchRequest the search DTO
	 * @return list of Students that match the search query
	 */
	List<Student> search(StudentSearchRequest searchRequest);

	/**
	 * Full text search on the fields: firstName, lastname, id, schoolCode, grade
	 * @param query string to search
	 * @return list of students that match the search query
	 */
	List<Student> searchFullText(String query);
}
