package com.springtutorial.springboot.service.impl;

import com.springtutorial.springboot.dto.EmployeeDto;
import com.springtutorial.springboot.entity.Employee;
import com.springtutorial.springboot.mapper.EmployeeMapper;
import com.springtutorial.springboot.repository.EmployeeRepository;
import com.springtutorial.springboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        // convert EmployeeDTO into Employee Entity
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> getEmployee(String employeeId) {
        Mono<Employee> foundEmployee = employeeRepository.findById(employeeId);
        return foundEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employeeFlux = employeeRepository.findAll();
        return employeeFlux.map(EmployeeMapper::mapToEmployeeDto).switchIfEmpty(Flux.empty());
    }
}
