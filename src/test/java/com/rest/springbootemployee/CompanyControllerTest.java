package com.rest.springbootemployee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    MockMvc client;

    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void cleanRepository(){companyRepository.clearAll();}

    @Test
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        //given
        List<Employee> employees = new ArrayList<>();
        companyRepository.create(new Company(10, "Dummy Company", employees));

        //when&then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Dummy Company"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees").isArray());
    }

    @Test
    void should_get_company_by_id_when_perform_get_given_company_id() throws Exception {
        //given
        List<Employee> employees = new ArrayList<>();
        Company dummyCompany = companyRepository.create(new Company(10, "Dummy Company", employees));

        //when

        client.perform(MockMvcRequestBuilders.get("/companies/{id}", dummyCompany.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dummy Company"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees").isArray());

    }

    @Test
    void should_get_employees_from_company_when_perform_get_given_company_id() {
        

    }

    @Test
    void should_get_companies_by_page_when_perform_get_given_page_and_pageSize() {
    }

    @Test
    void should_get_created_company_when_perform_create_given_company() {
    }

    @Test
    void should_update_company_when_perform_update_given_companies() {
    }

    @Test
    void should_delete_company_when_perform_delete_given_companies() {
    }
}