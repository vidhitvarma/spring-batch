package com.old.employeesdata.config;

import com.old.employeesdata.entity.Employee;

public class EmployeeProcessor implements org.springframework.batch.item.ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee employeeData) throws Exception {
		return employeeData;
	}
	
}
