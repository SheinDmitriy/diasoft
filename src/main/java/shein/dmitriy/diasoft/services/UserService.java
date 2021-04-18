package shein.dmitriy.diasoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shein.dmitriy.diasoft.interfaces.IUserProvider;

@Service
public class UserService {
    private IUserProvider UserProvider;

    @Autowired
    public UserService(IUserProvider userProvider) {
        UserProvider = userProvider;
    }
}
