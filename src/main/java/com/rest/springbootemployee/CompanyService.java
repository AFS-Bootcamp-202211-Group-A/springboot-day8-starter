package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    CompanyRepository companyRepository;

    public List<Company> findAll()  {
        return companyRepository.findAll();
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Employee> getEmployees(int id) {
        return companyRepository.getEmployees(id);
    }

    public List<Company> findByPage(int page, int pageSize) {
        return companyRepository.findByPage(page, pageSize);
    }

    public Company create(Company company) {
        return companyRepository.create(company);
    }
}
