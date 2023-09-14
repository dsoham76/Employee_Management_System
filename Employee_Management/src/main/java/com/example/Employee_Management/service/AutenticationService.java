package com.example.Employee_Management.service;

import com.example.Employee_Management.model.AuthenticationToken;
import com.example.Employee_Management.model.Employee;
import com.example.Employee_Management.repository.IAuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutenticationService {
    @Autowired
    IAuthRepo authenticationRepo;



    public boolean authenticate(String email, String authTokenValue)
    {
        AuthenticationToken authToken = authenticationRepo.findFirstByTokenValue(authTokenValue);

        if(authToken == null)
        {
            return false;
        }

        String tokenConnectedEmail = authToken.getEmployee().getEmail();

        return tokenConnectedEmail.equals(email);
    }

    public void saveAuthToken(AuthenticationToken authToken)
    {
        authenticationRepo.save(authToken);
    }

    public AuthenticationToken findFirstByEmployee(Employee employee) {
        return authenticationRepo.findFirstByEmployee(employee);
    }

    public void removeToken(AuthenticationToken token) {
        authenticationRepo.delete(token);
    }
}
