package com.karrostech.domain;

public class Student {
	private String id;
	private String firstName;
	private String lastName;
	private String middleName;
	private String schoolCode;
	private String grade;
	private boolean specialEd;
	private String medicalNodes;

	private Address address;

	public Student() {
	}

	public Student(String id, String firstName, String lastName,
	               String schoolCode, String grade) {
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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public boolean isSpecialEd() {
		return specialEd;
	}

	public void setSpecialEd(boolean specialEd) {
		this.specialEd = specialEd;
	}

	public String getMedicalNodes() {
		return medicalNodes;
	}

	public void setMedicalNodes(String medicalNodes) {
		this.medicalNodes = medicalNodes;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
