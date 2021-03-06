package shein.dmitriy.diasoft.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.entitys.User;
import shein.dmitriy.diasoft.interfaces.IUserProvider;
import shein.dmitriy.diasoft.utilits.CurrDate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private IUserProvider userProvider;
    private CurrDate currDate;

    @Autowired
    public UserService(IUserProvider userProvider, CurrDate currDate) {
        this.userProvider = userProvider;
        this.currDate = currDate;
    }

    public String addUser(UserDTO userDTO) {
        if (!userProvider.checkUser(userDTO)){
            userProvider.addUser(userDTO);
            userProvider.auditReg(userDTO, (short) 1);
            log.info("User: " + userDTO.getName() + " registered successfully. - " + currDate.getDate());
            return "User registered successfully";
        }
        log.info("User: " + userDTO.getName() + " not registered. - " + currDate.getDate());
        return "User not registered";

    }

    public String checkMail(int userID) {
        if (userProvider.findUser(userID)) {
            if(userProvider.auditCheckMailInLog(userID, (short) 2)){
                log.info("UserID: " + userID + " Mail already confirmed. - " + currDate.getDate());
                return "Mail already confirmed";
            }
            if(userProvider.auditAddMailLog(userID, (short) 2)){
                log.info("UserID: " + userID + " Mail confirmed. - " + currDate.getDate());
                return "Mail confirmed";
            }
            return "Mail not confirmed";
        }
        return "User not found";
    }

    public String logInPass(int userID) {
        if (userProvider.findUser(userID)) {
            userProvider.auditLogInPass(userID, (short) 3);
            userProvider.addToken(userID);
            log.info("UserID: " + userID + " Log In - " + currDate.getDate());
            return "User is LogIn";
        } else {
            return "User not found";
        }
    }

    public String logOut(int userID) {
        if (userProvider.findUser(userID)) {
            if (userProvider.auditLogOutCheck(userID, (short) 4)) {
                userProvider.auditAddLogOutLog(userID, (short) 4);
                log.info("UserID: " + userID + " LogOut - " + currDate.getDate());
                return "User is LogOut";
            }
            log.info("UserID: " + userID + " already is LogOut - " + currDate.getDate());
            return "User already is LogOut";
        }
        return "User not found";
    }

    public String logInFail(int userID) {
        if (userProvider.findUser(userID)){
            userProvider.auditLogInFail(userID, (short) 5);
            log.info("UserID: " + userID + " LogIn Fail- " + currDate.getDate());
            return "LogIn Fail";
        } else {
            return "User not found";
        }
    }

    public List<User> audit(int audit) {

        switch (audit){
            case 1:
                log.info("?????????? ???? ?????????? ???1 ???????????? - " + currDate.getDate());
                return userProvider.notConfirmedMail();
            case 2:
                log.info("?????????? ???? ?????????? ???2 ???????????? - " + currDate.getDate());
                return userProvider.lessToken(currDate.getDate());
            case 3:
                log.info("?????????? ???? ?????????? ???3 ???????????? - " + currDate.getDate());
                return userProvider.noLogIn();
            case 4:
                log.info("?????????? ???? ?????????? ???4 ???????????? - " + currDate.getDate());
                return userProvider.failLogin();
        }
        return null;
    }
}
