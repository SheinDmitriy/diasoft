package shein.dmitriy.diasoft.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.services.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "reg")
    public String regUser(@RequestBody UserDTO userDTO){
        if(userService.addUser(userDTO)){
            return "User registered successfully";
        } else {
            return "Error";
        }
    }
}
