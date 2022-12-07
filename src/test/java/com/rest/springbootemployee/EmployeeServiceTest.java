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

}
