package org.scd.controller;

import org.scd.config.exception.BusinessException;
import org.scd.model.Location;
import org.scd.model.User;
import org.scd.model.dto.UserLocationDTO;
import org.scd.model.dto.UserLoginDTO;
import org.scd.model.dto.UserRegisterDTO;
import org.scd.model.security.CustomUserDetails;
import org.scd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@CrossOrigin
@RestController()
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping(path = "/me")
    public ResponseEntity<User> getCurrentUser() {
        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<User> loginUser(@RequestBody final UserLoginDTO userLoginDTO) throws BusinessException {
        return ResponseEntity.ok(userService.login(userLoginDTO));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> registerUser(@RequestBody final UserRegisterDTO userRegisterDTO) throws BusinessException {
        final User user = userService.register(userRegisterDTO);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
