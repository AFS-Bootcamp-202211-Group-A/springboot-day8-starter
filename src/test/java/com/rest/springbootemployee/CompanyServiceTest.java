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

}
