package com.login.login_app.controllers;

import com.login.login_app.models.userModel.User;
import com.login.login_app.models.userModel.UserPermission;
import com.login.login_app.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;




    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public List<User> getUsers(){
        var users = userService.getAllUsers();
        return users;
    }

    // just admin

   // @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("{userId}")
    public void removeUsers(@PathVariable @RequestBody Long userId){
        userService.deleteUsers(userId);
    }

    // just admin
   // @PreAuthorize("hasAuthority('BLOCK_USER')")
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
