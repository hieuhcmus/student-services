package com.karrostech.domain;

public class StudentSearchRequest {
	private String id;
	private String firstName;
	private String lastName;
	private String schoolCode;
	private String grade;

	public StudentSearchRequest() {
	}

	public StudentSearchRequest(String id, String firstName, String lastName, String schoolCode, String grade) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.schoolCode = schoolCode;
		this.grade = grade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}
