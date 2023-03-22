package com.springtutorial.springboot.controller;

import com.springtutorial.springboot.dto.EmployeeDto;
import com.springtutorial.springboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private EmployeeService employeeService;

    private EmployeeDto employeeDto;

    @BeforeEach
    void prepare() {
        employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mihail");
        employeeDto.setLastName("Cepraga");
        employeeDto.setEmail("cepraga.mihail.leon@gmail.com");
    }

    @Test
    @DisplayName("Unit test to save Employee")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        // given - pre-condition or set up
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

    @Test
    @DisplayName("Unit test to get Employee by id")
    public void givenEmployeeID_whenGetEmployee_thenReturnEmployeeObject() {
        // given - pre-condition or set up
        String employeeId = "123456";
        BDDMockito.given(employeeService.getEmployee(employeeId))
                .willReturn(Mono.just(employeeDto));

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        // then - verify the result or output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    @DisplayName("Unit test to get all Employee")
    public void givenListOfEmployees_whenGetAllEmployees_thenListOfEmployee() {
        // given - pre-condition or set up
        List<EmployeeDto> list = new ArrayList<>();
        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Mihail1");
        employeeDto1.setLastName("Cepraga1");
        employeeDto1.setEmail("cepraga.mihail.leon@gamil.com");
        list.add(employeeDto);
        list.add(employeeDto1);
        Flux<EmployeeDto> employeeFlux = Flux.fromIterable(list);
        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeFlux);

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the result or output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }



}