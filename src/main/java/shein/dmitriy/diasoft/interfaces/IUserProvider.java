package shein.dmitriy.diasoft.interfaces;

import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.entitys.User;

import java.util.List;

public interface IUserProvider {
    boolean findUser(int userID);

    boolean addUser(UserDTO userDTO);

    boolean auditReg(UserDTO userDTO, short aType);

    int auditCheckMail(int userID, short aType);

    boolean auditLogInPass(int userID, short aType);

    boolean auditLogOut(int userID, short aType);

    boolean auditLogInFail(int userID, short aType);

    boolean addToken(int userID);

    List<User> notConfirmedMail();

    List<User> lessToken(String date);

    List<User> noLogIn();
}
