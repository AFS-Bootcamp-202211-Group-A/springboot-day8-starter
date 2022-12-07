package com.rest.springbootemployee;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    MockMvc client;

    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    void cleanRepository(){companyRepository.clearAll();}


    @Test
    void should_get_all_companies_when_get_given_companies() throws Exception {
        //given
        companyRepository.create(new Company(1, "Company A", new ArrayList<>()));
        companyRepository.create(new Company(2, "Company B", new ArrayList<>()));


        //when&then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Company A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Company B"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].employees").isArray());
    }

    @Test
    void should_get_corresponding_company_when_perform_get_by_id_given_companies() throws Exception {
        //given
        Company company = companyRepository.create(new Company(1, "Company A", new ArrayList<>()));
        companyRepository.create(new Company(2, "Company B", new ArrayList<>()));
        //when&then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Company A"))
                .andExpect(MockMvcResultMatchers.jsonPath("employees").isArray());
    }

    @Test
    void should_get_employees_when_get_employees_given_company_id() throws Exception {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(10, "Tom", 22, "Male", 13000));
        Company company = companyRepository.create(new Company(10, "Company", employees));

        //when

        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees", company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tom"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(13000));
    }

    @Test
    void should_get_companies_when_get_company_by_page_given_companies() throws Exception {
        //given
        companyRepository.create(new Company(1, "Company A", new ArrayList<>()));
        companyRepository.create(new Company(2, "Company B", new ArrayList<>()));

        //when

        client.perform(MockMvcRequestBuilders.get("/companies?page={page}&pageSize={pageSize}", 1, 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Company A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Company B"));
    }

    @Test
    void should_get_new_company_when_create_given_company() throws Exception {
        //given
        List<Employee> employees = new ArrayList<>();
        Company company = new Company(10, "Company", employees);

        //when&then
        client.perform(MockMvcRequestBuilders.post("/companies")
                .content(asJsonString(company))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Company"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees").isArray());

        //then
        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company repoCompany= companies.get(0);
        assertThat(repoCompany.getName(), equalTo("Company"));
        assertThat(repoCompany.getEmployees(), equalTo(employees));
    }

   


    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
