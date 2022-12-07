package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    // SUT -> Service, DOC -> repository(mocked)
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    //1. verify interaction
    // when EmployeeService.findAll is called, it will call employeeRepository.findAll()
    // 2. verify data
    // return the data get from  employeeRepository.findAll() without any change
    @Test
    void should_return_all_employees_when_find_all_given_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "female", 10000);
        employees.add(employee);

        when(employeeRepository.findAll()).thenReturn(employees);

        //when
        List<Employee> result = employeeService.findAll();

        //then
        //1. verify data
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(employee));

        //2. verify interaction
        //spy
        verify(employeeRepository).findAll();
    }

    //1. verify interaction
    // when EmployeeService.findAll is called, it will call employeeRepository.findById()
    // 2. verify data
    // when input an employee, only the age and salary will be changed, name and gender remain unchanged
    @Test
    void should_update_only_age_and_salary_when_update_all_given_employees() {
        //given
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Susan", 22, "Female", 10000);
        Employee toUpdateEmployee = new Employee(employeeId, "Tom", 23, "Male", 12000);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        //when
        Employee updatedEmployee = employeeService.update(employeeId, toUpdateEmployee);

        //then
        //1. verify data age, salary
        //will change
        assertThat(updatedEmployee.getAge(), equalTo(23));
        assertThat(updatedEmployee.getSalary(), equalTo(12000));
        //will not change
        assertThat(updatedEmployee.getName(), equalTo("Susan"));
        assertThat(updatedEmployee.getGender(), equalTo("Female"));

        //2. verify interaction
        //spy
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void should_return_employee_with_id_when_find_by_id_given_employees() {
        //given
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Susan", 22, "Female", 10000);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        //when
        Employee updatedEmployee = employeeService.findById(employeeId);

        //then
        //1. verify data age, salary
        assertThat(updatedEmployee.getAge(), equalTo(22));
        assertThat(updatedEmployee.getSalary(), equalTo(10000));
        assertThat(updatedEmployee.getName(), equalTo("Susan"));
        assertThat(updatedEmployee.getGender(), equalTo("Female"));

        //2. verify interaction
        //spy
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void should_return_employees_with_correct_gender_when_find_by_gender_given_employees() {
        //given
        String gender= "female";
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "female", 10000);
        employees.add(employee);

        when(employeeRepository.findByGender(gender)).thenReturn(employees);

        //when
        List<Employee> returnedEmployees = employeeService.findByGender(gender);

        //then
        //1. verify data age, salary
        assertThat(returnedEmployees.size(), equalTo(1));
        assertThat(returnedEmployees.get(0).getAge(), equalTo(22));
        assertThat(returnedEmployees.get(0).getSalary(), equalTo(10000));
        assertThat(returnedEmployees.get(0).getName(), equalTo("Susan"));
        assertThat(returnedEmployees.get(0).getGender(), equalTo("female"));

        //2. verify interaction
        //spy
        verify(employeeRepository).findByGender(gender);
    }

    @Test
    void should_return_employee_when_create_given_employee() {
        //given

        Employee employee = new Employee(10, "Susan", 22, "female", 10000);

        when(employeeRepository.create(employee)).thenReturn(employee);

        //when
        Employee createEmployee = employeeService.create(employee);

        //then
        //1. verify data age, salary
        assertThat(createEmployee.getAge(), equalTo(22));
        assertThat(createEmployee.getSalary(), equalTo(10000));
        assertThat(createEmployee.getName(), equalTo("Susan"));
        assertThat(createEmployee.getGender(), equalTo("female"));

        //2. verify interaction
        //spy
        verify(employeeRepository).create(employee);
    }
}
