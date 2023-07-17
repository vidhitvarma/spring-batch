package com.old.employeesdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.old.employeesdata.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
