package com.rest.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


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
    void should_get_employees_from_company_when_perform_get_given_company_id() throws Exception {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(10, "Susan", 22, "Female", 10000));
        Company dummyCompany = companyRepository.create(new Company(10, "Dummy Company", employees));

        //when

        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees", dummyCompany.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
    }

    @Test
    void should_get_companies_by_page_when_perform_get_given_page_and_pageSize() throws Exception {
        //given
        List<Employee> employees = new ArrayList<>();
        companyRepository.create(new Company(2, "Dummy Company1", employees));
        companyRepository.create(new Company(3, "Dummy Company2", employees));
        companyRepository.create(new Company(10, "Dummy Company3", employees));

        //when

        client.perform(MockMvcRequestBuilders.get("/companies?page={page}&pageSize={pageSize}", 1, 2))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Dummy Company1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Dummy Company2"));

    }

    @Test
    void should_get_created_company_when_perform_create_given_company() throws Exception {
        //given

        List<Employee> employees = new ArrayList<>();
        Company dummyCompany = new Company(10, "Dummy Company", employees);


        //when&then
        client.perform(MockMvcRequestBuilders.post("/companies").content(asJsonString(dummyCompany)).contentType(MediaType.APPLICATION_JSON))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dummy Company"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees").isArray());

        //then
        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company company= companies.get(0);
        assertThat(company.getName(), equalTo("Dummy Company"));
        assertThat(company.getEmployees(), equalTo(employees));
    }

    @Test
    void should_update_company_when_perform_update_given_companies() throws Exception {
        //given
        List<Employee> employees = new ArrayList<>();
        Company dummyCompany = companyRepository.create(new Company(10, "Dummy Company", employees));

        Company dummyCompanytoBeUpdated = new Company(10, "Dummy Company 2", employees);



        //when&then
        client.perform(MockMvcRequestBuilders.put("/companies/{id}", dummyCompany.getId()).content(asJsonString(dummyCompanytoBeUpdated)).contentType(MediaType.APPLICATION_JSON))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dummy Company 2"))
                .andDo(print());

//        //then
        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company company= companies.get(0);
        assertThat(company.getName(), equalTo("Dummy Company 2"));
        assertThat(company.getEmployees(), equalTo(employees));

    }

    @Test
    void should_delete_company_when_perform_delete_given_companies() {
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}