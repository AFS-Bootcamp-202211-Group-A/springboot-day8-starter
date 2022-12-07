package com.rest.springbootemployee;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Test
    void should_return_employees_when_perform_find_by_employee_given_companies() {
        //given
        int companyId = 1;
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Sam", 20, "Male", 200000),
                new Employee(2, "Ken", 20, "Male", 200000)
        );
        Company company = new Company(companyId, "spring", employees);
        when(companyRepository.findById(companyId)).thenReturn(company);
        when(companyRepository.getEmployees(companyId)).thenReturn(employees);
        //when
        List<Employee> returnedEmployees = this.companyService.getEmployees(1);
        //then
        assertEquals(employees, returnedEmployees);
    }
    @Test
    void should_get_company_by_page_and_pageSize_when_perform_get_by_page_and_pageSize_through_service_given_companies() {
        //given
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Sam", 20, "Male", 200000),
                new Employee(2, "Ken", 20, "Male", 200000)
        );
        List<Company> companies = Arrays.asList(
                new Company(1, "spring", employees),
                new Company(2, "boot", employees)
        );
        when(companyRepository.findByPage(1,2)).thenReturn(companies);

        //when
        List<Company> returnedCompanies = this.companyService.findByPage(1,2);

        //this
        assertEquals(companies, returnedCompanies);
    }

    @Test
    void should_add_company_when_perform_through_service_add_given_companies() {
        //given
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Sam", 20, "Male", 200000),
                new Employee(2, "Ken", 20, "Male", 200000)
        );
        Company company = new Company(1, "spring", employees);

        when(this.companyRepository.create(company)).thenReturn(company);

        //when
        Company returnedCompany = this.companyService.create(company);

        //then
        assertEquals(company.getName(), returnedCompany.getName());
    }

    @Test
    void should_delete_company_when_perform_delete_given_companies() {
        // given
        int companyId = 1;
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Sam", 20, "Male", 200000),
                new Employee(2, "Ken", 20, "Male", 200000)
        );
        Company company = new Company(companyId, "spring", employees);
        when(companyRepository.findById(companyId)).thenReturn(company);
        // when
        companyService.delete(companyId);
        // then
        verify(companyRepository).delete(companyId);
        List<Company> returnedCompanies = companyRepository.findAll();
        assertThat(returnedCompanies, hasSize(0));
    }
}