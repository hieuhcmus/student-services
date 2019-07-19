package com.karrostech.dto;

import javax.validation.constraints.NotBlank;

public class StudentDTO {
	@NotBlank
	private String id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;
	private String middleName;

	@NotBlank
	private String schoolCode;

	@NotBlank
	private String grade;
	private boolean specialEd;
	private String medicalNodes;

	private AddressDTO address;

	public StudentDTO() {
	}

	public StudentDTO(String id, String firstName, String lastName,
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

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}
}
