package com.example.myApp.Service;

import org.springframework.stereotype.Service;

@Service
public class MyAppService {

    public String getUserDetails(String email) {
        return email;
    }
}
