package com.springtutorial.springboot.controller;

import com.springtutorial.springboot.dto.EmployeeDto;
import com.springtutorial.springboot.repository.EmployeeRepository;
import com.springtutorial.springboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;
    @BeforeEach
    public void before() {
        System.out.println("Before each test");
        employeeRepository.deleteAll().subscribe();
    }

    @Test
    @DisplayName("Integration test to save Employee")
    public void saveEmployeeTest() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mihail");
        employeeDto.setLastName("Cepraga");
        employeeDto.setEmail("cepraga.mihail.leon@gmail.com");

        webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    @DisplayName("Integration test to get single Employee")
    public void getEmployeeTest() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mihail_");
        employeeDto.setLastName("Cepraga_");
        employeeDto.setEmail("cepraga.mihail.leon@gmail.com");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        assert savedEmployee != null;
        webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    @DisplayName("Integration test to get all Employee")
    public void getAllEmployeesTest() {
        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Mihail_001");
        employeeDto1.setLastName("Cepraga_001");
        employeeDto1.setEmail("cepraga.mihail.leon@gmail.com");

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Mihail_002");
        employeeDto2.setLastName("Cepraga_002");
        employeeDto2.setEmail("cepraga.mihail.leon@gmail.com");

        employeeService.saveEmployee(employeeDto1).block();
        employeeService.saveEmployee(employeeDto2).block();

        webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("Integration test to update Employee")
    public void updateEmployeeTest() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mihail_N");
        employeeDto.setLastName("Cepraga_N");
        employeeDto.setEmail("cepraga.mihail.leon@gmail.com");
        EmployeeDto savedEmployeeDto = employeeService.saveEmployee(employeeDto).block();

        EmployeeDto updatedEmployeeDto = new EmployeeDto();
        updatedEmployeeDto.setFirstName("Mihail_U");
        updatedEmployeeDto.setLastName("Cepraga_U");
        updatedEmployeeDto.setEmail("cepraga.mihail.leon@gmail.com");

        assert savedEmployeeDto != null;

        webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id",savedEmployeeDto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployeeDto.getLastName());
    }
}
