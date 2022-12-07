package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {
    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_company_when_find_all_given_companies() {
        // given
        List<Company> companies = new ArrayList<>();
        Company company = new Company(1, "spring", null);
        companies.add(company);
        when(companyRepository.findAll()).thenReturn(companies);
        // when
        List<Company> results = companyService.findAll();
        // then
        assertThat(results, hasSize(1));
        assertEquals(company, results.get(0));
        verify(companyRepository).findAll();
    }

    @Test
    void should_update_name_only_when_update_all_given_companies() {
        // given
        int companyId = 1;
        Company company = new Company(companyId, "company1", null);
        Company toUpdateCompany = new Company(companyId, "company1_update", null);
        List<Company> companies = new ArrayList<>();
        companies.add(company);
        when(companyRepository.findById(companyId)).thenReturn(company);
        // when & then
        Company updatedCompany = companyService.update(companyId, toUpdateCompany);
        verify(companyRepository).findById(companyId);
        assertEquals(toUpdateCompany.getName(), updatedCompany.getName());
    }

}
