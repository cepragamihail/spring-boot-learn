package com.springtutorial.springboot.controller;

import com.springtutorial.springboot.dto.EmployeeDto;
import com.springtutorial.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers =  EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        // given - pre-condition or set up
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mihail");
        employeeDto.setLastName("Cepraga");
        employeeDto.setEmail("cepraga.mihail.leon@gmail.com");
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class))).willReturn(Mono.just(employeeDto));

        // when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();
        // then - verify the result or output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }
}