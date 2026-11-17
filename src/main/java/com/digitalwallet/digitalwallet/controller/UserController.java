
package com.digitalwallet.digitalwallet.controller;

import com.digitalwallet.digitalwallet.dto.RegistrationRequest;
import com.digitalwallet.digitalwallet.dto.UserResponse;
import com.digitalwallet.digitalwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.digitalwallet.digitalwallet.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private  UserService userService;

//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(
            @Valid @RequestBody RegistrationRequest request) {

        User user = new User();
        user.setFullName(request.getFullName());
        user.setMobileNumber(request.getMobileNumber());
        user.setEmail(request.getEmail());

        // Pass the submitted password to the service for hashing.
        user.setPasswordHash(request.getPassword());

        User savedUser = userService.registerUser(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getMobileNumber(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );
    }
}
