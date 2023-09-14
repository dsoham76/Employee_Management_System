package com.example.Employee_Management.repository;

import com.example.Employee_Management.model.AuthenticationToken;
import com.example.Employee_Management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthRepo extends JpaRepository<AuthenticationToken,Integer> {
    AuthenticationToken findFirstByEmployee(Employee employee);

    AuthenticationToken findFirstByTokenValue(String authTokenValue);
}
