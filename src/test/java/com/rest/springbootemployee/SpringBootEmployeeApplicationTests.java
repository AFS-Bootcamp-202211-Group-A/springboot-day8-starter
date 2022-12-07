package com.rest.springbootemployee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootEmployeeApplicationTests {

	@Autowired
	MockMvc client;

	@Autowired
	EmployeeRepository employeeRepository;

	@BeforeEach
	void cleanRepository() {
			employeeRepository.clearAll();
	}

	@Test
	void should_get_all_employees_when_perform_get_given_employees() throws Exception {
	    //given
	    employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		employeeRepository.create(new Employee(2, "Bob", 21, "Male", 5000));
	    //when
	    client.perform(MockMvcRequestBuilders.get("/employees"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(20,21)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Female", "Male")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(10000, 5000)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")));

	    //then
	}

	@Test
	void should_get_employee_when_perform_get_by_id_given_employees() throws Exception {
		//given
		Employee employee = employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees/{id}", employee.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("name").value("Susan"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));

		//then
	}

	@Test
	void should_get_employee_with_specific_gender_when_perform_get_by_gender_given_employees() throws Exception {
		//given
		Employee susan = employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		Employee bob = employeeRepository.create(new Employee(2, "Bob", 20, "Male", 10000));
		Employee peter = employeeRepository.create(new Employee(3, "Peter", 20, "Male", 10000));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", "Male"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Bob", "Peter")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(20, 20)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Male", "Male")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(10000, 10000)));

		//then
	}

	@Test
	void should_get_2_employees_when_perform_get_by_page_given_employees() throws Exception {
		//given
		Employee susan = employeeRepository.create(new Employee(1, "Susan", 20, "Female", 10000));
		Employee bob = employeeRepository.create(new Employee(2, "Bob", 20, "Male", 10000));
		Employee peter = employeeRepository.create(new Employee(3, "Peter", 22, "Male", 44));
		Employee sam = employeeRepository.create(new Employee(2, "Sam", 23, "Male", 45));
		Employee ted = employeeRepository.create(new Employee(3, "Ted", 20, "Male", 10000));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees?page={page}&pageSize={pageSize}", "2", "2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(22,23)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("Male", "Male")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(44, 45)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Peter", "Sam")));

	}

	@Test
	void should_create_new_employee_when_perform_post_given_new_employee() throws Exception {
		Employee newEmployee = new Employee(1, "Tom", 19, "Male", 15000);
		String newEmployeeJson = new ObjectMapper().writeValueAsString(newEmployee);
	    //given

		//when
		client.perform(MockMvcRequestBuilders.post("/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newEmployeeJson))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tom"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(19))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(15000))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"));

	    //then
		List<Employee> employees = employeeRepository.findAll();
		assertThat(employees, hasSize(1));
		Employee employee = employees.get(0);
		assertThat(employee.getName(), equalTo("Tom"));
		assertThat(employee.getAge(), equalTo(19));
		assertThat(employee.getSalary(), equalTo(15000));
		assertThat(employee.getGender(), equalTo("Male"));
	}

	@Test
	void should_update_existing_employee_when_perform_put_given_employee() throws Exception {
		Employee tom = employeeRepository.create(new Employee(1, "Tom", 19, "Male", 15000));

		Employee tomUpdated = new Employee(1, "Tom", 20, "Male", 25000);
		String newEmployeeJson = new ObjectMapper().writeValueAsString(tomUpdated);

		//given

		//when
		client.perform(MockMvcRequestBuilders.put("/employees/{id}", tom.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(newEmployeeJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tom"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(25000))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"));

		//then
		List<Employee> employees = employeeRepository.findAll();
		assertThat(employees, hasSize(1));
		Employee employee = employees.get(0);
		assertThat(employee.getName(), equalTo("Tom"));
		assertThat(employee.getAge(), equalTo(20));
		assertThat(employee.getSalary(), equalTo(25000));
		assertThat(employee.getGender(), equalTo("Male"));
	}

	@Test
	void should_delete_existing_employee_when_perform_delete_given_employee() throws Exception {
		Employee tom = employeeRepository.create(new Employee(1, "Tom", 19, "Male", 15000));


		//given

		//when
		client.perform(MockMvcRequestBuilders.delete("/employees/{id}", tom.getId()))
				.andExpect(MockMvcResultMatchers.status().isNoContent());


		//then
		List<Employee> employees = employeeRepository.findAll();
		assertThat(employees, empty());
	}

}
