package shein.dmitriy.diasoft.interfaces;

import shein.dmitriy.diasoft.dto.UserDTO;

public interface IUserProvider {
    boolean addUser(UserDTO userDTO);

    void auditReg(UserDTO userDTO);
}
