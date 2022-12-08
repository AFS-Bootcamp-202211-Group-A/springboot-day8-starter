package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company update(Integer id, Company company) {
        Company existingCompany = companyRepository.findById(id);
        if (company.getName() != null) {
            existingCompany.setName(company.getName());
        }
        return existingCompany;
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Company> findByPage(int page, int pageSize) {
        return companyRepository.findByPage(page, pageSize);
    }

    public Company create(Company company) {
        return companyRepository.create(company);
    }

    public void delete(Integer companyId) {
        companyRepository.delete(companyId);
    }

    public List<Employee> getEmployees(Integer id) {
        return companyRepository.getEmployees(id);
    }
}
