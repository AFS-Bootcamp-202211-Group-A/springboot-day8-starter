package com.rest.springbootemployee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    MockMvc client;
    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void cleanRepository() {
        companyRepository.clearAll();
    }

    @Test
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
        //given
        Employee susan = new Employee(1, "Susan", 22, "Female`", 10000);
        Employee tom = new Employee(2, "Tom", 23, "Male", 20000);
        Employee sam = new Employee(3, "Sam", 24, "Male", 30000);
        companyRepository.create(new Company(1, "spring", new ArrayList<>(Arrays.asList(susan, tom)) ));
        companyRepository.create(new Company(2, "boot",  new ArrayList<>(Arrays.asList(sam))));
        //when
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("spring", "boot")));

        //then
    }

//    @Test
//    void should_get_company_when_perform_get_by_id_given_companies() throws Exception {
//        //given
//        Employee employee = employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
//        //when
//        client.perform(MockMvcRequestBuilders.get("/employees/{id}", employee.getId()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Susan"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
//
//        //then
//    }
}
