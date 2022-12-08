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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
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
    void should_return_all_companies_when_perform_get_all_given_companies() throws Exception {
        // given
        companyRepository.create(new Company(1, "Company1", null));
        companyRepository.create(new Company(2, "Company2", null));

        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Company1", "Company2")));
    }
    @Test
    void should_return_company_by_id_when_perform_find_by_id_given_companies() throws Exception {
        //given
        Company company = companyRepository.create(new Company(1, "Company1", null));
        companyRepository.create(new Company(2, "Company2", null));

        //when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Company1"));
    }

    @Test
    void should_return_company_by_page_and_pageSize_when_perform_find_by_page_and_pageSize_given_companies() throws Exception {
        //given
        companyRepository.create(new Company(1, "Company1", null));
        companyRepository.create(new Company(2, "Company2", null));

        //when
        client.perform(MockMvcRequestBuilders.get("/companies?page={page}&pageSize={pageSize}", 1, 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Company1", "Company2")));
    }
    @Test
    void should_return_employee_by_company_id_when_perform_get_employee_given_companies() throws Exception {
        //given
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Lily", 20, "Female", 8000),
                new Employee(2, "Lily2", 21, "Female", 8100));
        Company company = companyRepository.create(new Company(1, "company", employees));
        
        //when & then
        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees", company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Lily"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(8000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Lily2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(21))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value(8100));
    }

    @Test
    void should_create_company_when_perform_create_given_company() throws Exception {
        // given
        Company company = new Company(1, "Company1", null);
        
        // when & then
        client.perform(MockMvcRequestBuilders.post("/companies")
                .content(new ObjectMapper().writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Company1"));

        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company returnedCompany = companies.get(0);
        assertThat(returnedCompany.getName(), equalTo("Company1"));
    }

    @Test
    void should_update_company_when_perform_update_given_companies() throws Exception {
        // given
        Company toUpdateCompany = new Company(1, "Company1_updateName", null);
        Company company = companyRepository.create(new Company(1, "Company1", null));
        
        // when & then
        String updateCompany1Json = new ObjectMapper().writeValueAsString(toUpdateCompany);
        client.perform(MockMvcRequestBuilders.put("/companies/{id}", company.getId())
                .content(updateCompany1Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Company1_updateName"));

        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company company_update = companies.get(0);
        assertEquals("Company1_updateName", company_update.getName());
    }

    @Test
    void should_delete_company_when_perform_delete_given_company() throws Exception {
        // given
        Company company = companyRepository.create(new Company(1, "Company1", null));
        companyRepository.create(new Company(2, "Company2", null));
        
        // when & then
        client.perform(MockMvcRequestBuilders.delete("/companies/{id}", company.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company returnedCompany = companies.get(0);
        assertEquals("Company2", returnedCompany.getName());
    }
}
