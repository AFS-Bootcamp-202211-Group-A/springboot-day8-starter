package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Integer id) {
        return employeeRepository.findById(id);
    }

    public Employee update(Integer id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id);
        if (employee.getAge() != null) {
            existingEmployee.setAge(employee.getAge());
        }
        if (employee.getSalary() != null) {
            existingEmployee.setSalary(employee.getSalary());
        }
        return existingEmployee;
    }

    public List<Employee> findByGender(String gender) {
        return employeeRepository.findByGender(gender);
    }


    public Employee create(Employee employee) {
        return employeeRepository.create(employee);
    }


    public List<Employee> findByPage(int page, int pageSize) {
        return employeeRepository.findByPage(page,pageSize);

    }

    public void delete(Integer id) {
        employeeRepository.delete(id);
    }
}
