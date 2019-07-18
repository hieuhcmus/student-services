package com.karrostech.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(type = "default", indexName = "students")
public class StudentModel {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String middleName;
	private String schoolCode;
	private String grade;
	private boolean specialEd;
	private String medicalNodes;

	@Field(type = FieldType.Object)
	private AddressModel address;

	public StudentModel() {
	}

	public StudentModel(String id, String firstName, String lastName,
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

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}
}
