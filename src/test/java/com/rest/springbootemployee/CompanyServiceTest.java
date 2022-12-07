package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_companies_when_find_all_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        Company company = new Company(1, "spring" , new ArrayList<>());
        companies.add(company);

        when(companyRepository.findAll()).thenReturn(companies);

        //when
        List<Company> result = companyService.findAll();

        //then
        //1. verify data
        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(company));

        //2. verify interaction
        //spy
        verify(companyRepository).findAll();
    }

    @Test
    void should_return_company_with_id_when_find_by_id_given_company() {
        //given
        int companyId = 1;
        Company company = new Company(companyId, "spring", new ArrayList<>());

        when(companyRepository.findById(companyId)).thenReturn(company);

        //when
        Company expectedCompany = companyService.findById(companyId);

        //then
        //1. verify data age, salary
        assertThat(expectedCompany.getId(), equalTo(companyId));
        assertThat(expectedCompany.getName(), equalTo("spring"));

        //2. verify interaction
        //spy
        verify(companyRepository).findById(companyId);
    }

    @Test
    void should_get_companies_by_page_when_get_by_page_given_companies() {
        //given
        final int PAGE = 2;
        final int PAGE_SIZE = 2;
        Employee susan = new Employee(1, "Susan", 22, "Female", 10000);
        Employee tom = new Employee(2, "Tom", 23, "Male", 20000);
        Employee sam = new Employee(3, "Sam", 24, "Male", 30000);
        Company spring = new Company(1, "spring", new ArrayList<>(Arrays.asList(susan)));
        Company boot = new Company(2, "boot",  new ArrayList<>(Arrays.asList(tom)));
        Company java = new Company(3, "java",  new ArrayList<>(Arrays.asList(sam)));
        List<Company> companies = new ArrayList<>(Arrays.asList(spring, boot, java));

        when(companyRepository.findByPage(PAGE, PAGE_SIZE)).thenReturn(
                companies.stream().skip(2).collect(Collectors.toList())
        );

        //when
        List<Company> returnedCompanies = companyService.findByPage(PAGE, PAGE_SIZE);

        //then
        //1. verify data
        assertThat(returnedCompanies.get(0).getName(), equalTo("java"));
        assertThat(returnedCompanies.get(0).getId(), equalTo(java.getId()));

        //2. verify interaction
        //spy
        verify(companyRepository).findByPage(PAGE, PAGE_SIZE);
    }

    @Test
    void should_update_only_name_when_update_given_company() {
        //given
        final int companyId = 1;
        Company company = new Company(companyId, "spring", new ArrayList<>());
        Company companyToBeUpdated = new Company(companyId, "boot", new ArrayList<>());

        when(companyRepository.findById(companyId)).thenReturn(company);

        //when
        Company updatedCompany = companyService.update(companyId, companyToBeUpdated);

        //then
        //1. verify data age, salary
        //will change
        assertThat(updatedCompany.getId(), equalTo(companyId));
        assertThat(updatedCompany.getName(), equalTo("boot"));
        //will not change
        assertThat(updatedCompany.getEmployees(), equalTo(company.getEmployees()));

        //2. verify interaction
        //spy
        verify(companyRepository).findById(companyId);
    }

    @Test
    void should_return_company_when_create_given_company() {
        //given

        Company company = new Company(1, "spring", new ArrayList<>());

        when(companyRepository.create(company)).thenReturn(company);

        //when
        Company createdCompany = companyService.create(company);

        //then
        //1. verify data age, salary
        assertThat(createdCompany.getId(), equalTo(company.getId()));
        assertThat(createdCompany.getName(), equalTo("spring"));

        //2. verify interaction
        //spy
        verify(companyRepository).create(company);
    }

    @Test
    void should_delete_company_when_delete_given_id() {
        //given
        final int companyId = 1;
        List<Company> companies = new ArrayList<>();
        Company company = new Company(companyId, "spring", new ArrayList<>());
        companies.add(company);

        when(companyRepository.findAll()).thenReturn(companies).thenReturn(new ArrayList<>());

        //when
        List<Company> result = companyService.findAll();
        companyService.delete(companyId);
        List<Company> deletedResult = companyService.findAll();

        //then
        //1. verify data
        assertThat(result, hasSize(1));
        assertThat(deletedResult, hasSize(0));

        //2. verify interaction
        //spy
        verify(companyRepository).delete(companyId);
    }
}
