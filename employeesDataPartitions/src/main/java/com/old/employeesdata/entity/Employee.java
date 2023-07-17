package com.old.employeesdata.entity;

import javax.persistence.Entity;

import javax.persistence.Id;

import org.springframework.context.annotation.Primary;

@Entity
public class Employee{
	
	@Id
	private Integer employeeId;
	private String firstName;
	private String lastName;
	private String location;
	
	public Employee() {
		
	}

	public Employee(Integer employeeId, String firstName, String lastName, String location) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.location = location;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
