package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Company company = new Company(1, "GoldmanSach", null);
        companies.add(company);
        when(companyRepository.findAll()).thenReturn(companies);

        // when
        List<Company> returnedCompanies = companyService.findAll();

        // then
        assertEquals(returnedCompanies.size(), 1);
        assertEquals(company, returnedCompanies.get(0));
        verify(companyRepository).findAll();
    }

    @Test
    void should_return_company_when_perform_find_by_ID_given_company() {
        // given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1,"ping", 12, "Female",12000));
        Company company = new Company(1, "goldmansach", employeeList);
        when(companyRepository.findById(company.getId())).thenReturn(company);
        // when
        companyService.findById(company.getId());
        // then
        verify(companyRepository).findById(company.getId());
        assertEquals(company.getName(), "goldmansach");
        assertEquals(company.getEmployees(), employeeList);
    }

    @Test
    void should_return_employee_list_when_perform_find_by_employee_list_given_company_ID() {
        //given
        List<Employee> employeeList = new  ArrayList<>();
        employeeList.add(new Employee(1, "john", 11, "Male", 100000));
        employeeList.add( new Employee(2, "cena", 23, "Male", 2000));
        Company company = new Company(1, "jpm", employeeList);
        when(companyRepository.findById(1)).thenReturn(company);
        when(companyRepository.getEmployees(1)).thenReturn(employeeList);

        //when
        List<Employee> result = this.companyService.getEmployees(1);

        //then
        assertEquals(employeeList, result);
    }

    @Test
    void should_return_company_by_page_and_pageSize_when_perform_find_by_page_and_pageSize_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1,"jpmorgan",null));
        companies.add(new Company(2,"goldmansach",null));
        companies.add(new Company(3,"morganstanley",null));
        when(companyRepository.findByPage(1,3)).thenReturn(companies);
        //when
        List<Company> result = this.companyService.findByPage(1,3);
        //this
        assertEquals(companies, result);
    }

    @Test
    void should_create_company_when_perform_create_given_company_detail() {
        //given
        List<Employee> employeeList = new  ArrayList<>();
        employeeList.add(new Employee(1, "john", 11, "Male", 100000));
        employeeList.add( new Employee(2, "cena", 23, "Male", 2000));
        Company company = new Company(1, "jpm", employeeList);

        when(this.companyRepository.create(company)).thenReturn(company);
        //when
        Company result = this.companyService.create(company);
        //then
        assertEquals(company.getName(), result.getName());
    }

}
