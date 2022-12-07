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
}
