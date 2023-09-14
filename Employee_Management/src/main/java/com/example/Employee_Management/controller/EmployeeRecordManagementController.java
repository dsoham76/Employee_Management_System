package com.example.Employee_Management.controller;

import com.example.Employee_Management.model.AuthenticationToken;
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
public class EmployeeRecordManagementController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    AutenticationService authenticationService;


    // Sign In, Sing Out, Sign Up
    @PostMapping("hr/signup")
    public SignUpOutput signUpUser(@RequestBody Employee employee)
    {
        return employeeService.signUpEmployee(employee);
    }

    @PostMapping("hr/signIn")
    public String sigInUser(@RequestBody @Valid SignInInput signInInput)
    {
        return employeeService.signInEmployee(signInInput);
    }

    @DeleteMapping("hr/signOut")
    public String signOutUser(@RequestParam String email,@RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return employeeService.signOutEmployee(email);
        }
        else {
            return "Sign out not allowed for non authenticated employee.";
        }

    }

    @PostMapping("employee")
    public String addEmployee(@RequestParam String email, @RequestParam String token, @RequestBody Employee employee)
    {
        return employeeService.addEmployee(email,token,employee);
    }

    @DeleteMapping("employee/{employee_id}")
    public String deleteEmployee(String email, String token,@PathVariable int employee_id)
    {
        return employeeService.deleteEmployee(email,token,employee_id);
    }

    @PutMapping("employee")
    public String updateEmployee(@RequestParam String email, @RequestParam String token, @RequestBody Employee employee)
    {
        return employeeService.updateEmployee(email,token,employee);
    }

    @GetMapping("employee/{employee_id}")
    public Employee getEmployee(String email, String token,@PathVariable int employee_id){
        return employeeService.getEmployee(email,token,employee_id);
    }




}
