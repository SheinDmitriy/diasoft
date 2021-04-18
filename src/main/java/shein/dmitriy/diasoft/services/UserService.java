package shein.dmitriy.diasoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import shein.dmitriy.diasoft.interfaces.IUserProvider;

public class UserService {
    private IUserProvider UserProvider;

    @Autowired
    public UserService(IUserProvider userProvider) {
        UserProvider = userProvider;
    }
}
