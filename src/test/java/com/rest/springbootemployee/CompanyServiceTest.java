package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_companies_when_find_all_given_employees(){
        List<Company> companies = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        Company company = new Company(10, "Dummy Company", employees);
        companies.add(company);

        when(companyRepository.findAll()).thenReturn(companies);

        List<Company> result = companyService.findAll();

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(company));

        verify(companyRepository).findAll();


    }



}