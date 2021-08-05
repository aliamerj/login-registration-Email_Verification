package com.login.login_app.controllers;

import com.login.login_app.models.userModel.User;
import com.login.login_app.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    //todo :set authority for each admin and user

    // just admin
    @GetMapping("/allusers")
    public List<User> getUsers(){
        var users = userService.getAllUsers();
        return users;
    }

    // just admin
    @DeleteMapping("{userId}")
    public void removeUsers(@PathVariable @RequestBody Long userId){
        userService.deleteUsers(userId);
    }
    // just admin
    @PutMapping("{userId}/{block}")
    public void blockUsers (@PathVariable @RequestBody Long userId ,@PathVariable boolean block){
        userService.blockUser(userId , block);
    }

    // user and admin
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId){
       var user = userService.getUser(userId);
       return user;

    }




}
