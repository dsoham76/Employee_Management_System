package com.example.Employee_Management.controller;

import com.example.Employee_Management.model.Employee;
import com.example.Employee_Management.model.dto.SignInInput;
import com.example.Employee_Management.model.dto.SignUpOutput;
import com.example.Employee_Management.service.AutenticationService;
import com.example.Employee_Management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
public class EmployeeSelfServiceController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    AutenticationService authenticationService;


    @PostMapping("employee/signIn")
    public String sigInUser(@RequestBody @Valid SignInInput signInInput)
    {
        return employeeService.signInEmployee(signInInput);
    }

    @DeleteMapping("employee/signOut")
    public String signOutUser(@RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return employeeService.signOutEmployee(email);
        }
        else {
            return "Sign out not allowed for non authenticated employee.";
        }

    }

    @GetMapping("employee")
    public Employee getSelfService(String email){
        return employeeService.getSelfService(email);
    }
}
