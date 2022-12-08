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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        // given
        companyRepository.create(new Company(1, "c1", null));
        companyRepository.create(new Company(2, "c2", null));
        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("c1"));
    }

    @Test
    void should_return_company_when_perform_find_by_ID() throws Exception {
        //given
        Company company = companyRepository.create(new Company(1, "c1", null));
        companyRepository.create(new Company(2, "c2", null));
        //when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}"
                        ,company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("c1"));
    }
    @Test
    void should_return_employee_list_by_company_id_when_perform_get_employee_list_given_company() throws Exception {
        //given
        List<Employee> employeeList = Arrays.asList(
                new Employee(1, "Lily", 20, "Female", 8000),
                new Employee(2, "Bob", 11, "Male", 2100));
        Company company = companyRepository.create(new Company(1, "amazon", employeeList));

        //when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees", company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Lily"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(8000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(11));
    }

    @Test
    void should_return_company_by_page_and_pageSize_when_perform_find_by_page_and_pageSize_given_companies() throws Exception {
        //given
        Integer page = 1;
        Integer pageSize = 2;
        companyRepository.create(new Company(1, "c1", null));
        companyRepository.create(new Company(2, "c2", null));
        companyRepository.create(new Company(3, "c3", null));
        //when
        client.perform(MockMvcRequestBuilders.get("/companies?page={page}&pageSize={pageSize}"
                        , page, pageSize))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("c2"));
    }

    @Test
    void should_create_company_when_perform_post_given_company() throws Exception {
        // given
        Company company = new Company(1, "JaneStreet", null);
        // when & then
        client.perform(MockMvcRequestBuilders.post("/companies")
                        .content(new ObjectMapper().writeValueAsString(company))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("JaneStreet"));

        Company returnedCompany = companyRepository.findAll().get(0);
        assertEquals(returnedCompany.getName(), "JaneStreet");
    }

    @Test
    void should_update_when_perform_put_given_company() throws Exception {
        // given
        Company newCompany = new Company(1, "MorganStanley", null);
        Company oldCompany = companyRepository.create(new Company(1, "JPMorgan", null));
        // when & then
        client.perform(MockMvcRequestBuilders.put("/companies/{id}", oldCompany.getId())
                        .content(new ObjectMapper().writeValueAsString(newCompany))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("MorganStanley"));

        Company result = companyRepository.findAll().get(0);
        assertEquals("MorganStanley", result.getName());
    }

    @Test
    void should_delete_company_when_perform_delete_given_company() throws Exception {
        // given
        Company company = companyRepository.create(new Company(1, "MorganStanley", null));
        // when & then
        client.perform(MockMvcRequestBuilders.delete("/companies/{id}", company.getId()))
                .andExpect((MockMvcResultMatchers.status().isNoContent()));
        assertEquals(companyRepository.findAll().size(), 0);
    }

}
