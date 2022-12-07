package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employee_when_find_all_given_employees() {
        // given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        // when
        List<Employee> results = employeeService.findAll();
        // then
        assertThat(results, hasSize(1));
        assertEquals(employee, results.get(0));
        verify(employeeRepository).findAll();
    }
    @Test
    void should_update_age_and_salary_only_when_update_all_given_employees() {
        // given
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Susan", 22, "Female", 10000);
        Employee toUpdateEmployee = new Employee(employeeId, "Lucy", 23, "Female", 2000);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeRepository.findById(employeeId)).thenReturn(employee);
        // when & then
        Employee updatedEmployee = employeeService.update(employeeId, toUpdateEmployee);
        verify(employeeRepository).findById(employeeId);
        assertEquals(toUpdateEmployee.getAge(), updatedEmployee.getAge());
        assertEquals(toUpdateEmployee.getSalary(), updatedEmployee.getSalary());
        assertEquals(employee.getName(), updatedEmployee.getName());
        assertEquals(employee.getGender(), updatedEmployee.getGender());
    }
}
