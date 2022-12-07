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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

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
        //given
        Employee susan = new Employee(1, "Susan", 22, "Female", 10000);
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

    @Test
    void should_get_company_when_perform_get_by_id_given_companies() throws Exception {
        //given
        Employee susan = new Employee(1, "Susan", 22, "Female", 10000);
        Employee tom = new Employee(2, "Tom", 23, "Male", 20000);
        Employee sam = new Employee(3, "Sam", 24, "Male", 30000);
        Company spring = companyRepository.create(new Company(1, "spring", new ArrayList<>(Arrays.asList(susan, tom)) ));
        companyRepository.create(new Company(2, "boot",  new ArrayList<>(Arrays.asList(sam))));
        //when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(spring.getName()));

        //then
    }

    @Test
    void should_get_employees_of_the_company_when_get_employees_by_company_given_companies() throws Exception{
        //given
        Employee susan = new Employee(1, "Susan", 22, "Female", 10000);
        Employee tom = new Employee(2, "Tom", 23, "Male", 20000);
        Employee sam = new Employee(3, "Sam", 24, "Male", 30000);
        Company spring = companyRepository.create(new Company(1, "spring", new ArrayList<>(Arrays.asList(susan, tom)) ));
        companyRepository.create(new Company(2, "boot",  new ArrayList<>(Arrays.asList(sam))));
        //when


        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees", spring.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Tom")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(22, 23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(10000, 20000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Female", "Male")));

        //then
    }

    @Test
    void should_get_companies_in_page_when_get_companies_by_page_given_companies() throws Exception{
        //given
        Employee susan = new Employee(1, "Susan", 22, "Female", 10000);
        Employee tom = new Employee(2, "Tom", 23, "Male", 20000);
        Employee sam = new Employee(3, "Sam", 24, "Male", 30000);
        Company spring = companyRepository.create(new Company(1, "spring", new ArrayList<>(Arrays.asList(susan, tom)) ));
        companyRepository.create(new Company(2, "boot",  new ArrayList<>(Arrays.asList(sam))));
        //when


        client.perform(MockMvcRequestBuilders.get("/companies?page={page}&pageSize={pageSize}", 2,1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("boot"));

        //then
    }

    @Test
    void should_create_new_company_when_perform_post_given_new_company() throws Exception {
        Company newCompany = new Company(1, "spring", new ArrayList<>());
        String newCompanyJson = new ObjectMapper().writeValueAsString(newCompany);
        //given

        //when
        client.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCompanyJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spring"));

        //then
        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company comp = companies.get(0);
        assertThat(comp.getName(), equalTo("spring"));
    }

    @Test
    void should_update_existing_company_when_perform_put_given_company() throws Exception {
        Company existingCompany = companyRepository.create(new Company(1, "spring", new ArrayList<>()));

        Company companyToUpdated = new Company(2, "boot", new ArrayList<>());
        String companyToUpdatedJson = new ObjectMapper().writeValueAsString(companyToUpdated);

        //given

        //when
        client.perform(MockMvcRequestBuilders.put("/companies/{id}", existingCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(companyToUpdatedJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingCompany.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("boot"));

        //then
        List<Company> companies = companyRepository.findAll();
        assertThat(companies, hasSize(1));
        Company company = companies.get(0);
        assertThat(company.getName(), equalTo("boot"));
        assertThat(company.getId(), equalTo(existingCompany.getId()));
    }
}
