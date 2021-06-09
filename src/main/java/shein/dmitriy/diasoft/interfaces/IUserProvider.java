package shein.dmitriy.diasoft.interfaces;

import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.entitys.User;

import java.util.List;

public interface IUserProvider {
    boolean findUser(int userID);

    boolean addUser(UserDTO userDTO);

    boolean checkUser(UserDTO userDTO);

    boolean auditReg(UserDTO userDTO, short aType);

    boolean auditCheckMailInLog(int userID, short aType);

    boolean auditAddMailLog(int userID, short aType);

    boolean auditLogInPass(int userID, short aType);

    boolean auditLogOutCheck(int userID, short aType);

    boolean auditAddLogOutLog(int userID, short aType);

    boolean auditLogInFail(int userID, short aType);

    boolean addToken(int userID);

    List<User> notConfirmedMail();

    List<User> lessToken(String date);

    List<User> noLogIn();

    List<User> failLogin();
}
