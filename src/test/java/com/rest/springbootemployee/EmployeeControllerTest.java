package com.rest.springbootemployee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    MockMvc client;
    @Autowired
    EmployeeRepository employeeRepository;
    @BeforeEach
    void cleanRepository() {
        employeeRepository.clearAll();
    }
    @Test
    void should_get_all_employees_when_perform_get_given_employees() throws Exception{
        //given
        employeeRepository.create(new Employee(10, "Bob",23, "Male", 5000));
        employeeRepository.create(new Employee(11, "Clara",13, "Female", 50000));
        //when & should
        client.perform(MockMvcRequestBuilders.get("/employees"))
                // 1. assert response status
                .andExpect((MockMvcResultMatchers.status().isOk()))
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(23))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(5000));
    }
    @Test
    void should_get_employee_when_perform_get_id_given_employees_and_ID() throws Exception{
        //given

        Employee bob = employeeRepository.create(new Employee(10, "Bob",23, "Male", 5000));
        employeeRepository.create(new Employee(11, "Clara",13, "Female", 50000));
        //when & should
        client.perform(MockMvcRequestBuilders.get("/employees/{id}",bob.getId()))
                // 1. assert response status
                .andExpect((MockMvcResultMatchers.status().isOk()))
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(23))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(5000));
    }
    @Test
    void should_get_employees_when_perform_get_given_employees_and_gender() throws Exception{
        //given

        Employee bob = employeeRepository.create(new Employee(10, "Bob",23, "Male", 5000));
        employeeRepository.create(new Employee(11, "Clara",13, "Female", 50000));
        employeeRepository.create(new Employee(12, "Peter",22, "Male", 8000));
        //when & should
        client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", "Male"))
                // 1. assert response status
                .andExpect((MockMvcResultMatchers.status().isOk()))
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(23))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(5000));
    }
}
