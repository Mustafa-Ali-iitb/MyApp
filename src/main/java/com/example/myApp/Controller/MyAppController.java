package com.example.myApp.Controller;

import com.example.myApp.Service.MyAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/my-app")
public class MyAppController {

    @Autowired
    private MyAppService myAppService;

    @GetMapping(value = "/user-details")
    public ResponseEntity<String> getUserDetails(@RequestParam String email) {
        String response = myAppService.getUserDetails(email);
        return ResponseEntity.ok(response);
    }
}
