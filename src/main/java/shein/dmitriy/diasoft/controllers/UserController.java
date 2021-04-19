package shein.dmitriy.diasoft.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.entitys.User;
import shein.dmitriy.diasoft.services.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "reg")
    public String regUser(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO);
    }

    @GetMapping(value = "checkmail")
    public String checkMail(@RequestParam (value = "userID", required = true) int userID){
        return userService.checkMail(userID);
    }

    @GetMapping(value = "loginpass")
    public String logInPass(@RequestParam (value = "userID", required = true) int userID){
        return userService.logInPass(userID);
    }

    @GetMapping(value = "logout")
    public String logOut(@RequestParam (value = "userID", required = true) int userID){
        return userService.logOut(userID);
    }

    @GetMapping(value = "loginfail")
    public String logInFail(@RequestParam (value = "userID", required = true) int userID){
        return userService.logInFail(userID);
    }

    @GetMapping(value = "audit")
    public List<User> audit(@RequestParam (value = "audit", required = true) int audit){
        return userService.audit(audit);
    }
}
