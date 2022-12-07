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


}
