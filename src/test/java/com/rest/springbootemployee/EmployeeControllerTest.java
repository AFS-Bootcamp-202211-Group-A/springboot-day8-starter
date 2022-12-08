package com.rest.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    EmployeeRepository employeeRepository;


    @BeforeEach
    void cleanRepository() {
            employeeRepository.clearAll();
    }

    @Test
    void should_return_all_employee_when_perform_get_all_given_employees() throws Exception {
        // given
        employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 11, "Male", 1000));
        
        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
    }

    @Test
    void should_return_employee_by_id_when_perform_find_by_id_given_employees() throws Exception {
        //given
        Employee jenny = employeeRepository.create(new Employee(10, "Jenny", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Ken", 22, "Male", 20000));
        
        //when
        client.perform(MockMvcRequestBuilders.get("/employees/{id}", jenny.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jenny"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
    }

    @Test
    void should_return_employee_by_gender_when_perform_find_by_gender_given_employees() throws Exception {
        // given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 11, "Male", 1000));

        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", susan.getGender()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
    }

    @Test
    void should_return_employee_by_page_and_pageSize_when_perform_find_by_page_and_pageSize_given_employees() throws Exception {
        //given
        employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 23, "Male", 20000));
        employeeRepository.create(new Employee(13, "Bob3", 24, "Male", 20000));

        //when
        client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}", 1, 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(22, 23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Female", "Male")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(10000, 20000)));
    }

    @Test
    void should_create_employee_when_perform_create_given_employee() throws Exception {
        // given
        Employee susan = new Employee(10, "Susan", 22, "Female", 10000);

        // when & then
        client.perform(MockMvcRequestBuilders.post("/employees")
                        .content(asJsonString(susan))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertEquals("Susan", employee.getName());
        assertEquals(22, employee.getAge());
        assertEquals("Female", employee.getGender());
        assertEquals(10000, employee.getSalary());
    }
    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_update_employee_when_should_update_given_employee_with_id() throws Exception {
        // given
        Employee toUpdateEmployee = new Employee(null, "Susan", 24, "Female", 30000);
        Employee employee = employeeRepository.create(new Employee(1, "Susan", 22, "Female", 10000));

        // when & then
        client.perform(MockMvcRequestBuilders.put("/employees/{id}", employee.getId())
                .content(asJsonString(toUpdateEmployee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(24))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(30000));

        List<Employee> returnedEmployees = employeeRepository.findAll();
        assertThat(returnedEmployees, hasSize(1));
        Employee returnedEmployee = returnedEmployees.get(0);
        assertEquals("Susan", returnedEmployee.getName());
        assertEquals(24, returnedEmployee.getAge());
        assertEquals("Female", returnedEmployee.getGender());
        assertEquals(30000, returnedEmployee.getSalary());
    }

    @Test
    void should_delete_employee_when_perform_delete_given_employees() throws Exception {
        // given
        Employee susan = employeeRepository.create(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.create(new Employee(11, "Bob", 23, "Male", 20000));

        // when & then
        client.perform(MockMvcRequestBuilders.delete("/employees/{id}", susan.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertEquals("Bob", employee.getName());
        assertEquals(23, employee.getAge());
        assertEquals("Male", employee.getGender());
        assertEquals(20000, employee.getSalary());
    }
}