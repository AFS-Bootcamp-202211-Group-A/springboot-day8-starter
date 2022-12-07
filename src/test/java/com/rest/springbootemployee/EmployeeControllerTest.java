package com.rest.springbootemployee;

import com.rest.springbootemployee.Employee;
import com.rest.springbootemployee.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanRepository(){
        employeeRepository.clearAll();
    }

    @Test
    void should_get_all_employees_when_perform_get_given_employees() {
        //given
        Employee employee = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));

        //when
        try {
            client.perform(MockMvcRequestBuilders.get("/employees"))
                    // 1. assert response status
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    // 2. assert response data
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_get_employee_id_when_perform_get_given_employees() {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));

        //when
        try {
            client.perform(MockMvcRequestBuilders.get("/employees/{id}", susan.getId()))
                    // 1. assert response status
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    // 2. assert response data
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_get_employee_gender_when_perform_get_given_employees() {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 20, "Male", 10550));

        //when
        try {
            client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", "Male"))
                    // 1. assert response status
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    // 2. assert response data
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bob"))
//                    .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(20))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10550));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }









}