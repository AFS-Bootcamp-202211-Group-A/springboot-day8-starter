package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_get_all_employees_when_perform_get_given_employees() {
        List<Employee> employeeList = new ArrayList<>();
        Employee bob = new Employee(10, "Bob", 23, "Male", 5000);
        employeeList.add(bob);

        when(employeeRepository.findAll()).thenReturn(employeeList);
//        when
        List<Employee> result = employeeService.findAll();
//        verify
        verify(employeeRepository).findAll();
        assertEquals(result,employeeList);
    }
    @Test
    void should_update_employee_when_put_given_employee_id(){
        //given
        Integer id = 10;
        Employee oldBob = new Employee(id, "Bob", 20, "Male", 0);
        Employee newBob = new Employee(id, "Bobe", 23, "Female", 5000);
        when(employeeRepository.findById(id)).thenReturn(oldBob);
        //when
        Employee updatedBob = employeeService.update(id, newBob);
        //then
        verify(employeeRepository).findById(id);
        assertEquals(23,updatedBob.getAge());
        assertEquals(5000,updatedBob.getSalary());

        assertEquals("Bob",updatedBob.getName());
        assertEquals("Male",updatedBob.getGender());
    }
    @Test
    void should_create_employee_when_post_given_employ_details(){
        //given
        Employee newEmployee = new Employee(10, "Bob", 20, "Male", 5500);
        when(employeeRepository.create(newEmployee)).thenReturn(newEmployee);
        //when
        Employee addedEmployee = employeeService.create(newEmployee);
        //then
        verify(employeeRepository).create(newEmployee);
        assertEquals("Bob", addedEmployee.getName());
        assertEquals(20, addedEmployee.getAge());
        assertEquals("Male", addedEmployee.getGender());
        assertEquals(5500, addedEmployee.getSalary());
    }
    @Test
    void should_delete_employee_when_delete_given_employee_id() {
        //given
        List<Employee> employees = new ArrayList<>();
        Integer employeeId = 10;
        Employee employee = new Employee(employeeId, "Bob", 20, "Male", 5500);
        employees.add(employee);
        //when
        employeeService.delete(employeeId);
        //then
        verify(employeeRepository).delete(employeeId);
        assertEquals(0, employeeService.findAll().size());
    }
    @Test
    void should_get_employee_when_get_given_employee_id(){
        Integer id = 10;
        Employee employee = new Employee(id, "Bob", 20, "Male", 5500);
        when(employeeRepository.findById(id)).thenReturn(employee);

        Employee foundEmployee = employeeService.findById(id);

        verify(employeeRepository).findById(id);
        assertEquals("Bob", foundEmployee.getName());
        assertEquals(20, foundEmployee.getAge());
        assertEquals("Male", foundEmployee.getGender());
        assertEquals(5500, foundEmployee.getSalary());
    }
    @Test
    void should_get_employees_when_get_given_employee_gender() {
        String gender = "Male";
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee(10, "Bob", 20, gender, 5500);
        Employee employee2 = new Employee(11, "Mary", 20, "Female", 500);
        employees.add(employee1);
        employees.add(employee2);
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(employee1);
        when(employeeRepository.findByGender(gender)).thenReturn(expectedEmployees);

        List<Employee> resultEmployees = employeeService.findByGender(gender);

        verify(employeeRepository).findByGender(gender);
        assertEquals("Bob", resultEmployees.get(0).getName());
        assertEquals(20, resultEmployees.get(0).getAge());
        assertEquals("Male", resultEmployees.get(0).getGender());
        assertEquals(5500, resultEmployees.get(0).getSalary());
    }
}
