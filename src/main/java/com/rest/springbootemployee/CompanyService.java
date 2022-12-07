package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company update(int companyId, Company companyToBeUpdated) {
        Company existingCompany = companyRepository.findById(companyId);
        if (existingCompany.getName() != null) {
            existingCompany.setName(companyToBeUpdated.getName());
        }
        return existingCompany;
    }

    public Company findById(int companyId) {
        return companyRepository.findById(companyId);
    }

    public Company create(Company company) {
        return companyRepository.create(company);
    }

    public void delete(int companyId) {
        companyRepository.delete(Integer.valueOf(companyId));
    }
}
