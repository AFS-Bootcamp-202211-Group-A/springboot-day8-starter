package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

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
    void should_get_all_employees_when_perform_get_given_employees() {
        List<Employee> employeeList = new ArrayList<>();
        Employee bob = new Employee(10, "Bob", 23, "Male", 5000);
        employeeList.add(bob);

        when(employeeRepository.findAll()).thenReturn(employeeList);
//        when
        List<Employee> result = employeeService.findAll();
//        verify
        verify(employeeRepository).findAll();
        assertEquals(result, employeeList);
    }

    @Test
    void should_get_employee_when_perform_get_id_given_employees_and_ID() {
        //given
        Integer id = 2;
        Employee oldBob = new Employee(1, "Bob", 20, "Male", 0);
        Employee newBob = new Employee(2, "Bobe", 23, "Female", 5000);
        when(employeeRepository.findById(id)).thenReturn(newBob);
        //when
        Employee returnEmployee = employeeService.findById(id);
        //then
        verify(employeeRepository).findById(id);
        assertEquals(23, returnEmployee.getAge());
        assertEquals(5000, returnEmployee.getSalary());
        assertEquals("Bobe", returnEmployee.getName());
        assertEquals("Female", returnEmployee.getGender());
    }

    @Test
    void should_get_employees_when_perform_get_given_employees_and_gender() {
        String gender = "Male";
        Employee oldBob = new Employee(1, "Bob", 20, "Male", 0);
        Employee bobe = new Employee(2, "Bobe", 23, "Female", 5000);
        Employee alice = new Employee(3, "Alcie", 33, "Female", 5000);

        List<Employee> expectedEmployeeList = new ArrayList<>();
        expectedEmployeeList.add(oldBob);
        when(employeeRepository.findByGender(gender)).thenReturn(expectedEmployeeList);
        //when
        List<Employee> result = employeeService.findByGender(gender);
        //then
        verify(employeeRepository).findByGender(gender);
        assertEquals(20, result.get(0).getAge());
        assertEquals(0, result.get(0).getSalary());
        assertEquals("Bob", result.get(0).getName());
        assertEquals("Male", result.get(0).getGender());

    }

    @Test
    void should_update_employee_when_put_given_employee_id() {
        //given
        Integer id = 10;
        Employee oldBob = new Employee(id, "Bob", 20, "Male", 0);
        Employee newBob = new Employee(id, "Bobe", 23, "Female", 5000);
        when(employeeRepository.findById(id)).thenReturn(oldBob);
        //when
        Employee updatedBob = employeeService.update(id, newBob);
        //then
        verify(employeeRepository).findById(id);
        assertEquals(23, updatedBob.getAge());
        assertEquals(5000, updatedBob.getSalary());

        assertEquals("Bob", updatedBob.getName());
        assertEquals("Male", updatedBob.getGender());
    }


    @Test
    void should_post_employee_when_perform_post_given_employees_details() {
        List<Employee> employeeList = new ArrayList<>();
        Employee bob = new Employee(10, "Bob", 23, "Male", 5000);
        employeeList.add(bob);

        when(employeeRepository.create(bob)).thenReturn(bob);
//        when
        Employee result = employeeService.create(bob);
//        verify
        verify(employeeRepository).create(bob);
        assertEquals(result, bob);
    }
}
