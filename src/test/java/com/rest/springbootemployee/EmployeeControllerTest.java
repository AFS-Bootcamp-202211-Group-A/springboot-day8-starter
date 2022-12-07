package com.rest.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


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
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
        //given
        employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));

        //when
        client.perform(MockMvcRequestBuilders.get("/employees"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
    }

    @Test
    void should_get_employee_by_id_when_perform_get_given_employee_id() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));

        //when

        client.perform(MockMvcRequestBuilders.get("/employees/{id}", susan.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
    }

    @Test
    void should_get_employees_by_gender_when_perform_get_given_employee_gender() throws Exception {
        //given
        employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 20, "Male", 10550));

        //when

        client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", "Male"))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10550));
    }

    @Test
    void should_get_employees_by_page_when_perform_get_given_employees() throws Exception {
        //given
        employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 20, "Male", 10550));
        employeeRepository.create(new Employee(12, "Mary", 20, "Male", 10550));


        //when
        client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}", 1, 2))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", contains("Susan", "Bob")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", contains(22, 20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", contains("Female", "Male")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", contains(10000, 10550)));
    }

    @Test
    void should_get_created_employee_when_perform_create_given_employees() throws Exception {
        //given

        Employee Dummy = new Employee(10, "Dummy", 21, "Female", 8000);


        //when&then
        client.perform(MockMvcRequestBuilders.post("/employees").content(asJsonString(Dummy)).contentType(MediaType.APPLICATION_JSON))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dummy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(21))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(8000))
                .andDo(print());

        //then
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getName(), equalTo("Dummy"));
        assertThat(employee.getAge(), equalTo(21));
        assertThat(employee.getGender(), equalTo("Female"));
        assertThat(employee.getSalary(), equalTo(8000));
    }

    @Test
    void should_update_employee_when_perform_update_given_employees() throws Exception {
        //given
        Employee susan = employeeRepository.create(new Employee(1, "Susan", 22, "Female", 10000));

        Employee Dummy = new Employee(2, "Dummy", 21, "Male", 8000);



        //when&then
        client.perform(MockMvcRequestBuilders.put("/employees/{id}", susan.getId()).content(asJsonString(Dummy)).contentType(MediaType.APPLICATION_JSON))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(21))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(8000))
                .andDo(print());

        //then
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getName(), equalTo("Susan"));
        assertThat(employee.getAge(), equalTo(21));
        assertThat(employee.getGender(), equalTo("Female"));
        assertThat(employee.getSalary(), equalTo(8000));
    }

    @Test
    void should_delete_employee_when_perform_delete_given_employees() throws Exception {
        //given
        employeeRepository.create(new Employee(1, "Susan", 22, "Female", 10000));

        //when&then
        client.perform(MockMvcRequestBuilders.delete("/employees/{id}", 2))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                // 2. assert response data
                .andDo(print());

        //then
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees, hasSize(0));
    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }







}