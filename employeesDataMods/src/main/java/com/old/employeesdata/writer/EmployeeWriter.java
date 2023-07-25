package com.old.employeesdata.writer;


import com.old.employeesdata.entity.Employee;
import com.old.employeesdata.repository.EmployeeRepository;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeWriter implements ItemWriter<Employee>{
	
	@Autowired
	public EmployeeRepository employeeRepository;

	@Override
	public void write(List<? extends Employee> items) throws Exception {
		System.out.println("Current Thread: " +Thread.currentThread().getName());
		employeeRepository.saveAll(items);
	}
}
