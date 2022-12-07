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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        //when & should
        client.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect((MockMvcResultMatchers.status().isOk()))
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
                .andExpect((MockMvcResultMatchers.status().isOk()))
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
                .andExpect((MockMvcResultMatchers.status().isOk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(23))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(5000));
    }
    @Test
    void should_get_employees_when_perform_get_given_employees_and_page_and_pageSize() throws Exception{
        //given
        Employee bob = employeeRepository.create(new Employee(10, "Bob",23, "Male", 5000));
        employeeRepository.create(new Employee(11, "Clara",13, "Female", 50000));
        employeeRepository.create(new Employee(12, "Peter",22, "Male", 8000));
        //when & should
        client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}", 1,2))
                .andExpect((MockMvcResultMatchers.status().isOk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Clara"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(23))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(5000));
    }

    @Test
    void should_post_employee_when_perform_post_given_employees_details() throws Exception{
        //given
        Employee bob = new Employee(10, "Bob",23, "Male", 5000);
        //when & should
        client.perform(MockMvcRequestBuilders.post("/employees")
                        .content(new ObjectMapper().writeValueAsString(bob))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.status().isCreated()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(23))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(5000));

        Employee newBob = employeeRepository.findAll().get(0);
        assertEquals("Bob",newBob.getName());
        assertEquals(23,newBob.getAge());
        assertEquals("Male",newBob.getGender());
        assertEquals(5000,newBob.getSalary());

    }
    @Test
    void should_update_employee_when_perform_put_given_employees_id() throws Exception{
        //given
        Employee oldBob = employeeRepository.create(new Employee(10, "Bob",23, "Male", 5000));
        Employee changeBob = new Employee(30,55000);
                //when & should
        client.perform(MockMvcRequestBuilders.put("/employees/{id}", oldBob.getId())
                        .content(new ObjectMapper().writeValueAsString(changeBob))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.status().isOk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(55000));

        Employee newBob = employeeRepository.findAll().get(0);
        assertEquals("Bob",newBob.getName());
        assertEquals(30,newBob.getAge());
        assertEquals("Male",newBob.getGender());
        assertEquals(55000,newBob.getSalary());

    }
}
