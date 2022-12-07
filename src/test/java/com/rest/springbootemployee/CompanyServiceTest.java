package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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
    void should_get_all_companies_when_get_given_companies(){
        //given
        List<Company> companies = new ArrayList<>();
        ArrayList<Employee> employeesOfCompany1 = new ArrayList<>();
        employeesOfCompany1.add(new Employee(1, "Carlos", 26, "Male", 70000));
        employeesOfCompany1.add(new Employee(2, "Nicole", 22, "Female", 80000));
        companies.add(new Company(100, "spring", employeesOfCompany1));
        companies.add(new Company(101, "autumn", employeesOfCompany1));
        when(companyRepository.findAll()).thenReturn(companies);
        //when
        List<Company> resultCompanies = companyService.findAll();
        //then
        verify(companyRepository).findAll();
        assertEquals(companies,resultCompanies);
    }
    @Test
    void should_get_company_when_get_given_company_id() {
        Integer id = 100;
        List<Company> companies = new ArrayList<>();
        Company company = new Company(id, "spring", new ArrayList<>());
        companies.add(company);
        companies.add(new Company(101, "autumn", new ArrayList<>()));
        when(companyRepository.findById(id)).thenReturn(company);

        Company resultCompany = companyService.findById(id);

        verify(companyRepository).findById(id);
        assertEquals("spring",resultCompany.getName());
    }
    @Test
    void should_get_employees_when_get_given_company_id() {
        Integer companyId = 100;
        List<Company> companies = new ArrayList<>();
        ArrayList<Employee> employeesOfCompany1 = new ArrayList<>();
        employeesOfCompany1.add(new Employee(1, "Carlos", 26, "Male", 70000));
        employeesOfCompany1.add(new Employee(2, "Nicole", 22, "Female", 80000));
        companies.add(new Company(companyId, "spring", employeesOfCompany1));
        companies.add(new Company(101, "autumn", employeesOfCompany1));
        when(companyRepository.getEmployees(companyId)).thenReturn(employeesOfCompany1);

        List<Employee> employees = companyService.getEmployees(companyId);

        verify(companyRepository).getEmployees(companyId);
        assertEquals("Carlos",employees.get(0).getName());
        assertEquals(26,employees.get(0).getAge());
        assertEquals("Male",employees.get(0).getGender());
        assertEquals(70000,employees.get(0).getSalary());
        assertEquals("Nicole",employees.get(1).getName());
        assertEquals(22,employees.get(1).getAge());
        assertEquals("Female",employees.get(1).getGender());
        assertEquals(80000,employees.get(1).getSalary());
    }
    @Test
    void should_get_companies_when_get_given_page_and_pageSize() {
        Integer page = 1;
        Integer pageSize = 1;
        List<Company> companies = new ArrayList<>();
        Company company = new Company(100, "spring", new ArrayList<>());
        companies.add(company);
        companies.add(new Company(101, "autumn", new ArrayList<>()));
        when(companyRepository.findByPage(page, pageSize)).thenReturn(companies.subList(0,1));

        List<Company> resultCompanies = companyService.findByPage(page, pageSize);

        verify(companyRepository).findByPage(page, pageSize);
        assertEquals(companies.subList(0,1),resultCompanies);
    }
    @Test
    void should_create_company_when_post_given_company_details() {
        Company company = new Company(100, "spring", new ArrayList<>());
        when(companyRepository.create(company)).thenReturn(company);

        Company createdCompany = companyService.create(company);

        verify(companyRepository).create(company);
        assertEquals(100, createdCompany.getId());
        assertEquals("spring", createdCompany.getName());
        assertEquals(new ArrayList<>(), createdCompany.getEmployees());
    }
    @Test
    void should_update_company_when_put_given_companyId_and_company(){
        Integer id = 100;
        Company company = new Company(id, "spring", new ArrayList<>());
        Company newCompany = new Company(id, "newSpring", new ArrayList<>());
        when(companyRepository.findById(id)).thenReturn(company);

        Company resultCompany = companyService.update(id,newCompany);

        verify(companyRepository).findById(id);
        assertEquals(100,resultCompany.getId());
        assertEquals("newSpring", resultCompany.getName());
    }
    @Test
    void should_delete_company_when_delete_given_companyId() {
        Integer id = 100;
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(id, "spring", new ArrayList<>()));
        //companies.add(new Company(101, "autumn", new ArrayList<>()));

        companyService.delete(id);

        verify(companyRepository).delete(id);
//        assertEquals("101", companies.get(0).getId());
//        assertEquals("autumn", companies.get(0).getName());
        assertEquals(0,companyService.findAll().size());
    }
}
