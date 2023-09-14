package com.example.Employee_Management.repository;

import com.example.Employee_Management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeRepo extends JpaRepository<Employee,Integer> {
    Employee findFirstByEmail(String newEmail);
}
