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
        List<Employee> returnedEmployees = employeeService.findAll();

        // then
        assertThat(returnedEmployees, hasSize(1));
        assertEquals(employee, returnedEmployees.get(0));
        verify(employeeRepository).findAll();
    }
    @Test
    void should_update_age_and_salary_only_when_update_given_employee() {
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

    @Test
    void should_return_employee_by_id_when_perform_find_by_id_given_employees() {
        // given
        Employee employee = new Employee(10, "Susan", 22, "Female", 10000);
        when(employeeRepository.findById(employee.getId())).thenReturn(employee);

        // when
        Employee returnedEmployee = employeeService.findById(employee.getId());

        // then
        verify(employeeRepository).findById(employee.getId());
        assertEquals(returnedEmployee.getAge(), 22);
        assertEquals(returnedEmployee.getSalary(), 10000);
        assertEquals(returnedEmployee.getName(), "Susan");
        assertEquals(returnedEmployee.getGender(), "Female");
    }

    @Test
    void should_return_employee_by_gender_when_perform_find_by_gender_given_employees() {
        // given
        String gender = "Female";
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee(10, "Susan", 22, gender, 10000);
        employees.add(employee);
        when(employeeRepository.findByGender(gender)).thenReturn(employees);

        // when
        List<Employee> returnedEmployees = employeeService.findByGender(gender);

        // then
        verify(employeeRepository).findByGender(gender);
        assertThat(returnedEmployees, hasSize(1));
        assertEquals(employee, returnedEmployees.get(0));
    }

    @Test
    void should_return_employee_by_page_and_pageSize_when_perform_find_by_page_and_pageSize_given_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        Employee bob = employeeRepository.create(new Employee(11, "Bob", 23, "Male", 20000));
        employeeRepository.create(new Employee(13, "Bob3", 24, "Male", 20000));
        employees.add(susan);
        employees.add(bob);

        when(employeeRepository.findByPage(1, 2)).thenReturn(employees);

        //when
        List<Employee> returnedEmployees = employeeService.findByPage(1,2);

        //then
        verify(employeeRepository).findByPage(1,2);
        assertThat(returnedEmployees, hasSize(2));
        assertEquals(susan, returnedEmployees.get(0));
        assertEquals(bob, returnedEmployees.get(1));
    }

    @Test
    void should_create_employee_when_perform_create_given_employee() {
        //given
        Employee susan = new Employee(10, "Susan", 22, "Female", 10000);
        when(employeeRepository.create(susan)).thenReturn(susan);

        //when
        Employee returnedEmployee = employeeService.create(susan);

        //then
        verify(employeeRepository).create(susan);
        assertEquals(susan, returnedEmployee);
    }
    @Test
    void should_delete_employee_when_perform_delete_given_employees() {
        // given
        int employeeId = 1;
        Employee employee = new Employee(employeeId, "Susan", 22, "Female", 10000);
        employeeRepository.create(employee);

        // when
        employeeService.delete(employeeId);

        // then
        verify(employeeRepository).delete(employeeId);
        List<Employee> newEmployees = employeeRepository.findAll();
        assertThat(newEmployees, hasSize(0));
    }
}
