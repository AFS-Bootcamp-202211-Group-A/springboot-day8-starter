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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees(){

        //given
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        employees.add(employee);

        when(employeeRepository.findAll()).thenReturn(employees);

        //when
        List<Employee> result = employeeService.findAll();

        //then
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(employee));

        verify(employeeRepository).findAll();

    }

    @Test
    void should_update_only_age_and_salary_when_update_all_given_employees(){
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Susan", 22, "Female", 10000);

        Employee toUpdateEmployee = new Employee(employeeId, "Tom", 23, "Male", 12000);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        Employee updatedEmployee = employeeService.update(employeeId, toUpdateEmployee);

        verify(employeeRepository).findById(employeeId);
        assertThat(updatedEmployee.getAge(), equalTo(23));
        assertThat(updatedEmployee.getSalary(), equalTo(12000));
        assertThat(updatedEmployee.getName(), equalTo("Susan"));
        assertThat(updatedEmployee.getGender(), equalTo("Female"));

    }

    @Test
    void should_return_employee_by_id_when_find_by_id_given_employees(){
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Dummy1", 20, "Male", 13213);

        when(employeeRepository.findById(employeeId)).thenReturn(employee);

        Employee employeeById = employeeService.findById(employeeId);

        verify(employeeRepository).findById(employeeId);
        assertThat(employeeById.getAge(), equalTo(20));
        assertThat(employeeById.getSalary(), equalTo(13213));
        assertThat(employeeById.getName(), equalTo("Dummy1"));
        assertThat(employeeById.getGender(), equalTo("Male"));


    }

    @Test
    void should_return_employee_by_gender_when_find_by_gender_given_employees(){

        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<Employee> filteredEmployees = new ArrayList<>();
        Employee employee1 = new Employee(1, "Dummy1", 20, "Male", 13213);
        Employee employee2 = new Employee(2, "Dummy2", 21, "Male", 10000);
        employees.add(employee1);
        employees.add(employee2);


        when(employeeRepository.findByGender("Male")).thenReturn(employees);

        //when
        List<Employee> result = employeeService.findByGender("Male");

        //then
        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalTo(employee1));
        assertThat(result.get(1), equalTo(employee2));

        verify(employeeRepository).findByGender("Male");


    }

    @Test
    void should_return_employee_when_create_given_employee(){
        Employee employee3 = new Employee(1, "Dummy3", 18, "Male", 12000);

        when(employeeRepository.create(employee3)).thenReturn(employee3);

        Employee createdEmployee = employeeService.create(employee3);

        assertThat(createdEmployee, equalTo(employee3));
        verify(employeeRepository).create(employee3);

    }

    @Test
    void should_delete_employee_when_delete_given_employee_id(){
        int id = 1;
        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee6 = new Employee(id, "Dummy3", 18, "Male", 12000);
        employees.add(employee6);

        employeeService.delete(id);

        assertThat(employeeRepository.findAll(), hasSize(0));
        verify(employeeRepository).delete(id);

    }

    @Test
    void should_get_employees_by_page_when_get_by_page_given_page_and_page_size(){
        //given
        int page = 1;
        int pageSize = 2;
        List<Employee> employees = new ArrayList<>();
        Employee employee4 = new Employee(11, "Dummy4", 18, "Male", 12000);
        Employee employee5 = new Employee(12, "Dummy5", 20, "Male", 11000);
        employees.add(employee4);
        employees.add(employee5);

        when(employeeRepository.findByPage(page, pageSize)).thenReturn(employees);

        //when
        List<Employee> result = employeeService.findByPage(page, pageSize);

        //then
        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalTo(employee4));
        assertThat(result.get(1), equalTo(employee5));

        verify(employeeRepository).findByPage(page, pageSize);

    }






}