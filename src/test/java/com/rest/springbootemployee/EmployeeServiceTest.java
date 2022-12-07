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
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
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
    void should_update_employee_when_post_given_employee_id() throws Exception {
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

}
