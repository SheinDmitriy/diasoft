package shein.dmitriy.diasoft.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.interfaces.IUserProvider;
import shein.dmitriy.diasoft.utilits.CurrDate;

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

    public boolean addUser(UserDTO userDTO) {
        if (userProvider.addUser(userDTO)){
            userProvider.auditReg(userDTO);
            log.info("User: " + userDTO.getName() + " registered successfully. - " + currDate.getDate());
            return true;
        } else {
            log.info("User: " + userDTO.getName() + " not registered. - " + currDate.getDate());
            return false;
        }
    }
}
