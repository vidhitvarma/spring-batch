package com.old.employeesdata.processor;

import org.springframework.stereotype.Component;

import com.old.employeesdata.entity.Employee;

@Component
public class EmployeeProcessor implements org.springframework.batch.item.ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee employeeData) throws Exception {
		return employeeData;
	}
	
}
