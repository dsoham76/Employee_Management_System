package com.example.Employee_Management.service;

import com.example.Employee_Management.model.AuthenticationToken;
import com.example.Employee_Management.model.Employee;
import com.example.Employee_Management.model.dto.SignInInput;
import com.example.Employee_Management.model.dto.SignUpOutput;
import com.example.Employee_Management.repository.IEmployeeRepo;
import com.example.Employee_Management.service.emailUtility.EmailHandler;
import com.example.Employee_Management.service.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    IEmployeeRepo employeeRepo;


    @Autowired
    AutenticationService authenticationService;



    public SignUpOutput signUpEmployee(Employee employee) {

        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        String newEmail = employee.getEmail();

        if(newEmail == null)
        {
            signUpStatusMessage = "Invalid email";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }

        if(!employee.getRole().equals("HR_Manager")){
            signUpStatusMessage = "You are not authorized to view this page";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);

        }

        //check if this user email already exists ??
        Employee existingEmployee = employeeRepo.findFirstByEmail(newEmail);

        if(existingEmployee != null)
        {
            signUpStatusMessage = "Email already registered!!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(employee.getPassword());

            //saveAppointment the user with the new encrypted password

            employee.setPassword(encryptedPassword);
            employeeRepo.save(employee);

            return new SignUpOutput(signUpStatus, "Employee registered successfully!!!");
        }
        catch(Exception e)
        {
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
    }


    public String signInEmployee(SignInInput signInInput) {


        String signInStatusMessage = null;

        String signInEmail = signInInput.getEmail();

        if(signInEmail == null)
        {
            signInStatusMessage = "Invalid email";
            return signInStatusMessage;


        }

        //check if this user email already exists ??
        Employee existingEmployee = employeeRepo.findFirstByEmail(signInEmail);

        if(existingEmployee == null)
        {
            signInStatusMessage = "Email not registered!!!";
            return signInStatusMessage;

        }

        //match passwords :

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if(existingEmployee.getPassword().equals(encryptedPassword))
            {
                //session should be created since password matched and user id is valid
                AuthenticationToken authToken  = new AuthenticationToken(existingEmployee);
                authenticationService.saveAuthToken(authToken);

                EmailHandler.sendEmail(signInEmail,"Authentication Token for your Account",authToken.getTokenValue());
                return "Token sent to your email";
            }
            else {
                signInStatusMessage = "Invalid credentials!!!";
                return signInStatusMessage;
            }
        }
        catch(Exception e)
        {
            signInStatusMessage = "Internal error occurred during sign in";
            return signInStatusMessage;
        }

    }


    public String signOutEmployee(String email) {

        Employee employee = employeeRepo.findFirstByEmail(email);
        AuthenticationToken token = authenticationService.findFirstByEmployee(employee);
        authenticationService.removeToken(token);
        return "Employee Signed out successfully";
    }


    public String addEmployee(String email, String token, Employee employee) {
        if(authenticationService.authenticate(email,token)) {
            employeeRepo.save(employee);
            return "Employee has been added in the database.";
        }
        else {
            return "Not an Authenticated HR activity!!!";
        }
    }

    public String deleteEmployee(String email, String token, int employee_id) {
        Employee employee=employeeRepo.getReferenceById(employee_id);
        if(authenticationService.authenticate(email,token)) {
            if(employee!=null){
                employeeRepo.delete(employee);
                return "Employee has been deleted in the database.";
            }else{
                return "No such employee exists in the database";
            }

        }
        else {
            return "Not an Authenticated HR activity!!!";
        }
    }

    public String updateEmployee(String email, String token, Employee employee) {
        if(authenticationService.authenticate(email,token)) {
            employeeRepo.save(employee);
            return "Employee details are updated in the database.";
        }
        else {
            return "Not an Authenticated HR activity!!!";
        }
    }

    public Employee getSelfService(String email) {
        Employee employee=employeeRepo.findFirstByEmail(email);
        if(employee!=null){
            return employee;
        }else{
            return null;
        }
    }

    public Employee getEmployee(String email, String token, int employee_id) {
        Employee employee=employeeRepo.getReferenceById(employee_id);
        if(authenticationService.authenticate(email,token) && employee!=null) {
            return employee;
        }else
            return null;
    }

    public List<Employee> getEmployees(String email, String token) {
        if(authenticationService.authenticate(email,token)) {
            return employeeRepo.findAll();
        }
        else
            return null;
    }
}
