package shein.dmitriy.diasoft.interfaces;

import shein.dmitriy.diasoft.dto.UserDTO;

public interface IUserProvider {
    boolean addUser(UserDTO userDTO);

    boolean auditReg(UserDTO userDTO, short aType);

    int auditCheckMail(int userID, short aType);
}
