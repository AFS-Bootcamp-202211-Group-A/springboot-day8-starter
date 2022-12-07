package com.rest.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.springbootemployee.Employee;
import com.rest.springbootemployee.EmployeeRepository;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
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
    void should_get_all_companies_when_perform_getAll_given_companies() throws Exception {
        // given
        companyRepository.create(new Company(1, "Company1", null));
        companyRepository.create(new Company(2, "Company2", null));
        // when & then
        client.perform(MockMvcRequestBuilders.get("/companies"))
                // 1. assert response code
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Company1", "Company2")));
    }

    @Test
    void should_get_company_by_id_when_perform_get_by_id_given_company() throws Exception {
        //given
        Company company1 = companyRepository.create(new Company(1, "Company1", null));
        companyRepository.create(new Company(2, "Company2", null));
        //when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", company1.getId()))
                // 1. assert response status
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Company1"));
        //then
    }

}
