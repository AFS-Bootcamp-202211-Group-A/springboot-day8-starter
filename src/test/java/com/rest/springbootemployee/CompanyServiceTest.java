package com.rest.springbootemployee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

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
    void should_return_all_companies_when_findAll_given_companies() {
        //given
        List<Company> companies = new ArrayList<>();
        Company companyA = new Company(1, "Company A", new ArrayList<>());
        Company companyB = new Company(2, "Company B", new ArrayList<>());

        companies.add(companyA);
        companies.add(companyB);


        when(companyRepository.findAll()).thenReturn(companies);

        //when
        List<Company> result = companyService.findAll();

        //then
        //1. verify data
        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalTo(companyA));
        assertThat(result.get(1), equalTo(companyB));

        //2. verify interaction
        //spy
        verify(companyRepository).findAll();
    }

}