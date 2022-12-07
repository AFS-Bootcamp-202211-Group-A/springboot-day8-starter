package com.rest.springbootemployee;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    CompanyRepository companyRepository;

    public List<Company> findAll()  {
        return companyRepository.findAll();
    }
}
