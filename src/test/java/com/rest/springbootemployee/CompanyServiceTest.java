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
    void should_get_company_by_id_when_perform_get_by_id_through_service_given_companies() {
        // given
        List<Employee> employees = Arrays.asList(new Employee(1,"Susan", 22, "Female",1000));
        Company company = new Company(1, "spring", employees);
        when(companyRepository.findById(company.getId())).thenReturn(company);
        // when
        companyService.findById(company.getId());
        // then

        verify(companyRepository).findById(company.getId());
        assertEquals(company.getName(), "spring");
        assertEquals(company.getEmployees(), employees);
    }
    @Test
    void should_update_name_only_when_update_given_companies() {
        // given
        int companyId = 1;
        List<Employee> employees = Arrays.asList(new Employee(1,"Susan", 22, "Female",1000));
        Company company = new Company(companyId, "spring", new ArrayList<>());
        Company toUpdateCompany = new Company(companyId, "boot", employees);
        List<Company> companies = new ArrayList<>();
        companies.add(company);
        when(companyRepository.findById(companyId)).thenReturn(company);
        // when & then
        Company updatedCompany = companyService.update(companyId, toUpdateCompany);
        verify(companyRepository).findById(companyId);
        assertEquals(toUpdateCompany.getName(), updatedCompany.getName());
        assertEquals(company.getEmployees(), updatedCompany.getEmployees());
    }
}