package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
